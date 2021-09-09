package example.dao;

import example.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

public interface UserDao {
    //@Select("select * from tb_user")
    List<Map<String,Object>> findAll();

    //@Select("select userName,password from tb_user where userName = #{userName}")
    User getUser(String userName);

    //@Insert("insert into tb_user(userName,password) values(#{userName},#{password})")
    int addUser(@Param("userName")String userName, @Param("password")String password);

    //@Insert("insert into tb_progress(userName,points,status) values(#{userName},0,'')")
    int addProgress(String userName);

    //@Update("update tb_progress set points = #{points} where userName = #{userName}")
    int savePoints(@Param("points")int points, @Param("userName")String userName);

    //@Select("select points from tb_progress where userName = #{userName}")
    int getPoints(String userName);

    //@Update("update tb_progress set status = #{status} where userName = #{userName}")
    int saveStatus(@Param("status")String status, @Param("userName")String userName);

    //@Select("select status from tb_progress where userName = #{userName}")
    String getStatus(String userName);
}
