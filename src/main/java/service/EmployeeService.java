package service;

import controller.dto.EmployeeDTO;
import exception.customException.BusinessRuleException;
import model.Employee;
import model.Role;
import repository.EmployeeDAO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

public class EmployeeService {
    private final EmployeeDAO dao;

    public EmployeeService(EmployeeDAO dao) {
        this.dao = dao;
    }

    public EmployeeDTO add(EmployeeDTO dto){
        Employee employee = dtoToEntity(dto);
        employee = dao.save(employee);
        dto = new EmployeeDTO(employee);
        return dto;
    }


    private Employee dtoToEntity(EmployeeDTO dto){
        employeeDataValidation(dto);
        Employee employee = new Employee();
        employee.setName(dto.getName());
        employee.setSalary(dto.getSalary());
        employee.setHiringDate(dto.getHiringDate());
        employee.setRole(Role.valueOf(dto.getRole()));
        return employee;
    }

    private void employeeDataValidation(EmployeeDTO dto){
        if(dto.getName() == null){
            throw new BusinessRuleException("The name can't be null");
        }
        if(dto.getSalary() == null){
            throw new BusinessRuleException("The salary can't be null");
        }
        if(dto.getHiringDate() == null){
            throw new BusinessRuleException("The hiring date can't be null");
        }
        if(dto.getName().isBlank()){
            throw new BusinessRuleException("The name can't be blank");
        }
        if(dto.getSalary().compareTo(BigDecimal.ZERO) <= 0){
            throw new BusinessRuleException("The salary must be above 0");
        }
        if(dto.getHiringDate().isAfter(LocalDate.now())){
            throw new BusinessRuleException("The hiring date can't be after today");
        }
        validateRole(dto.getRole());
    }

    private void validateRole(String dtoRole){
        if(dtoRole == null){
            throw new BusinessRuleException("You must specify the employee role");
        }

        try{
            Role.valueOf(dtoRole);
        }catch (IllegalArgumentException e){
            throw new BusinessRuleException("Invalid role: " + dtoRole + ". Available roles: " + Arrays.toString(Role.values()));
        }
    }

}
