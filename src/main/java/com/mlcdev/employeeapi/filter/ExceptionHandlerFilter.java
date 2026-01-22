package com.mlcdev.employeeapi.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mlcdev.employeeapi.config.ObjectMapperConfig;
import com.mlcdev.employeeapi.dto.ErrorDTO;
import com.mlcdev.employeeapi.exception.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebFilter("/*")
public class ExceptionHandlerFilter implements Filter {

    private final ObjectMapper objectMapper = ObjectMapperConfig.getMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlerFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception e) {
            handleException((HttpServletResponse) servletResponse, e);
        }

    }


    private void handleException(HttpServletResponse response, Exception e) throws IOException {
        String message = "Internal Server Error";
        int status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        if (e instanceof InvalidParamException || e instanceof BusinessRuleException) {
            status = HttpServletResponse.SC_BAD_REQUEST;
            message = e.getMessage();
        }
        if (e instanceof NotFoundException) {
            status = HttpServletResponse.SC_NOT_FOUND;
            message = e.getMessage();
        }
        if (e instanceof DatabaseException || e instanceof DBConnectionException) {
            message = e.getMessage();
        } else {
            e.printStackTrace();
        }
        LOGGER.error("{} -> {}", e.getMessage(),status);
        ErrorDTO error = new ErrorDTO(status, message);
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String json = objectMapper.writeValueAsString(error);
        response.getWriter().write(json);


    }
}
