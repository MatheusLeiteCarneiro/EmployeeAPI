package com.mlcdev.employeeapi.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mlcdev.employeeapi.config.ObjectMapperConfig;
import com.mlcdev.employeeapi.dto.EmployeeDTO;
import com.mlcdev.employeeapi.exception.InvalidParamException;
import com.mlcdev.employeeapi.model.Role;
import com.mlcdev.employeeapi.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeControllerTest {

    @InjectMocks
    private EmployeeController controller;

    @Mock
    private EmployeeService service;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private StringWriter responseWriter;

    @BeforeEach
    void setUp() throws Exception{
        responseWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(responseWriter);
        lenient().when(response.getWriter()).thenReturn(printWriter);
    }
    private EmployeeDTO getBaseDTO(){
        return new EmployeeDTO(1L, "name", new BigDecimal("1.00"), LocalDate.of(2000, 1, 1), Role.INTERN.name());
    }

    @Nested
    class HappyPath {

        @Test
        void doGetShouldReturnEmployeeJsonWhenActionIsFindById() throws Exception {
            EmployeeDTO dto = getBaseDTO();
            ObjectMapper mapper = ObjectMapperConfig.getMapper();
            String expectedJson = mapper.writeValueAsString(dto);
            when(request.getPathInfo()).thenReturn("/1");
            when(service.findById(1L)).thenReturn(dto);
            controller.doGet(request, response);
            verify(response).setStatus(HttpServletResponse.SC_OK);
            Assertions.assertEquals(expectedJson, responseWriter.toString());
        }

        @Test
        void doGetShouldReturnAListOfEmployeeJsonWhenActionIsFindAll() throws Exception{
            EmployeeDTO dto = getBaseDTO();
            EmployeeDTO dto2 = getBaseDTO();
            dto2.setId(2L);
            ObjectMapper mapper = ObjectMapperConfig.getMapper();
            List<EmployeeDTO> expectedList = List.of(dto, dto2);
            String expectedJson = mapper.writeValueAsString(expectedList);
            when(request.getPathInfo()).thenReturn(null);
            when(service.findAll(EmployeeController.getDefaultPage(),EmployeeController.getDefaultSize())).thenReturn(expectedList);
            controller.doGet(request, response);
            verify(response).setStatus(HttpServletResponse.SC_OK);
            Assertions.assertEquals(expectedJson, responseWriter.toString());
        }

        @Test
        void doPostShouldReturnAEmployeeJson() throws Exception{
            EmployeeDTO dto = getBaseDTO();
            ObjectMapper mapper = ObjectMapperConfig.getMapper();
            String expectedJson = mapper.writeValueAsString(dto);
            String jsonInput = expectedJson;
            BufferedReader reader = new BufferedReader(new StringReader(jsonInput));
            when(request.getReader()).thenReturn(reader);
            when(service.add(dto)).thenReturn(dto);
            controller.doPost(request, response);
            verify(response).setStatus(HttpServletResponse.SC_CREATED);
            Assertions.assertEquals(expectedJson, responseWriter.toString());
        }

        @Test
        void doPutShouldReturnTheUpdatedEmployeeJson() throws Exception{
            EmployeeDTO dto = getBaseDTO();
            dto.setName("newName");
            ObjectMapper mapper = ObjectMapperConfig.getMapper();
            String expectedJson = mapper.writeValueAsString(dto);
            String inputJson = expectedJson;
            BufferedReader reader = new BufferedReader(new StringReader(inputJson));
            when(request.getReader()).thenReturn(reader);
            when(request.getPathInfo()).thenReturn("/1");
            when(service.update(any(EmployeeDTO.class))).thenReturn(dto);
            controller.doPut(request,response);
            verify(response).setStatus(HttpServletResponse.SC_OK);
            Assertions.assertEquals(responseWriter.toString(), expectedJson);

        }

        @Test
        void doDeleteShouldReturnEmptyJson(){
            when(request.getPathInfo()).thenReturn("/1");
            controller.doDelete(request, response);
            verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
            Assertions.assertEquals("", responseWriter.toString());
        }

    }

    @Nested
    class InvalidParams{
        @Test
        void doGetShouldThrowInvalidParamExceptionWhenTheIdIsNotALong(){
            when(request.getPathInfo()).thenReturn("/string");
            Assertions.assertThrows(InvalidParamException.class, () -> {
                controller.doGet(request, response);
            });
        }

        @Test
        void doGetShouldThrowInvalidParamExceptionWhenPageIsNotAInteger(){
            when(request.getPathInfo()).thenReturn(null);
            when(request.getParameter("size")).thenReturn("1");
            when(request.getParameter("page")).thenReturn("page");
            Assertions.assertThrows(InvalidParamException.class, () -> {
                controller.doGet(request, response);
            });
        }

        @Test
        void doGetShouldThrowInvalidParamExceptionWhenSizeIsNotAInteger(){
            when(request.getPathInfo()).thenReturn(null);
            when(request.getParameter("page")).thenReturn("1");
            when(request.getParameter("size")).thenReturn("size");
            Assertions.assertThrows(InvalidParamException.class, () -> {
                controller.doGet(request, response);
            });
        }

        @Test
        void doDeleteShouldThrowInvalidParamExceptionWhenTheIdIsNotALong(){
            when(request.getPathInfo()).thenReturn("/string");
            Assertions.assertThrows(InvalidParamException.class, () -> {
                controller.doDelete(request, response);
            });
        }
    }

    @Nested
    class DefaultPagination{
        @Test
        void doGetShouldUseTheDefaultSizeAndPageWhenParameterNotSpecified() throws Exception {
            when(request.getPathInfo()).thenReturn(null);
            when(request.getParameter("page")).thenReturn(null);
            when(request.getParameter("size")).thenReturn(null);
            controller.doGet(request, response);
            Mockito.verify(service).findAll(EmployeeController.getDefaultPage(), EmployeeController.getDefaultSize());
        }
    }

}
