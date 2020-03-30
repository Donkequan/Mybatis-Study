import com.hou.mapper.UserMapper;
import com.hou.pojo.User;
import com.hou.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

public class MyTest {

    @Test
    public void addBlog(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        SqlSession sqlSession1 = MybatisUtils.getSqlSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        User user = userMapper.queryUserByid(1);
        System.out.println(user);
        sqlSession.close();

        UserMapper userMapper1 = sqlSession1.getMapper(UserMapper.class);
        User user1 = userMapper1.queryUserByid(1);
        System.out.println(user1);
        System.out.println(user==user1);
        sqlSession1.close();
    }


}
