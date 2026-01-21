package com.mlcdev.employeeapi.config;

import com.mlcdev.employeeapi.exception.DBConnectionException;
import com.mlcdev.employeeapi.exception.DatabaseException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConfig {

    private static HikariDataSource dataSource;

    private DatabaseConfig() {
    }

    static {
        try {
            Properties properties = new Properties();
            InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties");

            if (input == null) {
                input = DatabaseConfig.class.getClassLoader().getResourceAsStream("application.properties");
            }

            if (input == null) {
                throw new DatabaseException("'application.properties' file not found in the classpath.");
            }

            properties.load(input);


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

        } catch (Exception e) {
            e.printStackTrace();
            throw new DBConnectionException("Error loading database configuration: " + e.getMessage());
        }
    }

    public static Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new DBConnectionException("Error connecting to the database");
        }
    }

    public static void closePool() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}