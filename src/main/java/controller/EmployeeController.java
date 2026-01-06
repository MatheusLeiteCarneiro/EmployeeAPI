package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import controller.dto.EmployeeDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import repository.EmployeeDAO;
import service.EmployeeService;

import java.io.IOException;
import java.util.List;

@WebServlet("/employee/*")
public class EmployeeController extends HttpServlet {
    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_SIZE = 10;
    private final EmployeeService service;
    private final ObjectMapper objectMapper;

    public EmployeeController() {
        this.service = new EmployeeService(new EmployeeDAO());
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String json = "";
        String pathInfo = req.getPathInfo();

        try{
            if(pathInfo == null || pathInfo.equals("/")){
                List<EmployeeDTO> employeePage;
                String pageParam = req.getParameter("page");
                String sizeParam = req.getParameter("size");
                int page = DEFAULT_PAGE;
                int size = DEFAULT_SIZE;
                if(pageParam != null && !pageParam.isEmpty()){
                    page = Integer.parseInt(pageParam);
                }
                if(sizeParam != null && !sizeParam.isEmpty()){
                    size = Integer.parseInt(sizeParam);
                }
                employeePage = service.findAll(page, size);
                json = objectMapper.writeValueAsString(employeePage);
            }
            else{
                Long id = Long.parseLong(pathInfo.substring(1));
                EmployeeDTO employee = service.findById(id);
                json = objectMapper.writeValueAsString(employee);
            }
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(json);
        }catch (NumberFormatException e){
            throw e;
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }
}
