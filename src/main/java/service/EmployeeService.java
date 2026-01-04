package service;

import controller.dto.EmployeeDTO;
import exception.customException.BusinessRuleException;
import exception.customException.NotFoundException;
import model.Employee;
import model.Role;
import repository.EmployeeDAO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EmployeeService {
    private final EmployeeDAO dao;

    public EmployeeService(EmployeeDAO dao) {
        this.dao = dao;
    }

    public EmployeeDTO findById(Long id){
        Optional<Employee> optionalEmployee = dao.findById(id);
        return new EmployeeDTO(verifyOptional(optionalEmployee));
    }

    public List<EmployeeDTO> findAll(){
         return dao.findAll().stream().map(x -> new EmployeeDTO(x)).collect(Collectors.toList());
    }

    public EmployeeDTO add(EmployeeDTO dto){
        Employee employee = new Employee();
        dtoToEntity(dto, employee);
        employee = dao.save(employee);
        dto = new EmployeeDTO(employee);
        return dto;
    }

    public EmployeeDTO update(EmployeeDTO dto){
        Long id = dto.getId();
        validId(id);
        Employee employee = new Employee();
        dtoToEntity(dto, employee);
        employee.setId(id);
        Optional<Employee> optionalEmployee = dao.update(employee);
        return new EmployeeDTO(verifyOptional(optionalEmployee));
    }

    public void delete(Long id){
        validId(id);
        boolean deleted = dao.delete(id);
        if(!deleted){
            throw new NotFoundException("The Id " + id + " was not found to delete");
        }
    }

    private void dtoToEntity(EmployeeDTO dto, Employee employee){
        employeeDataValidation(dto);
        employee.setName(dto.getName());
        employee.setSalary(dto.getSalary());
        employee.setHiringDate(dto.getHiringDate());
        employee.setRole(Role.valueOf(dto.getRole()));
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
            throw new BusinessRuleException("The salary must be positive");
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

    private void validId(Long id){
        if(id == null){
            throw new BusinessRuleException("The ID cannot be null");
        }
        if(id < 0 ){
            throw new BusinessRuleException("ID number must be grater than 0");
        }

    }

    private Employee verifyOptional(Optional<Employee> optional){
        if(optional.isEmpty()){
            throw new NotFoundException("The employee does not exist");
        }
        return optional.get();
    }
}
