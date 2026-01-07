package config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import exception.DBConnectionException;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConfig {

    private static HikariDataSource dataSource;

    static {
        try{
            Properties properties = new Properties();
            try (InputStream input = DatabaseConfig.class.getClassLoader().getResourceAsStream("application.properties")) {
                properties.load(input);
            }

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(properties.getProperty("db.url"));
            config.setUsername(properties.getProperty("db.user"));
            config.setPassword(properties.getProperty("db.password"));
            config.setDriverClassName(properties.getProperty("db.driver"));
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(5);
            config.setConnectionTimeout(30000);
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            dataSource = new HikariDataSource(config);

        }catch (Exception e){
            throw new DBConnectionException("Error loading database configuration");
        }
    }


   public static Connection getConnection(){
       try {
           return dataSource.getConnection();
       } catch (SQLException e) {
           throw new DBConnectionException("Error connecting to the database");
       }
   }

   public static void closePool(){
        if(dataSource !=null && !dataSource.isClosed()){
            dataSource.close();
        }
   }

}
