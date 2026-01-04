package controller.dto;

import model.Employee;
import model.Role;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
        this.role = employee.getRole().name();
    }


    public EmployeeDTO(String name, BigDecimal salary, LocalDate hiringDate, String role) {
        this.name = name;
        this.salary = salary;
        this.hiringDate = hiringDate;
        this.role = role.toUpperCase();
    }

    public EmployeeDTO(Long id, String name, BigDecimal salary, LocalDate hiringDate, String role) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.hiringDate = hiringDate;
        this.role = role.toUpperCase();
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
