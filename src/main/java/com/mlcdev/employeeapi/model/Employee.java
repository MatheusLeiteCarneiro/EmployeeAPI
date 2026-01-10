package com.mlcdev.employeeapi.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Employee {
    private Long id;
    private String name;
    private BigDecimal salary;
    private LocalDate hiringDate;
    private Role role;

    public Employee() {
    }

    public Employee(String name, BigDecimal salary, LocalDate hiringDate, Role role) {
        this.name = name;
        this.salary = salary;
        this.hiringDate = hiringDate;
        this.role = role;
    }

    public Employee(Long id, String name, BigDecimal salary, LocalDate hiringDate, Role role) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.hiringDate = hiringDate;
        this.role = role;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(id, employee.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return getId() + " | " + getName() + " | " + String.format("%.2f", getSalary()) + " | " + getRole() + " | Hired At: " + getHiringDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}
