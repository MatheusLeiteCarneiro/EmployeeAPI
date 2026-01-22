package com.mlcdev.employeeapi.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebFilter("/*")
public class RequestLoggingFilter implements Filter{
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestLoggingFilter.class);


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        String method = httpRequest.getMethod();
        String path = httpRequest.getRequestURI();
        String queryString = httpRequest.getQueryString();
        if (queryString != null){
            LOGGER.info("{} {}?{} INCOMING",method, path,queryString);
        }
        else {
            LOGGER.info("{} {} INCOMING",method, path);
        }
        long startTime = System.currentTimeMillis();
        filterChain.doFilter(servletRequest,servletResponse);
        long duration = System.currentTimeMillis() - startTime;
        int statusCode = ((HttpServletResponse) servletResponse).getStatus();
        LOGGER.info("{} {} -> {} ({}ms)", method, path, statusCode, duration);

    }
}
