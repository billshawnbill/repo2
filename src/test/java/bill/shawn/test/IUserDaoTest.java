package bill.shawn.test;

import bill.shawn.dao.IUserDao;
import bill.shawn.domain.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class IUserDaoTest {
    private SqlSession sqlSession;
    private InputStream in;
    private IUserDao dao;

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
        sqlSession = factory.openSession();//其中加true,表示自动提交,但是实际应用中采取手动提交
        //2.4通过session来获取IUserDao的代理对象
        dao = sqlSession.getMapper(IUserDao.class);
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
     * 查询所有用户
     * */
    @Test
    public void findAllTest() throws IOException {
        //
        User user1 = new User();
        user1.setAddress("北京");
        user1.setUsername("老王");
        List<User> list = dao.findAll(user1);
        //2.6遍历输出
        for (User user : list) {
            System.out.println("user = " + user);
        }
    }

    /*
    * 添加用户
    * */
    @Test
    public void addUserTest(){
        User user = new User();
        user.setUsername("张三");
        user.setSex("女");
        user.setBirthday(new Date());
        user.setAddress("北京市中南海");
        Integer integer = dao.addUser(user);
        System.out.println(user);
    }
    /*
    * 删除用户
    * */
    @Test
    public void deleteUserByIdTest(){
        Integer integer = dao.deleteUserById(52);
        System.out.println("integer = " + integer);
    }

    /*
    * 更新用户信息
    * */
    @Test
    public void updateUserTest(){
        User user = new User();
        user.setUsername("惊奇队长");
        user.setSex("女");
        user.setId(43);
        Integer integer = dao.updateUser(user);
        System.out.println("integer = " + integer);
        System.out.println(user);
    }

    /*
    *查询所有的用户并显示所对应的账户信息
    * */
    @Test
    public void findUserAccountTest(){
        List<User> userAccount = dao.findUserAccount();
        for (User user : userAccount) {
            System.out.println("user = " + user);
        }
    }
    /*
     *查询所有的用户并显示所对应的账户信息和角色信息
     * */
    @Test
    public void manyToMayTest(){
        List<User> users = dao.manyToMany();
        for (User user : users) {
            System.out.println("user = " + user);
        }
    }
}
