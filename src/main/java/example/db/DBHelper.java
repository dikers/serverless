package example.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @author dikers
 * @date 2019-03-29
 * 数据库连接管理类，单例（静态内部类模式）
 */
public class DBHelper {

    static final Logger logger = LogManager.getLogger(DBHelper.class);

    private Connection connection;


    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    //TODO:  替换数据库地址
    private static final String JDBC_DOMAIN = "dburl:3306/dbname" ;
    //TODO:  替换用户名
    private static final String USER_NAME = "username";
    //TODO:  替换数据库密码
    private static final String PASSWORD = "password";

    private static final String URL = "jdbc:mysql://"+JDBC_DOMAIN+"?useUnicode=true&characterEncoding=utf-8&useSSL=false";


    private static class LazyHolder {
        private static final DBHelper helper = new DBHelper();
    }

    private DBHelper() {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            logger.error("load driver failed.", e);
        }
        try {
            connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
            logger.info("get connection success.");
        } catch (Exception e) {
            logger.error("get connection failed.", e);
        }
    }

    public static final DBHelper getInstance() {
        return LazyHolder.helper;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}