package config;

import exception.DBConnectionException;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DatabaseConfig {

    private static final Properties properties = new Properties();

    static {
        loadProperties();
    }

    private static void loadProperties(){
        try(InputStream input = DatabaseConfig.class.getClassLoader().getResourceAsStream("application.properties")){
            if(input == null){
                throw new DBConnectionException("Properties not found");
            }
            properties.load(input);
            Class.forName(properties.getProperty("db.driver"));
        }catch (Exception e){
            throw new DBConnectionException("Error loading database configuration" , e);
        }
    }

   public static Connection getConnection(){
       Connection connection = null;
       try {
           connection = DriverManager.getConnection(properties.getProperty("db.url"),
                   properties.getProperty("db.user"),
                   properties.getProperty("db.password"));
       } catch (Exception e) {
           throw new DBConnectionException("An error occurred while connecting to the database", e);
       }
           return connection;
   }

}
