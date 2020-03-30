package com.hou.mapper;

import com.hou.pojo.Blog;

import java.util.List;
import java.util.Map;

public interface BlogMapper {

    void addBlog(Blog blog);

    List<Blog> queryBlogIF(Map map);

    List<Blog> queryBlogchoose(Map map);

    void updateBlog(Map map);

    List<Blog> queryBlogForeach(Map map);

}
