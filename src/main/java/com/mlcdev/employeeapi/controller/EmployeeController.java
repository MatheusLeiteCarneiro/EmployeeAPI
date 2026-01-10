package com.mlcdev.employeeapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mlcdev.employeeapi.config.ObjectMapperConfig;
import com.mlcdev.employeeapi.dto.EmployeeDTO;
import com.mlcdev.employeeapi.exception.InvalidParamException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.mlcdev.employeeapi.repository.EmployeeDAO;
import com.mlcdev.employeeapi.service.EmployeeService;

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
        this.objectMapper = ObjectMapperConfig.getMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String json;
        Long id = getIdFromPath(req);
        if (id == null) {
            List<EmployeeDTO> employeePage;
            String pageParam = req.getParameter("page");
            String sizeParam = req.getParameter("size");
            int page = parseIntegerParam("page", pageParam, DEFAULT_PAGE);
            int size = parseIntegerParam("size", sizeParam, DEFAULT_SIZE);
            employeePage = service.findAll(page, size);
            json = objectMapper.writeValueAsString(employeePage);
        } else {
            EmployeeDTO employee = service.findById(id);
            json = objectMapper.writeValueAsString(employee);
        }
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(json);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        EmployeeDTO dtoReceived = objectMapper.readValue(req.getReader(), EmployeeDTO.class);
        EmployeeDTO dtoSaved = service.add(dtoReceived);
        String json = objectMapper.writeValueAsString(dtoSaved);
        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.getWriter().write(json);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = getIdFromPath(req);
        EmployeeDTO receivedDto = objectMapper.readValue(req.getReader(), EmployeeDTO.class);
        receivedDto.setId(id);
        EmployeeDTO updatedDto = service.update(receivedDto);
        String json = objectMapper.writeValueAsString(updatedDto);
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(json);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        Long id = getIdFromPath(req);
        service.delete(id);
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    private Long getIdFromPath(HttpServletRequest req) {
        String pathInfo = req.getPathInfo();
        Long id = null;
        try {
            if (pathInfo != null && !pathInfo.equals("/")) {
                id = Long.parseLong(pathInfo.substring(1));
            }
        } catch (NumberFormatException e) {
            throw new InvalidParamException("The 'id' parameter must be a numeric value.");
        }
        return id;

    }

    private int parseIntegerParam(String paramName, String paramValue, int defaultValue) {
        if (paramValue == null || paramValue.isEmpty()) {
            return defaultValue;
        }

        try {
            return Integer.parseInt(paramValue);
        } catch (NumberFormatException e) {
            throw new InvalidParamException("The '" + paramName + "' field must be a numeric value");
        }
    }
}
