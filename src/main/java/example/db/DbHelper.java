package example.db;

import example.constant.AppConstant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;


/**
 * @author dikers
 * @date 2019-03-29
 * 数据库连接管理类，单例（静态内部类模式）
 */
public class DbHelper {

    static final Logger logger = LogManager.getLogger(DbHelper.class);



    private Connection connection;


    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";


    private static final String JDBC_DOMAIN = System.getenv(AppConstant.DB_JDBC_URL);

    private static final String USERNAME = System.getenv( AppConstant.DB_USERNAME );

    private static final String PASSWORD = System.getenv( AppConstant.DB_PASSWORD );

    private static final String DB_NAME = System.getenv( AppConstant.DB_NAME );

    private static final String JDBC_URL = "jdbc:mysql://"+JDBC_DOMAIN+"/"+DB_NAME+"?useUnicode=true&characterEncoding=utf-8&useSSL=false";


    private static class LazyHolder {



        private static final DbHelper HELPER_INSTANCE = new DbHelper();
    }

    private DbHelper() {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            logger.error("load driver failed.", e);
        }
        try {
            connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            logger.info("get connection success.");
        } catch (Exception e) {
            logger.error("get connection failed.", e);
        }
    }

    public static final DbHelper getInstance() {
        return LazyHolder.HELPER_INSTANCE;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}