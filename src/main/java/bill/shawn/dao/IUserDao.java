package bill.shawn.dao;

import bill.shawn.domain.Account;
import bill.shawn.domain.User;
import bill.shawn.util.SimpleSelectLangDriver;
import bill.shawn.util.SimpleUpdateLangDriver;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;
import java.util.Map;
//开启二级缓存
@CacheNamespace(blocking = true)
public interface IUserDao {
    /*
    * 通过id查找用户
    * */
    @Select("select*from user where id=#{id}")
    User findUserById(Integer id);

    /*
     * 添加用户
     *
     * */
    @SelectKey(statement = "select last_insert_id()",keyProperty ="id" ,keyColumn ="id" ,before = false,resultType = Integer.class)
    @Insert("insert into user(username,birthday,sex,address) values(#{username},#{birthday},#{sex},#{address})")
     Integer addUser(User user);
    /*
     * 删除用户
     * */
    @Delete("delete from user where id =#{id}")
    Integer deleteUserById(Integer id);
    /*
     * 更新用户信息
     * */
    @Update("update user set (#{condition}) where id=#{id}")
    @Lang(SimpleUpdateLangDriver.class)
    Integer updateUser(User user);

    /*
     * 查询所有用户
     * */
    @Select("select*from user (#{field})")
    @Lang(SimpleSelectLangDriver.class)
    List<User> findAll(User user);

    /*
     * 查询所有用户和其对应的账户信息-->一对多
     * */
    @Select(value ={"select*from user"})
    @Results(id="myUser",value={
            @Result(id=true,column = "id",property = "id"),
            @Result(column = "username",property = "username"),
            @Result(column = "address",property = "address"),
            @Result(column = "sex",property = "sex"),
            @Result(column = "birthday",property = "birthday"),
            @Result(property ="accounts",column ="id",many=@Many(
            select = "bill.shawn.dao.IAccountDao.findAccountByUid"
            ,fetchType = FetchType.LAZY
            ))
    })
    List<User> findUserAccount();

    /*
    * 查询用户对应的所有角色-->多对多
    * */
    @Select("select*from user  ")
    @Results(id="roleUser",value = {
            @Result(id=true,column = "id",property = "id"),
            @Result(column = "username",property = "username"),
            @Result(column = "address",property = "address"),
            @Result(column = "sex",property = "sex"),
            @Result(column = "birthday",property = "birthday"),
            @Result(property = "roles",column = "id",many=@Many(
                    select ="bill.shawn.dao.IRoleDao.findRoleByUid",
                    fetchType = FetchType.LAZY
            ))
    })
    List<User> manyToMany();
}
