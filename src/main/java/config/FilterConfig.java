package config;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;

import java.io.IOException;


@WebFilter("/*")
public class FilterConfig implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        servletResponse.setContentType("application/json");
        servletResponse.setCharacterEncoding("UTF-8");

        filterChain.doFilter(servletRequest,servletResponse);
    }
}
