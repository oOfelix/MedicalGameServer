package example.utils;

import example.dao.UserDao;
import example.domain.User;
import org.apache.ibatis.session.SqlSession;

/**
 * 工具类
 * author felix
 */
public class DBUtils {
    public static int signUp(User user) {
        try (SqlSession sqlSession = MybatisUtils.getSqlSession()) {
            UserDao userDao = sqlSession.getMapper(UserDao.class);
            User searchUser = userDao.getUser(user.getUserName());
            System.out.println("search" + searchUser);
            // 0:正确 -3:账号已注册 500:服务器错误
            if (searchUser != null) return -3;
            else {
                System.out.println(user);
                int a = userDao.addUser(user.getUserName(), user.getPassword());
                int b = userDao.addProgress(user.getUserName());
                return a - b;
            }
        }
    }


    public static int signIn(User user) {
        try (SqlSession sqlSession = MybatisUtils.getSqlSession()) {
            UserDao userDao = sqlSession.getMapper(UserDao.class);
            User searchUser = userDao.getUser(user.getUserName());
            System.out.println("search" + searchUser);
            // 0:正确 -1:密码错误 -2:用户不存在
            return searchUser == null ? -2 : searchUser.getPassword().equals(user.getPassword()) ? 0 : -1;
        }
    }

    public static int savePoints(int points, String userName) {
        try (SqlSession sqlSession = MybatisUtils.getSqlSession()) {
            UserDao userDao = sqlSession.getMapper(UserDao.class);
            // 0:正确  500:服务器错误
            return userDao.savePoints(points, userName) == 1 ? 0 : 500;
        }
    }

    public static int getPoint(String userName) {
        try (SqlSession sqlSession = MybatisUtils.getSqlSession()) {
            UserDao userDao = sqlSession.getMapper(UserDao.class);
            // 0:正确 500:服务器错误
            return userDao.getPoints(userName);
        }
    }

    public static int savaStatus(String status, String userName) {
        try (SqlSession sqlSession = MybatisUtils.getSqlSession()) {
            UserDao userDao = sqlSession.getMapper(UserDao.class);
            // 0:正确 500:服务器错误
            return userDao.saveStatus(status, userName) == 1 ? 0 : 500;
        }
    }

    public static String getStatus(String userName) {
        try (SqlSession sqlSession = MybatisUtils.getSqlSession()) {
            UserDao userDao = sqlSession.getMapper(UserDao.class);
            // 0:正确 500:服务器错误
            return userDao.getStatus(userName);
        }
    }
}
