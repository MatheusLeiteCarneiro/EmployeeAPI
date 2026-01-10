package filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import config.ObjectMapperConfig;
import dto.ErrorDTO;
import exception.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/*")
public class ExceptionHandlerFilter implements Filter {

    private final ObjectMapper objectMapper = ObjectMapperConfig.getMapper();

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
        ErrorDTO error = new ErrorDTO(status, message);
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String json = objectMapper.writeValueAsString(error);
        response.getWriter().write(json);


    }
}
