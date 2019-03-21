package bill.shawn.dao;

import bill.shawn.domain.Role;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface IRoleDao {
    /*
    * 通过id查询角色
    * */
    @Select("select*from role  where id  in " +
            "(select rid from user_role where uid=#{id})")
    @Results(id="role",value = {
            @Result(id=true,column ="id" ,property ="roleId" ),
            @Result(column = "role_name",property = "roleName"),
            @Result(column = "role_desc",property = "roleDesc")
    })
    Role findRoleByUid(Integer uid);
    /*
    *
    * 查询所有的角色表对应的用户
    *
    * */
  List<Role> manyToMany();
}
