package com.mlcdev.employeeapi.config;

import com.mlcdev.employeeapi.controller.EmployeeController;
import com.mlcdev.employeeapi.exception.DBConnectionException;
import com.mlcdev.employeeapi.exception.DatabaseException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.InputStream;
import java.util.Properties;

public class DatabaseConfig {


    private DatabaseConfig() {
    }

    public static HikariDataSource createDataSource(){
        InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties");
        if (input == null) {
            input = DatabaseConfig.class.getClassLoader().getResourceAsStream("application.properties");
        }
        if (input == null) {
            throw new DatabaseException("'application.properties' file not found in the classpath.");
        }

        try (InputStream loadedInput = input){
            Properties properties = new Properties();
            properties.load(loadedInput);


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

            return new HikariDataSource(config);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DBConnectionException("Error loading database configuration: " + e.getMessage());
        }
    }

}