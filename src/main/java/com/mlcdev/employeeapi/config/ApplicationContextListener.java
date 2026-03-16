package com.mlcdev.employeeapi.config;


import com.mlcdev.employeeapi.repository.EmployeeDAO;
import com.mlcdev.employeeapi.service.EmployeeService;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class ApplicationContextListener implements ServletContextListener {

    private HikariDataSource dataSource;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        dataSource = DatabaseConfig.createDataSource();
        EmployeeDAO employeeDao = new EmployeeDAO(dataSource);
        EmployeeService employeeService = new EmployeeService(employeeDao);
        ServletContext context = sce.getServletContext();
        context.setAttribute("EmployeeService", employeeService);
        context.setAttribute("ObjectMapper", ObjectMapperConfig.getMapper());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if(this.dataSource != null && !this.dataSource.isClosed()){
            this.dataSource.close();
        }
    }
}
