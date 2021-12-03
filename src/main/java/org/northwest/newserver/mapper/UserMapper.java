package org.northwest.newserver.mapper;

import org.apache.ibatis.annotations.*;
import org.northwest.newserver.pojo.Record;
import org.northwest.newserver.pojo.User;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Mapper
@Repository
public interface UserMapper {
    // 查询所有人
    @Select("select * from tb_user")
    List<User> getAllUser();

    // 查询单人信息
    @Select("select * from tb_user where userName = #{userName}")
    User getUser(@Param("userName")String userName);

    // 查询单人用户详细信息
    @Select("SELECT * FROM  tb_user,tb_progress  where tb_progress.userName = tb_user.userName " +
            "and tb_user.userName = #{userName}")
    Map<String,Object> getUserInfo(@Param("userName")String userName);

    // 更新个人信息
    @Update("update tb_user " +
            "set realName = #{realName}, " +
            "userName = #{userName}, " +
            "birthday = #{birthday}, " +
            "age = #{age}, " +
            "phoneNumber = #{phoneNumber}, " +
            "sex = #{sex}, " +
            "address = #{address} " +
            "where userName = #{userName}"
    )
    int updateUserInfo(User user);

    // 增加一个用户
    @Insert("insert into tb_user (uuid, userName, password) values(#{uuid}, #{userName}, #{password})")
    int addUser(@Param("uuid")String uuid, @Param("userName")String userName, @Param("password")String password);

    // 添加进度
    @Insert("insert into tb_progress(userName,points,state) values(#{userName},0,\"{scene:0,story:3}\")")
    int addProgress(String userName);

    // 更新分数
    @Update("update tb_progress set points = #{points} where userName = #{userName}")
    int savePoints(@Param("points")int point, @Param("userName")String userName);

    // 获取分数】
    @Select("select points from tb_progress where userName = #{userName}")
    int getPoints(String userName);

    // 保存进度
    @Update("update tb_progress set state = #{state} where userName = #{userName}")
    int savaState(@Param("state")String state, @Param("userName")String userName);

    // 查看进度
    @Select("select state from tb_progress where userName = #{userName}")
    String getStatus(String userName);

    // 获取某个用户所有日志
    @Select("select * from tb_gameRecord where userName = #{userName}")
    List<Record> getGameRecord(@Param("userName")String userName);

    // 记录日志
    @Update("update tb_gameRecord set record = #{record} where rid = #{rid}")
    int saveGameRecord(@Param("rid")String rid, @Param("record")String record);

    // 增加一条日志
    @Insert("insert into tb_gameRecord values(#{rid}, #{userName}, #{record}, #{datetime})")
    int addRecord(@Param("rid") String rid, @Param("userName")String userName, @Param("record")String record, @Param("datetime") String datetime);

}
