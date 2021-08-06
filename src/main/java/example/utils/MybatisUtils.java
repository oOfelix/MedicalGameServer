package example.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author felix
 */
public class MybatisUtils {
    private static SqlSessionFactory factory;
    static {
        String config = "mybatisConfig.xml";
        try {
            InputStream in = Resources.getResourceAsStream(config);
            SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
            factory = sqlSessionFactoryBuilder.build(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SqlSession getSqlSession(){
        return factory.openSession(true);
    }
}
