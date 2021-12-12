package mybatis.mybatis.test;


import com.atguigu.mybatis.bean.Employee;
import com.atguigu.mybatis.dao.EmployeeMapper;
import com.atguigu.mybatis.dao.EmployeeMapperAnnotation;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class MybatisTest {

    public SqlSessionFactory getSqlSessionFactory() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory =
                new SqlSessionFactoryBuilder().build(inputStream);
        return sqlSessionFactory;
    }

    /**
     * 1、根据xml配置文件创建一个sqlSessionFactory对象
     * 有数据源一些运行环境信息
     * 2、sql映射文件：配置了每一个sql，以及sql的封装规则；
     * 3、将sql映射文件注册在全局配置文件中
     * 4、写代码：
     *     1）根据全局配置文件得到SqlSessionFactory；
     *     2）使用SqlSessionFactory获取到SqlSession对象，使用它来执行增删改查，
     *         一个SqlSession就是代表和数据库的一次会话，用完关闭
     *     3）使用sql的唯一标志来告诉mybatis执行哪个sql,sql都是保存在映射文件中的；
     */
    @Test
    public void test() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory =
                new SqlSessionFactoryBuilder().build(inputStream);
        //获取sqlSession实例，能直接执行已经映射到的sql语句
        //selectOne两个参数：
        //1）sql的唯一标识；
        //2）执行sql要使用到的参数
        SqlSession openSession = sqlSessionFactory.openSession();
        try {
            Employee employee = openSession.selectOne("com.atguigu.mybatis.bean.EmployeeMapper.selectEmp", 1);
            System.out.println(employee);
        } finally {
            openSession.close();
        }
    }


    /**
     * 接口式编程
     * 1、SqlSession代表和数据库的一次会话，用完必须关闭
     * 2、SqlSession和Connection一样都是非线程安全的，每次使用都应该去获取新的对象
     * 3、mapper接口虽然没有实现类，但是mybati会为这个接口生成一个代理对象
     *    （将接口和xml进行绑定）
     *    EmployeeMapper employeeMapper = openSession.getMapper(EmployeeMapper.class);
     * 4、两个重要的配置文件：
     *    mybatis的全局配置文件：包含了mybatis的全局配置，如数据库连接池信息，事务管理器信息等，系统运行环境信息
     *    sql映射文件（XXXmapper.xml）:保存了每一个sql语句的映射信息，mybatis按照此配置文件将sql语句中抽取出来的
     */
    @Test
    public void testMapper() throws IOException {
        //1、获取SqlSessionFactory对象
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        //2、获取sqlSession对象
        SqlSession openSession = sqlSessionFactory.openSession();
        try {
            //3、获取接口的实习类对象
            //会为接口自动的创建一个代理对象，代理对象去执行增删改查方法
            EmployeeMapper employeeMapper = openSession.getMapper(EmployeeMapper.class);
            Employee employee = employeeMapper.getEmpById(1);
            System.out.println(employeeMapper.getClass());
            System.out.println(employee);
        } finally {
            openSession.close();
        }
    }

    /**
     * 测试直接在注册接口Mapper中使用注解的方式写Sql语句
     */
    @Test
    public void testMapperAnnotation() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();
        try {
            EmployeeMapperAnnotation mapperAnnotation = openSession.getMapper(EmployeeMapperAnnotation.class);
            Employee employee = mapperAnnotation.getEmpById(1);
            System.out.println(mapperAnnotation.getClass());
            System.out.println(employee);
        } finally {
            openSession.close();
        }
    }

    /**
     * 测试Mapper中的增删改查
     * 1、 mybatis允许增删改直接定义以下类型返回值：
     *     Integer, Long, Boolean、void
     * 2、需要我们手动提交数据
     *     SqlSession openSession = sqlSessionFactory.openSession();   ==>手动提交
     *     SqlSession openSession = sqlSessionFactory.openSession(true);===>自动提交
     */
    @Test
    public void testCRUD() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();
        try{
            EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
            //测试add
            Employee employee = new Employee(null, "jerry", "1", "jerry@atguigu");
            Long count = mapper.addEmp(employee);
            System.out.println(count);

            //测试update
//            Employee employee = new Employee(1, "jerry", "1", "jerry@atguigu");
//            mapper.updateEmp(employee);

            //测试delete
//            mapper.deleteEmp(2);
            openSession.commit();
        } finally {
            openSession.close();
        }
    }

    /**
     *  mybatis多个参数
     */
    @Test
    public void testParameters() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();
        try{
            EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
            Employee employee = mapper.getEmpByIdAndLastName(1, "jerry");
            Map<String, Object> map = new HashMap<>();
            map.put("id", 1);
            map.put("lastName", "jerry");
            map.put("tableName", "tbl_employee");
            Employee employee2 = mapper.getEmpByMap(map);
            System.out.println(employee2);
        } finally {
            openSession.close();
        }

    }

}
