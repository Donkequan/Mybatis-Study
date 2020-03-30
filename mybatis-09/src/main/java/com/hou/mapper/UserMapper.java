package com.hou.mapper;

import com.hou.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {

    User queryUserByid(@Param("id") int id);

    void updateUser(User user);

}
