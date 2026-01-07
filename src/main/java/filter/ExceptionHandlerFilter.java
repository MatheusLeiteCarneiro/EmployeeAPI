package filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;

import java.io.IOException;

@WebFilter("/*")
public class ExceptionHandlerFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try{
            doFilter(servletRequest, servletResponse, filterChain);
        }catch (Exception e){
            handleException(servletResponse, e);
        }

    }


    private  void handleException(ServletResponse response, Exception e){

    }
}
