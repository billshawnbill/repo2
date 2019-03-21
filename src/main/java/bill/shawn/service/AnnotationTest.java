package bill.shawn.service;

import bill.shawn.dao.IUserDao;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class AnnotationTest {
    public static void main(String[] args) throws IOException {
        //1.加载配置文件
        InputStream in = Resources.getResourceAsStream("jdbconfig.properties");
        //2.创建sqlsession工厂
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(in);
        //3.创建sqlsession
        SqlSession session = factory.openSession();
        //4.通过session获取实体类的代理对象
        IUserDao userDao = session.getMapper(IUserDao.class);
        //5.调用方方法
        //5.释放资源
        session.commit();
        session.close();
        in.close();




    }

}
