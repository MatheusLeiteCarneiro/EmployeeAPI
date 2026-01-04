package service;

import repository.EmployeeDAO;

public class EmployeeService {
    private final EmployeeDAO dao;

    public EmployeeService(EmployeeDAO dao) {
        this.dao = dao;
    }


}
