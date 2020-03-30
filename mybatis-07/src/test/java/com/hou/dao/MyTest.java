package com.hou.dao;

import com.hou.pojo.Student;
import com.hou.pojo.Teacher;
import com.hou.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

public class MyTest {

    @Test
    public void test(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        try{
            TeacherMapper teacherMapper = sqlSession.getMapper(TeacherMapper.class);
            Teacher teacher = teacherMapper.getTeacher2(1);
            System.out.println(teacher);

//            Teacher(id=1, name=hou,
//            studentList=[
//            Student(id=1, name=xiao1, tid=0),
//            Student(id=2, name=xiao2, tid=0),
//            Student(id=3, name=xiao3, tid=0),
//            Student(id=4, name=xiao4, tid=0),
//            Student(id=5, name=xiao5, tid=0)])

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            sqlSession.close();
        }
    }

}
