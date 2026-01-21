package com.mlcdev.employeeapi.dto;

import com.mlcdev.employeeapi.model.Employee;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class EmployeeDTO {
    private Long id;
    private String name;
    private BigDecimal salary;
    private LocalDate hiringDate;
    private String role;

    public EmployeeDTO() {
    }

    public EmployeeDTO(Employee employee) {
        this.id = employee.getId();
        this.name = employee.getName();
        this.salary = employee.getSalary();
        this.hiringDate = employee.getHiringDate();
        if(employee.getRole() != null){
            this.role = employee.getRole().name().toUpperCase();
        }
        else {
            this.role = null;
        }
    }


    public EmployeeDTO(String name, BigDecimal salary, LocalDate hiringDate, String role) {
        this.name = name;
        this.salary = salary;
        this.hiringDate = hiringDate;
        if(role != null){
        this.role = role.toUpperCase();
        }
        else {
            this.role = null;
        }
    }

    public EmployeeDTO(Long id, String name, BigDecimal salary, LocalDate hiringDate, String role) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.hiringDate = hiringDate;
        if(role != null){
            this.role = role.toUpperCase();
        }
        else {
            this.role = null;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public LocalDate getHiringDate() {
        return hiringDate;
    }

    public void setHiringDate(LocalDate hiringDate) {
        this.hiringDate = hiringDate;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role.toUpperCase();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeDTO employee = (EmployeeDTO) o;
        return Objects.equals(id, employee.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
