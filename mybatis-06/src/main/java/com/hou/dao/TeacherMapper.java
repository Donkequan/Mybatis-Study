package com.hou.dao;

import com.hou.pojo.Teacher;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface TeacherMapper {

    @Select("select * from teacher where id =#{tid}")
    Teacher getTeacher(@Param("tid") int id);

}
