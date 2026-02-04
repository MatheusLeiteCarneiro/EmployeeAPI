package com.mlcdev.employeeapi.service;

import com.mlcdev.employeeapi.dto.EmployeeDTO;
import com.mlcdev.employeeapi.exception.BusinessRuleException;
import com.mlcdev.employeeapi.exception.NotFoundException;
import com.mlcdev.employeeapi.model.Employee;
import com.mlcdev.employeeapi.model.Role;
import com.mlcdev.employeeapi.repository.EmployeeDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EmployeeService {
    private final EmployeeDAO dao;
    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeService.class);

    public EmployeeService(EmployeeDAO dao) {
        this.dao = dao;
    }

    public EmployeeDTO findById(Long id) {
        Optional<Employee> optionalEmployee = dao.findById(id);
        Employee employee= verifyOptional(optionalEmployee);
        LOGGER.debug("Employee with ID: {}, successfully found.", id);
        return new EmployeeDTO(employee);
    }

    public List<EmployeeDTO> findAll(int page, int size) {
        if (size <= 0) {
            throw new BusinessRuleException("The 'size' must be greater than 0");
        }
        if (page <= 0) {
            throw new BusinessRuleException("The 'page' must be greater than 0");
        }
        int limit = size;
        int offset = (page - 1) * size;
        List<EmployeeDTO> dtoList = dao.findAll(limit, offset).stream().map(x -> new EmployeeDTO(x)).collect(Collectors.toList());
        LOGGER.debug("Got a list with {} DTOs.", dtoList.size());
        return dtoList;
    }

    public EmployeeDTO add(EmployeeDTO dto) {
        Employee employee = new Employee();
        dtoToEntity(dto, employee);
        employee = dao.save(employee);
        dto = new EmployeeDTO(employee);
        LOGGER.info("Employee saved with ID: {}.", dto.getId());
        return dto;
    }

    public EmployeeDTO update(EmployeeDTO dto) {
        Long id = dto.getId();
        validId(id);
        Employee employee = new Employee();
        dtoToEntity(dto, employee);
        employee.setId(id);
        Employee finalEmployee = verifyOptional(dao.update(employee));
        LOGGER.info("Employee with ID: {} successfully updated!",finalEmployee.getId());
        return new EmployeeDTO(finalEmployee);
    }

    public void delete(Long id) {
        validId(id);
        boolean deleted = dao.delete(id);
        if (!deleted) {
            throw new NotFoundException("The Id " + id + " was not found to delete");
        }
        LOGGER.info("Employee with ID: {} successfully deleted!", id);
    }

    private void dtoToEntity(EmployeeDTO dto, Employee employee) {
        employeeDataValidation(dto);
        employee.setName(dto.getName());
        employee.setSalary(dto.getSalary());
        employee.setHiringDate(dto.getHiringDate());
        employee.setRole(Role.valueOf(dto.getRole()));
    }


    private void employeeDataValidation(EmployeeDTO dto) {
        if (dto.getName() == null) {
            throw new BusinessRuleException("The name can't be null");
        }
        if (dto.getSalary() == null) {
            throw new BusinessRuleException("The salary can't be null");
        }
        if (dto.getHiringDate() == null) {
            throw new BusinessRuleException("The hiring date can't be null");
        }
        if (dto.getName().isBlank()) {
            throw new BusinessRuleException("The name can't be blank");
        }
        if (dto.getSalary().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessRuleException("The salary must be greater than 0");
        }
        validateRole(dto.getRole());
        LOGGER.debug("All the information from the DTO are valid.");
    }

    private void validateRole(String dtoRole) {
        if (dtoRole == null) {
            throw new BusinessRuleException("You must specify the employee role");
        }
        if (dtoRole.isBlank()) {
            throw new BusinessRuleException("You must specify the employee role");
        }

        try {
            Role.valueOf(dtoRole);
        } catch (IllegalArgumentException e) {
            throw new BusinessRuleException("Invalid role: " + dtoRole + ". Available roles: " + Arrays.toString(Role.values()));
        }
    }

    private void validId(Long id) {
        if (id == null) {
            throw new BusinessRuleException("The ID cannot be null");
        }
        if (id < 0) {
            throw new BusinessRuleException("ID number must be grater than 0");
        }
    }

    private Employee verifyOptional(Optional<Employee> optional) {
        if (optional.isEmpty()) {
            throw new NotFoundException("The employee does not exist");
        }
        LOGGER.debug("The Optional contain a Employee");
        return optional.get();
    }
}
