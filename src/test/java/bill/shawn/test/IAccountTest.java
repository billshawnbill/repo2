package bill.shawn.test;

import bill.shawn.dao.IAccountDao;
import bill.shawn.dao.IUserDao;
import bill.shawn.domain.Account;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class IAccountTest {
    private SqlSession sqlSession;
    private InputStream in;
    private IAccountDao dao;
    //before注解,在运行之前会运行,用于加载配置文件
    @Before
    public void init() throws IOException {
        //1.读取配置文件
        in = Resources.getResourceAsStream("SqlMapperConfig.xml");
        //2.创建sqlsessionfactory
        //2.1获取工厂创建对象-->工厂-->session-->获取映射的代理对象-->对象调方法
        SqlSessionFactoryBuilder buider = new SqlSessionFactoryBuilder();
        //2.2将流化配置文件传入创建目标对象
        SqlSessionFactory factory = buider.build(in);
        //2.3获取sqlsession对象
        sqlSession = factory.openSession();
        //2.4通过session来获取IUserDao的代理对象
        dao = sqlSession.getMapper(IAccountDao.class);
    }
    //after注解,用于释放资源
    @After
    public void destroy() throws IOException {
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
        in.close();
    }


    /*
     * 查询所有账户及对应的用户信息--左外连接
     * */
    @Test
    public void oneToManyLeftTest(){
        List<Account> accounts = dao.oneToManyLeft();
        for (Account account : accounts) {
            System.out.println("account = " + account);
        }
    }
}
