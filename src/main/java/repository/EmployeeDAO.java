package repository;

import com.mysql.cj.result.SqlDateValueFactory;
import config.DatabaseConfig;
import exception.customException.DatabaseException;
import exception.customException.NotFoundException;
import model.Employee;
import model.Role;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeDAO {

    public Optional<Employee> findById(Long id){
        Employee employee = null;
        String query = "SELECT * FROM employee WHERE id = ?;";
        try(Connection con = DatabaseConfig.getConnection() ; PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            try(ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    employee = setDatabaseAttributesToEmployee(rs);
                }
            }
        } catch (Exception e) {
            throw new DatabaseException("Error Selecting the employee", e);
        }
        return Optional.ofNullable(employee);
    }

    public List<Employee> findAll(){
        List<Employee> employeeList = new ArrayList<>();
        String query = "SELECT * FROM employee;";
        try(Connection con = DatabaseConfig.getConnection() ;
            PreparedStatement preparedStatement = con.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();) {

            while (rs.next()){
                employeeList.add(setDatabaseAttributesToEmployee(rs));
            }
        } catch (Exception e) {
            throw new DatabaseException("Error on Getting the employee list", e);
        }
        return employeeList;
    }

    public Employee save(Employee employee){
        String query = "INSERT INTO employee (name,salary,role,hiring_date) VALUES (?,?,?,?);";
        try(Connection con = DatabaseConfig.getConnection(); PreparedStatement preparedStatement = con.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)){
            setEmployeeStatements(preparedStatement, employee);
            preparedStatement.executeUpdate();
            try(ResultSet rs = preparedStatement.getGeneratedKeys()){
             if(rs.next()){
                    employee.setId(rs.getLong(1));
                }
            }
        }
        catch (Exception e){
            throw new DatabaseException("Error on the insertion", e);
        }
        return employee;
    }

    public Employee update(Employee employee){
        String query = "UPDATE employee SET name = ?, salary = ?, role = ?, hiring_date = ? WHERE id = ?;";
        try(Connection con = DatabaseConfig.getConnection(); PreparedStatement preparedStatement = con.prepareStatement(query)){
            setEmployeeStatements(preparedStatement, employee);
            preparedStatement.setLong(5, employee.getId());
            int rowsAffected = preparedStatement.executeUpdate();
            if(rowsAffected == 0){
                throw new NotFoundException("The with ID: " + employee.getId() + " not found");
            }
        }
        catch (SQLException e){
            throw new DatabaseException("Error on updating the employee", e);
        }
        return employee;
    }



    private void setEmployeeStatements(PreparedStatement preparedStatement, Employee employee) throws SQLException {
        preparedStatement.setString(1,employee.getName());
        preparedStatement.setBigDecimal(2, employee.getSalary());
        preparedStatement.setString(3, employee.getRole().name());
        preparedStatement.setDate(4, Date.valueOf(employee.getHiringDate()));
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
