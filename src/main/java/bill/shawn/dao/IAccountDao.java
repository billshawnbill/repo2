package bill.shawn.dao;

import bill.shawn.domain.Account;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

public interface IAccountDao {

    /*
    * 通过id查询用户
    *
    * */
    @Select("select*from account where uid=#{uid}")
    Account findAccountByUid(Integer uid);
    /*
     * 查询所有账户及所有对应的用户信息--左外连接--.多对一
     * select="bill.shawn.dao.IUserDao.findUserById")
     * */
    @Select("select*from account ")
    @Results(id="myAccount",value = {
            @Result(id=true ,column ="aid",property = "id"),
            @Result(column = "uid",property = "uid"),
            @Result(column = "money",property = "money"),
            @Result(property ="user",column = "uid",one=@One(
                    select = "bill.shawn.dao.IUserDao.findUserById",
                    fetchType = FetchType.EAGER))
    })
    List<Account> oneToManyLeft();
}
