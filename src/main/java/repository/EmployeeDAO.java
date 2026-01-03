package repository;

import com.mysql.cj.result.SqlDateValueFactory;
import config.DatabaseConfig;
import exception.customException.DatabaseException;
import model.Employee;
import model.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeDAO {

    public Optional<Employee> findById(Long id){
        Employee employee = null;
        String selectById = "SELECT * FROM employee WHERE id = ?";
        try(Connection con = DatabaseConfig.getConnection() ; PreparedStatement preparedStatement = con.prepareStatement(selectById)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                employee = setDatabaseAttributesToEmployee(resultSet);
            }
        } catch (Exception e) {
            throw new DatabaseException("Error Selecting the employee", e);
        }
        return Optional.ofNullable(employee);
    }

    public List<Employee> findAll(){
        List<Employee> employeeList = new ArrayList<>();
        String selectAll = "SELECT * FROM employee";
        try(Connection con = DatabaseConfig.getConnection() ; PreparedStatement preparedStatement = con.prepareStatement(selectAll)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                employeeList.add(setDatabaseAttributesToEmployee(resultSet));
            }
        } catch (Exception e) {
            throw new DatabaseException("Error on Getting the employee list", e);
        }
        return employeeList;
    }

    private Employee setDatabaseAttributesToEmployee(ResultSet resultSet) throws SQLException {
        Employee employee = new Employee();
        employee.setId(resultSet.getLong("id"));
        employee.setName(resultSet.getString("name"));
        employee.setSalary(resultSet.getBigDecimal("salary"));
        employee.setRole(Role.valueOf(resultSet.getString("role")));
        java.sql.Date dbDate = resultSet.getDate("hiring_date");
        employee.setHiringDate(dbDate.toLocalDate());
        return employee;
    }
}
