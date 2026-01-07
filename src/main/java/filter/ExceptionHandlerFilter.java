package filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dto.ErrorDTO;
import exception.BusinessRuleException;
import exception.DBConnectionException;
import exception.DatabaseException;
import exception.NotFoundException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/*")
public class ExceptionHandlerFilter implements Filter {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

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
        if (e instanceof NumberFormatException || e instanceof BusinessRuleException) {
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
