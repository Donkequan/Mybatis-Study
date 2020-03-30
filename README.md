# 简介

自学的[【狂神JAVA】MyBatis]( https://www.bilibili.com/video/BV1NE411Q7Nx?p=1 ) 

分享自写源码和笔记，希望对大家有帮助

本人配置

- jdk13.0.2 （jdk1.7以上均可）
- Maven 3.6.3 
- MySQL 5.7.23 （mysql5.6以上均可）




# 1. 配置

官网文档： https://mybatis.org/mybatis-3/zh/getting-started.html 

### pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>batis</groupId>
    <artifactId>batis-maven</artifactId>
    <packaging>pom</packaging>
    <version>1.0-SNAPSHOT</version>
    <modules>
        <module>mybatis-01</module>
    </modules>

    <!--导入依赖-->
    <dependencies>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.47</version>
        </dependency>

        <dependency>
        <groupId>org.mybatis</groupId>
        <artifactId>mybatis</artifactId>
        <version>3.5.2</version>
    </dependency>

        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>3.5.2</version>
        </dependency>

        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
        </dependency>
    </dependencies>
</project>
```

### src/main/resources

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">

<configuration>
    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="com.mysql.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://111.230.212.103:3306/mybatis?userSSL=true&amp;
                userUnicode=true&amp;characterEncoding=UTF-8"/>
                <property name="username" value="root"/>
                <property name="password" value="hdk123"/>
            </dataSource>
        </environment>
    </environments>
</configuration>
```

### src/main/java

```java
package com.hou.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

//sqlSessionFactory --> sqlSession
public class MybatisUtils {

    private static SqlSessionFactory sqlSessionFactory;

    static {
        try {
            //使用mybatis第一步：获取sqlSessionFactory对象
            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //既然有了 SqlSessionFactory，顾名思义，我们可以从中获得 SqlSession 的实例。SqlSession 提供了在数据库执行 SQL 命令所需的所有方法。
    // 你可以通过 SqlSession 实例来直接执行已映射的 SQL 语句

    public static SqlSession getSqlSession(){
        return sqlSessionFactory.openSession();
    }

}
```

### 编写代码

- 实体类

  src/main/java

```java
package com.hou.pogo;

public class User {

    private int id;
    private String name;
    private String pwd;

    public User() {
    }

    public User(int id, String name, String pwd) {
        this.id = id;
        this.name = name;
        this.pwd = pwd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pwd='" + pwd + '\'' +
                '}';
    }
}

```



- Dao接口

```java
package com.hou.dao;

import com.hou.pogo.User;

import java.util.List;

public interface UserDao {
    List<User> getUserList();
}
```



- 接口实现类

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!--namespace绑定一个对应的mapper接口-->
<mapper namespace="com.hou.dao.UserDao">

    <!--id方法名-->
    <select id="getUserList" resultType="com.hou.pogo.User">
        select * from mybatis.user
    </select>

</mapper>
```

### 测试

注意点：

org.apache.ibatis.binding.BindingException: Type interface com.hou.dao.UserDao is not known to the MapperRegistry.

### mybatis-config.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">

<configuration>
    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="com.mysql.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://111.230.212.103:3306/mybatis?userSSL=true&amp;
                userUnicode=true&amp;characterEncoding=UTF-8"/>
                <property name="username" value="root"/>
                <property name="password" value="hdk123"/>
            </dataSource>
        </environment>
    </environments>

    <!--每一个mapper.xml都需要注册-->
    <mappers>
        <mapper resource="com/hou/dao/UserMapper.xml"/>
    </mappers>

</configuration>
```

在两个pom.xml中加入

```xml
<!--在build中配置resources，来防止我们资源导出失败的问题-->
    <build>
        <resources>
            <resource>
                <directory>src/main/resources</directory>
                <includes>
                    <include>**/*.properties</include>
                    <include>**/*.xml</include>
                </includes>
            </resource>
            <resource>
                <directory>src/main/java</directory>
                <includes>
                    <include>**/*.properties</include>
                    <include>**/*.xml</include>
                </includes>
                <filtering>true</filtering>
            </resource>
        </resources>
    </build>
```

### 步骤

1. 导入包
2. 配置数据库
3. 建造工具类

#### SqlSessionFactoryBuilder

这个类可以被实例化、使用和丢弃，一旦创建了 SqlSessionFactory，就不再需要它了。 因此 SqlSessionFactoryBuilder 实例的最佳作用域是方法作用域（也就是局部方法变量）。 你可以重用 SqlSessionFactoryBuilder 来创建多个 SqlSessionFactory 实例，但最好还是不要一直保留着它，以保证所有的 XML 解析资源可以被释放给更重要的事情。

#### SqlSessionFactory

SqlSessionFactory 一旦被创建就应该在应用的运行期间一直存在，没有任何理由丢弃它或重新创建另一个实例。 使用 SqlSessionFactory 的最佳实践是在应用运行期间不要重复创建多次，多次重建 SqlSessionFactory 被视为一种代码“坏习惯”。因此 SqlSessionFactory 的最佳作用域是应用作用域。 有很多方法可以做到，最简单的就是使用单例模式或者静态单例模式。

#### SqlSession

每个线程都应该有它自己的 SqlSession 实例。SqlSession 的实例不是线程安全的，因此是不能被共享的，所以它的最佳的作用域是请求或方法作用域。 绝对不能将 SqlSession 实例的引用放在一个类的静态域，甚至一个类的实例变量也不行。 也绝不能将 SqlSession 实例的引用放在任何类型的托管作用域中，比如 Servlet 框架中的 HttpSession。 如果你现在正在使用一种 Web 框架，考虑将 SqlSession 放在一个和 HTTP 请求相似的作用域中。 换句话说，每次收到 HTTP 请求，就可以打开一个 SqlSession，返回一个响应后，就关闭它。 这个关闭操作很重要，为了确保每次都能执行关闭操作，你应该把这个关闭操作放到 finally 块中。 下面的示例就是一个确保 SqlSession 关闭的标准模式



# 2. 增删改查

### 1. namespace

namespace中的包名要和接口一致

### 2. select

- id：就是对应的namespace的方法名
- resultType：sql语句的返回值！
- parameterType： 参数类型！

1. 编写接口
2. 编写对应的mapper中的对应语句
3. 测试

#### UserMapper

```java
package com.hou.dao;

import com.hou.pogo.User;

import java.util.List;

public interface UserMapper {
    //查询全部用户
    List<User> getUserList();

    //根据id查询用户
    User getUserById(int id);

    //插入用户
    void addUser(User user);

    //修改用户
    int updateUser(User user);

    //删除用户
    int deleteUser(int id);
}

```

#### UserMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!--namespace绑定一个对应的mapper接口-->
<mapper namespace="com.hou.dao.UserMapper">

    <!--id方法名-->
    <select id="getUserList" resultType="com.hou.pogo.User">
        select * from mybatis.user
    </select>

    <select id="getUserById" resultType="com.hou.pogo.User"
    parameterType="int">
        select * from mybatis.user where id = #{id}
    </select>

    <!--对象中的属性可以直接取出来-->
    <insert id="addUser" parameterType="com.hou.pogo.User">
        insert into mybatis.user (id, name, pwd) values (#{id}, #{name}, #{pwd});
    </insert>

    <update id="updateUser" parameterType="com.hou.pogo.User">
        update mybatis.user set name=#{name}, pwd=#{pwd} where id =#{id};
    </update>

    <delete id="deleteUser" parameterType="int">
        delete from mybatis.user where id=#{id};
    </delete>

</mapper>
```

#### Test

```java
package com.hou.dao;

import com.hou.pogo.User;
import com.hou.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

public class UserDaoTest {

    @Test
    public void test(){
        // 获得sqlsession对象
        SqlSession sqlSession = MybatisUtils.getSqlSession();

        try{
            // 1.执行 getmapper
            UserMapper userDao = sqlSession.getMapper(UserMapper.class);
            List<User> userList = userDao.getUserList();

            // method 2
//        List<User> userList = sqlSession.selectList("com.hou.dao.UserDao.getUserList");

            for(User user: userList){
                System.out.println(user);
            }

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            //关闭
            sqlSession.close();
        }

    }

    @Test
    public void getUserById(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();

        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = mapper.getUserById(1);
        System.out.println(user);
        sqlSession.close();
    }

    //增删改需要提交事务
    @Test
    public void addUser(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();

        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        mapper.addUser(new User(5,"hou","123456"));

        //提交事务
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void updateUser(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();

        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        mapper.updateUser(new User(4,"hou","123"));

        //提交事务
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void deleteUser(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();

        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        mapper.deleteUser(5);

        //提交事务
        sqlSession.commit();
        sqlSession.close();
    }
}

```

注意点：增删改需要提交事务。

### 3. Map

假如我们的实体类属性过多，用map，传递map的key

```xml
<insert id="addUser2" parameterType="map">
    insert into mybatis.user (id, name, pwd) values (#{id1}, #{name1}, #{pwd1});
</insert>
```

```java
int addUser2(Map<String, Object> map);
```

```java
@Test
public void addUser2(){
    SqlSession sqlSession = MybatisUtils.getSqlSession();

    UserMapper mapper = sqlSession.getMapper(UserMapper.class);
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("id1",5);
    map.put("name1","dong");
    map.put("pwd1","12345");
    mapper.addUser2(map);

    //提交事务
    sqlSession.commit();
    sqlSession.close();
}
```



### 4.模糊查询

java代码执行的时候，传递通配符%

```xml
<select id="getUserLike" resultType="com.hou.pogo.User">
    select * from mybatis.user where name like #{value}
</select>
```

```java
@Test
public void getUserLike(){
    SqlSession sqlSession = MybatisUtils.getSqlSession();

    UserMapper mapper = sqlSession.getMapper(UserMapper.class);
    List<User> list = mapper.getUserLike("%o%");

    for(User user : list){
        System.out.println(user);
    }

    sqlSession.close();
}
```



# 3. 配置解析

### 1. 核心配置文件

- mybatis-config.xml

```xml
configuration（配置）
    properties（属性）
    settings（设置）
    typeAliases（类型别名）
    typeHandlers（类型处理器）
    objectFactory（对象工厂）
    plugins（插件）
        environments（环境配置）
            environment（环境变量）
            transactionManager（事务管理器）
    dataSource（数据源）
    databaseIdProvider（数据库厂商标识）
    mappers（映射器）
```

### 

###2. 环境配置（environments）

 MyBatis 可以配置成适应多种环境 

 **不过要记住：尽管可以配置多个环境，但每个 SqlSessionFactory 实例只能选择一种环境。** 

Mybatis 默认的事务管理器是JDBC，连接池：POOLED



### 3. 属性

我们可以通过properties属性来引用配置文件

这些属性可以在外部进行配置，并可以进行动态替换。你既可以在典型的 Java 属性文件中配置这些属性，也可以在 properties 元素的子元素中设置。 （db.properties）

编写一个配置文件

db.properties

```properties
driver = com.mysql.jdbc.Driver
url = "jdbc:mysql://111.230.212.103:3306/mybatis?userSSL=true&userUnicode=true&characterEncoding=UTF-8"
username = root 
password = hdk123
```

在核心配置文件中引入

mybatis-config.xml (同时有的话，优先走外面properties)

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">

<configuration>

    <!--引入外部配置文件-->
    <!--<properties resource="db.properties"/>-->

    <properties resource="db.properties">
        <property name="username" value="root"></property>
        <property name="password" value="hdk123"></property>
    </properties>

    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="${driver}"/>
                <property name="url" value="${url}"/>
                <property name="username" value="${username}"/>
                <property name="password" value="${password}"/>
            </dataSource>
        </environment>
    </environments>

    <!--每一个mapper.xml都需要注册-->
    <mappers>
        <mapper resource="com/hou/dao/UserMapper.xml"/>
    </mappers>

</configuration>
```



### 4. 类型别名（typeAliases）

 类型别名可为 Java 类型设置一个缩写名字。 

```xml
<typeAliases>
    <typeAlias type="com.hou.pogo.User" alias="User"></typeAlias>
</typeAliases>
```

扫描实体类的包，默认别名就为这个类的类名首字母小写

```xml
<typeAliases>
    <package name="com.hou.pogo"></package>
</typeAliases>
```

在实体类，比较少的时候使用第一种，实体类多使用第二种。

第一种可以自定义，第二则不行，但是 若有注解，则别名为其注解值 。

```java
@Alias("hello")
public class User {
}
```



### 5. 设置

| 设置名             | 描述                                                         | 有效值                                                       | 默认值 |
| :----------------- | :----------------------------------------------------------- | :----------------------------------------------------------- | :----- |
| cacheEnabled       | 全局性地开启或关闭所有映射器配置文件中已配置的任何缓存。     | true \| false                                                | true   |
| lazyLoadingEnabled | 延迟加载的全局开关。当开启时，所有关联对象都会延迟加载。 特定关联关系中可通过设置 `fetchType` 属性来覆盖该项的开关状态。 | true \| false                                                | false  |
| logImpl            | 指定 MyBatis 所用日志的具体实现，未指定时将自动查找。        | SLF4J \| LOG4J \| LOG4J2 \| JDK_LOGGING \| COMMONS_LOGGING \| STDOUT_LOGGING \| NO_LOGGING | 未设置 |



### 6. 其他

- [typeHandlers（类型处理器）](https://mybatis.org/mybatis-3/zh/configuration.html#typeHandlers)
- [objectFactory（对象工厂）](https://mybatis.org/mybatis-3/zh/configuration.html#objectFactory)
- [plugins（插件）](https://mybatis.org/mybatis-3/zh/configuration.html#plugins)
  - mybatis-generator-core
  - mybatis-plus
  - 通用mapper



### 7. 映射器

方式一: [推荐使用]

```xml
<mappers>
    <mapper resource="com/hou/dao/UserMapper.xml"/>
</mappers>
```

方式二：

```xml
<mappers>
    <mapper class="com.hou.dao.UserMapper" />
</mappers>
```

- 接口和它的Mapper必须同名
- 接口和他的Mapper必须在同一包下

方式三：

```xml
<mappers>
    <!--<mapper resource="com/hou/dao/UserMapper.xml"/>-->
    <!--<mapper class="com.hou.dao.UserMapper" />-->
    <package name="com.hou.dao" />
</mappers>
```

- 接口和它的Mapper必须同名
- 接口和他的Mapper必须在同一包下



### 8.生命周期和作用域

作用域和生命周期类别是至关重要的，因为错误的使用会导致非常严重的**并发问题**。

**SqlSessionFactoryBuilder**: 

-  一旦创建了 SqlSessionFactory，就不再需要它了 。
- 局部变量

 **SqlSessionFactory**：

-  就是数据库连接池。
-  一旦被创建就应该在应用的运行期间一直存在 ，**没有任何理由丢弃它或重新创建另一个实例 。** 多次重建 SqlSessionFactory 被视为一种代码“坏习惯”。 
-  因此 SqlSessionFactory 的最佳作用域是应用作用域。 
-  最简单的就是使用单例模式或者静态单例模式。 

 **SqlSession**：

- 每个线程都应该有它自己的 SqlSession 实例。 
- 连接到连接池的请求！
-  SqlSession 的实例不是线程安全的，因此是不能被共享的 ，所以它的最佳的作用域是请求或方法作用域。 
- 用完之后赶紧关闭，否则资源被占用。



# 4. 解决属性名和字段名不一致的问题

数据库中的字段

新建一个项目，拷贝之前，测试实体字段不一致的情况

User

```java
package com.hou.pogo;

public class User {

    private int id;
    private String name;
    private String password;
}
```

问题：

User{id=2, name='wang', password='null'}

解决方法：

核心配置文件

- 起别名

```xml
<select id="getUserById" resultType="User"
    parameterType="int">
        select id,name,pwd as password from mybatis.user where id = #{id}
</select>
```

- resultMap 结果集映射

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!--namespace绑定一个对应的mapper接口-->
<mapper namespace="com.hou.dao.UserMapper">

    <select id="getUserById" resultMap="UserMap" parameterType="int">
        select * from mybatis.user where id = #{id}
    </select>

    <!--结果集映射-->
    <resultMap id="UserMap" type="User">
        <!--colunm 数据库中的字段，property实体中的属性-->
        <result column="id" property="id"></result>
        <result column="name" property="name"></result>
        <result column="pwd" property="password"></result>
    </resultMap>

</mapper>
```

- `resultMap` 元素是 MyBatis 中最重要最强大的元素。 

- ResultMap 的设计思想是，对简单的语句做到零配置，对于复杂一点的语句，只需要描述语句之间的关系就行了。 

```xml
<resultMap id="UserMap" type="User">
    <!--colunm 数据库中的字段，property实体中的属性-->
    <!--<result column="id" property="id"></result>-->
    <!--<result column="name" property="name"></result>-->
    <result column="pwd" property="password"></result>
</resultMap>
```



# 5. 日志

### 1. 日志工厂

如果一个数据库操作出现了异常，我们需要排错。日志就是最好的助手。

曾经：sout，debug

现在：日志工厂

 logImpl 

- SLF4J 
- LOG4J [掌握]
- LOG4J2 
- JDK_LOGGING 
- COMMONS_LOGGING 
- STDOUT_LOGGING [掌握]
- NO_LOGGING 

具体使用哪一个，在设置中设定

STDOUT_LOGGING 标志日志输出

mybatis-confi中

```xml
<settings>
    <setting name="logImpl" value="STDOUT_LOGGING"/>
</settings>
```



### 2. Log4j

1. 先导包

   pom.xml下

   ```xml
   <dependencies>
       <!-- https://mvnrepository.com/artifact/log4j/log4j -->
       <dependency>
           <groupId>log4j</groupId>
           <artifactId>log4j</artifactId>
           <version>1.2.17</version>
       </dependency>
   </dependencies>
   ```

2. 新建log4j.properties文件

```properties
### set log levels ###
log4j.rootLogger = DEBUG,console,file

### 输出到控制台 ###
log4j.appender.console = org.apache.log4j.ConsoleAppender
log4j.appender.console.Target = System.out
log4j.appender.console.Threshold = DEBUG
log4j.appender.console.layout = org.apache.log4j.PatternLayout
log4j.appender.console.layout.ConversionPattern = [%c]-%m%n

### 输出到日志文件 ###
log4j.appender.file=org.apache.log4j.RollingFileAppender
log4j.appender.file.File=./log/hou.log
log4j.appender.file.MaxFileSize=10mb 
log4j.appender.file.Threshold=DEBUG 
log4j.appender.file.layout=org.apache.log4j.PatternLayout
log4j.appender.file.layout.ConversionPattern=[%p][%d{yy-MM-dd}][%c]%m%n

# 日志输出级别
log4j.logger.org.mybatis=DEBUG
log4j.logger.java.sql=DEBUG
log4j.logger.java.sql.Statement=DEBUG
log4j.logger.java.sql.ResultSet=DEBUG
log4j.logger.java.sql.PreparedStatement=DEBUG
```

3. 配置实现

```xml
<settings>
    <setting name="logImpl" value="LOG4J"/>
</settings>
```

4. Log4j使用

```java
package com.hou.dao;

import com.hou.pojo.User;
import com.hou.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.junit.Test;

public class UserDaoTest {

    static Logger logger = Logger.getLogger(UserDaoTest.class);

    @Test
    public void test(){
        // 获得sqlsession对象
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        try{
            // 1.执行 getmapper
            UserMapper userDao = sqlSession.getMapper(UserMapper.class);
            logger.info("测试");
            User user = userDao.getUserById(2);
            System.out.println(user);
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            //关闭
            sqlSession.close();
        }
    }

    @Test
    public void testLog4j(){
        logger.info("info:进入了testlog4j");
        logger.debug("debug:进入了testlog4j");
        logger.error("error:进入了testlog4j");
    }

}
```



# 6. 分页

### 1. Limit 分页

语法：

```sql
SELECT * from user limit startIndex,pageSize;
SELECT * from user limit 0,2;
```

```java
package com.hou.dao;

import com.hou.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserMapper {

    //根据id查询用户
    User getUserById(int id);

    List<User> getUserByLimit(Map<String, Integer> map);

}

```

xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!--namespace绑定一个对应的mapper接口-->
<mapper namespace="com.hou.dao.UserMapper">

    <select id="getUserById" resultMap="UserMap"
    parameterType="int">
        select * from mybatis.user where id = #{id}
    </select>

    <!--结果集映射-->
    <resultMap id="UserMap" type="User">
        <!--colunm 数据库中的字段，property实体中的属性-->
        <!--<result column="id" property="id"></result>-->
        <!--<result column="name" property="name"></result>-->
        <result column="pwd" property="password"></result>
    </resultMap>

    <select id="getUserByLimit" parameterType="map"
            resultType="User" resultMap="UserMap">
      select * from mybatis.user limit #{startIndex},#{pageSize}
    </select>

</mapper>
```

test类

```java
@Test
public void getByLimit(){
    SqlSession sqlSession = MybatisUtils.getSqlSession();
    UserMapper mapper = sqlSession.getMapper(UserMapper.class);
    Map<String, Integer> map = new HashMap<String, Integer>();
    map.put("startIndex", 1);
    map.put("pageSize", 2);
    List<User> userList = mapper.getUserByLimit(map);

    for(User user:userList){
        System.out.println(user);
    }

    sqlSession.close();
}
```



### 2. RowBounds分页

@Test

```java
@Test
public void getUserByRow(){
    SqlSession sqlSession = MybatisUtils.getSqlSession();
    //RowBounds实现
    RowBounds rowBounds = new RowBounds(1, 2);

    //通过java代码层面
    List<User> userList = sqlSession.selectList
        ("com.hou.dao.UserMapper.getUserByRowBounds",
         null,rowBounds);

    for (User user : userList) {
        System.out.println(user);
    }

    sqlSession.close();
}
```

### 3. 分页插件

- pageHelper



# 7. 使用注解开发

1. 删除 UserMapper.xml

2. UserMapper

   ```java
   package com.hou.dao;
   
   import com.hou.pojo.User;
   import org.apache.ibatis.annotations.Select;
   
   import java.util.List;
   
   public interface UserMapper {
   
       @Select("select * from user")
       List<User> getUsers();
   }
   ```

3. 核心配置 mybatis-config.xml

   ```xml
   <?xml version="1.0" encoding="UTF-8" ?>
   <!DOCTYPE configuration
           PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
           "http://mybatis.org/dtd/mybatis-3-config.dtd">
   
   <configuration>
   
       <!--引入外部配置文件-->
       <properties resource="db.properties"/>
   
       <!--可以给实体类起别名-->
       <typeAliases>
           <typeAlias type="com.hou.pojo.User" alias="User"></typeAlias>
       </typeAliases>
   
       <environments default="development">
           <environment id="development">
               <transactionManager type="JDBC"/>
               <dataSource type="POOLED">
                   <property name="driver" value="${driver}"/>
                   <property name="url" value="${url}"/>
                   <property name="username" value="${username}"/>
                   <property name="password" value="${password}"/>
               </dataSource>
           </environment>
       </environments>
   
       <!--绑定接口-->
       <mappers>
           <mapper class="com.hou.dao.UserMapper"></mapper>
       </mappers>
   </configuration>
   ```

   本质：反射机制

   底层：动态代理！

### Mybatis详细执行流程：

1. Resource获取全局配置文件
2. 实例化SqlsessionFactoryBuilder
3. 解析配置文件流XMLCondigBuilder

4. Configration所有的配置信息
5. SqlSessionFactory实例化
6. trasactional事务管理
7. 创建executor执行器
8. 创建SqlSession
9. 实现CRUD
10. 查看是否执行成功
11. 提交事务
12. 关闭

### 注解CRUD

```java
package com.hou.dao;

import com.hou.pojo.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface UserMapper {

    @Select("select * from user")
    List<User> getUsers();

    //方法存在多个参数，所有的参数必须加@Param
    @Select("select * from user where id = #{id}")
    User getUserById(@Param("id") int id);

    @Insert("insert into user (id, name, pwd) values" +
            "(#{id},#{name},#{password})")
    int addUser(User user);

    @Update("update user set name=#{name}, pwd=#{password} " +
            "where id=#{id}")
    int updateUser(User user);

    @Delete("delete from user where id=#{id}")
    int deleteUser(@Param("id") int id);

}
```

MybatisUtile

```java
package com.hou.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

//sqlSessionFactory --> sqlSession
public class MybatisUtils {

    private static SqlSessionFactory sqlSessionFactory;

    static {
        try {
            //使用mybatis第一步：获取sqlSessionFactory对象
            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SqlSession getSqlSession(){
        return sqlSessionFactory.openSession(true);
    }

}
```

Test

```java
package com.hou.dao;

import com.hou.pojo.User;
import com.hou.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

public class UserDaoTest {

    @Test
    public void test(){
        // 获得sqlsession对象
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        try{
            // 1.执行 getmapper
            UserMapper userDao = sqlSession.getMapper(UserMapper.class);
            List<User> userList = userDao.getUsers();
            for (User user : userList) {
                System.out.println(user);
            }

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            //关闭
            sqlSession.close();
        }
    }

    @Test
    public void getuserById(){
        // 获得sqlsession对象
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        try{
            // 1.执行 getmapper
            UserMapper userDao = sqlSession.getMapper(UserMapper.class);
            User user = userDao.getUserById(1);

            System.out.println(user);


        }catch(Exception e){
            e.printStackTrace();
        }finally{
            //关闭
            sqlSession.close();
        }
    }

    @Test
    public void addUser(){
        // 获得sqlsession对象
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        try{
            // 1.执行 getmapper
            UserMapper userDao = sqlSession.getMapper(UserMapper.class);
            userDao.addUser(new User(6, "kun","123"));

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            //关闭
            sqlSession.close();
        }
    }

    @Test
    public void updateUser(){
        // 获得sqlsession对象
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        try{
            // 1.执行 getmapper
            UserMapper userDao = sqlSession.getMapper(UserMapper.class);
            userDao.updateUser(new User(6, "fang","123"));

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            //关闭
            sqlSession.close();
        }
    }

    @Test
    public void deleteUser(){
        // 获得sqlsession对象
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        try{
            // 1.执行 getmapper
            UserMapper userDao = sqlSession.getMapper(UserMapper.class);
            userDao.deleteUser(6);

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            //关闭
            sqlSession.close();
        }
    }
}
```



# 8. Lombok

1. 在IDEA中安装lombok插件

2. 配置

   ```xml
   <dependencies>
       <!-- https://mvnrepository.com/artifact/org.projectlombok/lombok -->
       <dependency>
           <groupId>org.projectlombok</groupId>
           <artifactId>lombok</artifactId>
           <version>1.18.12</version>
       </dependency>
   </dependencies>
   ```

3. ```
   @Getter and @Setter
   @FieldNameConstants
   @ToString
   @EqualsAndHashCode
   @AllArgsConstructor, @RequiredArgsConstructor and @NoArgsConstructor
   @Log, @Log4j, @Log4j2, @Slf4j, @XSlf4j, @CommonsLog, @JBossLog, @Flogger, @CustomLog
   @Data
   @Builder
   @SuperBuilder
   @Singular
   @Delegate
   @Value
   @Accessors
   @Wither
   @With
   @SneakyThrows
   ```

   @Data: 无参构造，get，set，toString，hashCode

   在实体类上加注解

   ```java
   package com.hou.pojo;
   
   import lombok.AllArgsConstructor;
   import lombok.Data;
   import lombok.NoArgsConstructor;
   
   @Data
   @AllArgsConstructor
   @NoArgsConstructor
   public class User {
   
       private int id;
       private String name;
       private String password;
   
   }
   ```

   

# 9. 多对一处理

- 多个学生**关联**一个老师（多对一）

- **集合**（一对多）

### 1. 建表

```sql
CREATE TABLE `teacher` (
	`id` INT(10) NOT NULL PRIMARY KEY,
	`name` VARCHAR(30) DEFAULT NULL
)ENGINE=INNODB DEFAULT CHARSET=utf8

INSERT INTO teacher (`id`, `name`) VALUES (1, 'hou');

CREATE TABLE `student` (
	`id` INT(10) NOT NULL,
	`name` VARCHAR(30) DEFAULT NULL,
	`tid` INT(10) DEFAULT NULL,
	PRIMARY KEY (`id`),
	KEY `fktid` (`tid`),
	CONSTRAINT `fktid` FOREIGN KEY (`tid`) REFERENCES `teacher` (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8

INSERT INTO student (`id`, `name`, `tid`) VALUES (1, 'xiao1', 1);
INSERT INTO student (`id`, `name`, `tid`) VALUES (2, 'xiao2', 1);
INSERT INTO student (`id`, `name`, `tid`) VALUES (3, 'xiao3', 1);
INSERT INTO student (`id`, `name`, `tid`) VALUES (4, 'xiao4', 1);
INSERT INTO student (`id`, `name`, `tid`) VALUES (5, 'xiao5', 1);
```

1. 新建实体类

   ```java
   package com.hou.pojo;
   
   import lombok.Data;
   
   @Data
   public class Student {
       private int id;
       private String name;
   
       //学生需要关联一个老师
       private Teacher teacher;
   }
   ```

   ```java
   package com.hou.pojo;
   
   import lombok.Data;
   
   @Data
   public class Teacher {
       private int id;
       private String name;
   }
   ```

2. 建立Mapper接口

3. 建立Mapper.xml

4. 测试是否能够成功

### 2. 按照查询嵌套处理

StudentMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="com.hou.dao.StudentMapper">

    <select id="getStudent" resultMap="StudentTeacher">
      select * from student;
    </select>

    <resultMap id="StudentTeacher" type="com.hou.pojo.Student">
        <result property="id" column="id"></result>
        <result property="name" column="name"></result>
        <!--对象使用assiociation-->
        <!--集合用collection-->
        <association property="teacher" column="tid"
                     javaType="com.hou.pojo.Teacher"
                     select="getTeacher"></association>
    </resultMap>

    <select id="getTeacher" resultType="com.hou.pojo.Teacher">
      select * from teacher where id = #{id};
    </select>

</mapper>
```



### 3. 按照结果嵌套处理

```sql
select s.id sid,s.name sname,t.name tname
from student s,teacher t where s.tid=t.id;
```

```xml
<select id="getStudent2" resultMap="StudentTeacher2">
    select s.id sid,s.name sname,t.name tname
    from student s,teacher t where s.tid=t.id;
</select>

<resultMap id="StudentTeacher2" type="com.hou.pojo.Student">
    <result property="id" column="sid"></result>
    <result property="name" column="sname"></result>
    <association property="teacher" javaType="com.hou.pojo.Teacher">
        <result property="name" column="tname"></result>
    </association>

</resultMap>
```

**property**	映射到列结果的字段或属性。

**column**	数据库中的列名，或者是列的别名。



# 10. 一对多

一个老师拥有多个学生

对于老师而言就是一对多

### 1.环境搭建

实体类

```java
package com.hou.pojo;

import lombok.Data;
import java.util.List;

@Data
public class Teacher {
    private int id;
    private String name;
    private List<Student> studentList;
}
```

```java
package com.hou.pojo;

import lombok.Data;

@Data
public class Student {
    private int id;
    private String name;
    private int tid;
}
```



### 2. 按照结果查询

```xml
<select id="getTeacher" resultMap="TeacherStudent">
    select s.id sid, s.name sname, t.name tname, t.id tid
    from student s, teacher t
    where s.tid = t.id and t.id = #{id};
</select>

<resultMap id="TeacherStudent" type="com.hou.pojo.Teacher">
    <result property="id" column="tid"></result>
    <result property="name" column="tname"></result>
    <!--集合中的泛型信息，我们用oftype获取-->
    <collection property="studentList" ofType="com.hou.pojo.Student">
        <result property="id" column="sid"></result>
        <result property="name" column="sname"></result>
    </collection>
</resultMap>
```



### 3. 按照查询嵌套处理

```xml
<select id="getTeacher2" resultMap="TeacherStudent2">
    select * from mybatis.teacher where id = #{id}
</select>

<resultMap id="TeacherStudent2" type="com.hou.pojo.Teacher">
    <collection property="studentList" column="id" javaType="ArrayList"
                ofType="com.hou.pojo.Student"
                select="getStudentByTeacherId"></collection>
</resultMap>

<select id="getStudentByTeacherId" resultType="com.hou.pojo.Student">
    select * from mybatis.student where tid = #{id}
</select>
```



### 小结

1. 关联 - association 多对一
2. 集合 - collection 一对多
3. javaType & ofType
   1. JavaType用来指定实体中属性类型
   2. ofType映射到list中的类型，泛型中的约束类型

注意点：

- 保证sql可读性，尽量保证通俗易懂
- 注意字段问题
- 如果问题不好排查错误，使用日志



# 11. 动态sql

动态sql：根据不同的条件生成不同的SQL语句

### 1. 搭建环境

```sql
create table `blog`(
	`id` varchar(50) not null comment '博客id',
    `title` varchar(100) not null comment '博客标题',
    `author` varchar(30) not null comment '博客作者',
    `create_time` datetime not null comment '创建时间',
    `views` int(30) not null comment '浏览量'
	)ENGINE=InnoDB DEFAULT CHARSET=utf8
```

实体类

```java
package com.hou.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class Blog {
    private String id;
    private String title;
    private String author;
    private Date createTime;
    private int views;
}
```

核心配置

```xml
<settings>
    <setting name="mapUnderscoreToCamelCase" value="true"/>
</settings>
```

Mapper.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="com.hou.mapper.BlogMapper">
    <insert id="addBlog" parameterType="Blog">
        insert into mybatis.blog (id, title, author, create_time, views) values
        (#{id}, #{title}, #{author}, #{create_time}, #{views});
    </insert>
</mapper>
```

新建随机生成ID包

```java
package com.hou.utils;

import org.junit.Test;

import java.util.UUID;

@SuppressWarnings("all")
public class IDUtiles {

    public static String getId(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    @Test
    public void  test(){
        System.out.println(getId());
    }

}
```

测试类：添加数据

```java
import com.hou.mapper.BlogMapper;
import com.hou.pojo.Blog;
import com.hou.utils.IDUtiles;
import com.hou.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.Date;

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
}
```

### 2. if

```xml
<select id="queryBlogIF" parameterType="map" resultType="Blog">
    select * from mybatis.blog where 1=1
    <if test="title != null">
        and title = #{title}
    </if>
    <if test="author != author">
        and author = #{author}
    </if>
</select>
```

test

```java
@Test
public void queryBlogIF(){
    SqlSession sqlSession = MybatisUtils.getSqlSession();
    BlogMapper blogMapper = sqlSession.getMapper(BlogMapper.class);
    Map map = new HashMap();

    //        map.put("title", "second");
    map.put("author", "houdongun");

    List<Blog> list = blogMapper.queryBlogIF(map);

    for (Blog blog : list) {
        System.out.println(blog);
    }

    sqlSession.close();
}
```



### 3. choose、when、otherwise

```xml
<select id="queryBlogchoose" parameterType="map" resultType="Blog">
    select * from mybatis.blog
    <where>
        <choose>
            <when test="title != null">
                title = #{title}
            </when>
            <when test="author != null">
                and author = #{author}
            </when>
            <otherwise>
                and views = #{views}
            </otherwise>
        </choose>
    </where>
</select>
```



### 4. trim、where、set

```xml
<update id="updateBlog" parameterType="map">
    update mybatis.blog
    <set>
        <if test="title != null">
            title = #{title},
        </if>
        <if test="author != null">
            author = #{author}
        </if>
    </set>
    where id = #{id}
</update>
```

trim 可以自定义

### SQL片段

有些时候我们有一些公共部分

1. 使用sql便签抽取公共部分

2. 在使用的地方使用include标签

```xml
<sql id="if-title-author">
    <if test="title != null">
        title = #{title}
    </if>
    <if test="author != null">
        and author = #{author}
    </if>
</sql>

<select id="queryBlogIF" parameterType="map" resultType="Blog">
    select * from mybatis.blog
    <where>
        <include refid="if-title-author"></include>
    </where>
</select>
```

注意：

- 最好基于单表
- sql里不要存在where标签



### 5. for-each

```xml
<!--ids是传的，#{id}是遍历的-->
<select id="queryBlogForeach" parameterType="map" resultType="Blog">
    select * from mybatis.blog
    <where>
        <foreach collection="ids" item="id" open="and ("
                 close=")" separator="or">
            id=#{id}
        </foreach>
    </where>
</select>
```

test

```java
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
```



# 12. 缓存（了解）

### 1. 一级缓存

1. 开启日志
2. 测试一个session中查询两次相同记录。

缓存失效：

- 映射语句文件中的所有 insert、update 和 delete 语句会刷新缓存。
- 查询不同的mapper.xml
- 手动清除缓存

一级缓存默认开启，只在一次sqlseesion中有效



### 2. 二级缓存

1. 开启全局缓存

```xml
<setting name="cacheEnabled" value="true"/>
```

2. 在当前mapper.xml中使用二级缓存

```xml
<cache eviction="FIFO"
       flushInterval="60000"
       size="512"
       readOnly="true"/>
```

test

```java
@Test
public void test(){
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
```

只用cache时加序列化

```xml
<cache/>
```

实体类

```java
package com.hou.pojo;

import lombok.Data;
import java.io.Serializable;

@Data
public class User implements Serializable {
    private int id;
    private String name;
    private String pwd;

    public User(int id, String name, String pwd) {
        this.id = id;
        this.name = name;
        this.pwd = pwd;
    }
}
```

小结：

- 只有开启了二级缓存，在Mapper下有效
- 所有数据都会先放在一级缓存
- 只有当回话提交，或者关闭的时候，才会提交到二级缓存



### 3. 自定义缓存-ehcache

```xml
<!-- https://mvnrepository.com/artifact/org.mybatis.caches/mybatis-ehcache -->
<dependency>
    <groupId>org.mybatis.caches</groupId>
    <artifactId>mybatis-ehcache</artifactId>
    <version>1.2.0</version>
</dependency>
```

ehcache.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<ehcache xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:noNamespaceSchemaLocation="http://ehcache.org/ehcache.xsd"
         updateCheck="false">
    <!--
       diskStore：为缓存路径，ehcache分为内存和磁盘两级，此属性定义磁盘的缓存位置。参数解释如下：
       user.home – 用户主目录
       user.dir  – 用户当前工作目录
       java.io.tmpdir – 默认临时文件路径
     -->
    <diskStore path="java.io.tmpdir/Tmp_EhCache"/>
    <!--
       defaultCache：默认缓存策略，当ehcache找不到定义的缓存时，则使用这个缓存策略。只能定义一个。
     -->
    <!--
      name:缓存名称。
      maxElementsInMemory:缓存最大数目
      maxElementsOnDisk：硬盘最大缓存个数。
      eternal:对象是否永久有效，一但设置了，timeout将不起作用。
      overflowToDisk:是否保存到磁盘，当系统当机时
      timeToIdleSeconds:设置对象在失效前的允许闲置时间（单位：秒）。仅当eternal=false对象不是永久有效时使用，可选属性，默认值是0，也就是可闲置时间无穷大。
      timeToLiveSeconds:设置对象在失效前允许存活时间（单位：秒）。最大时间介于创建时间和失效时间之间。仅当eternal=false对象不是永久有效时使用，默认是0.，也就是对象存活时间无穷大。
      diskPersistent：是否缓存虚拟机重启期数据 Whether the disk store persists between restarts of the Virtual Machine. The default value is false.
      diskSpoolBufferSizeMB：这个参数设置DiskStore（磁盘缓存）的缓存区大小。默认是30MB。每个Cache都应该有自己的一个缓冲区。
      diskExpiryThreadIntervalSeconds：磁盘失效线程运行时间间隔，默认是120秒。
      memoryStoreEvictionPolicy：当达到maxElementsInMemory限制时，Ehcache将会根据指定的策略去清理内存。默认策略是LRU（最近最少使用）。你可以设置为FIFO（先进先出）或是LFU（较少使用）。
      clearOnFlush：内存数量最大时是否清除。
      memoryStoreEvictionPolicy:可选策略有：LRU（最近最少使用，默认策略）、FIFO（先进先出）、LFU（最少访问次数）。
      FIFO，first in first out，这个是大家最熟的，先进先出。
      LFU， Less Frequently Used，就是上面例子中使用的策略，直白一点就是讲一直以来最少被使用的。如上面所讲，缓存的元素有一个hit属性，hit值最小的将会被清出缓存。
      LRU，Least Recently Used，最近最少使用的，缓存的元素有一个时间戳，当缓存容量满了，而又需要腾出地方来缓存新的元素的时候，那么现有缓存元素中时间戳离当前时间最远的元素将被清出缓存。
   -->
    <defaultCache
            eternal="false"
            maxElementsInMemory="10000"
            overflowToDisk="false"
            diskPersistent="false"
            timeToIdleSeconds="1800"
            timeToLiveSeconds="259200"
            memoryStoreEvictionPolicy="LRU"/>

    <cache
            name="cloud_user"
            eternal="false"
            maxElementsInMemory="5000"
            overflowToDisk="false"
            diskPersistent="false"
            timeToIdleSeconds="1800"
            timeToLiveSeconds="1800"
            memoryStoreEvictionPolicy="LRU"/>

</ehcache>
```



