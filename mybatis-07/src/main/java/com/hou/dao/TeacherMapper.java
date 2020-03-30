package com.hou.dao;

import com.hou.pojo.Teacher;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TeacherMapper {

//    List<Teacher> getTeacher();

    //获取指定老师下的所有学生
    Teacher getTeacher(@Param("id") int id);
    Teacher getTeacher2(@Param("id") int id);

}
