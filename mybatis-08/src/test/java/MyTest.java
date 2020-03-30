import com.hou.mapper.BlogMapper;
import com.hou.pojo.Blog;
import com.hou.utils.IDUtiles;
import com.hou.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.*;

public class MyTest {

    @Test
    public void addBlog(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        BlogMapper blogMapper = sqlSession.getMapper(BlogMapper.class);

        Blog blog = new Blog();
        blog.setId(IDUtiles.getId());
        blog.setAuthor("houdongun");
        blog.setCreateTime(new Date());
        blog.setViews(999);
        blog.setTitle("first");

        blogMapper.addBlog(blog);

        blog.setId(IDUtiles.getId());
        blog.setTitle("second");
        blogMapper.addBlog(blog);

        blog.setId(IDUtiles.getId());
        blog.setTitle("third");
        blogMapper.addBlog(blog);

        blog.setId(IDUtiles.getId());
        blog.setTitle("forth");
        blogMapper.addBlog(blog);

        sqlSession.close();
    }

    @Test
    public void queryBlogIF(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        BlogMapper blogMapper = sqlSession.getMapper(BlogMapper.class);
        Map map = new HashMap();

        map.put("title", "first");
//        map.put("author", "houdongun");
//        map.put("views", 999);

        List<Blog> list = blogMapper.queryBlogIF(map);

        for (Blog blog : list) {
            System.out.println(blog);
        }

        sqlSession.close();
    }

    @Test
    public void updateBlog() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        BlogMapper blogMapper = sqlSession.getMapper(BlogMapper.class);
        Map map = new HashMap();

        map.put("title", "second23");
//        map.put("author", "houdongun3");
        map.put("id", "bd4f2a83c3b843ab925dcd4d9aa6fbeb");

        blogMapper.updateBlog(map);

        sqlSession.close();

    }

    @Test
    public void queryBlogForeach(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        BlogMapper blogMapper = sqlSession.getMapper(BlogMapper.class);
        Map map = new HashMap();

        ArrayList<Integer> ids = new ArrayList<Integer>();
        ids.add(1);
        ids.add(3);
        map.put("ids",ids);

        List<Blog> list = blogMapper.queryBlogForeach(map);

        for (Blog blog : list) {
            System.out.println(blog);
        }

        sqlSession.close();
    }

}
