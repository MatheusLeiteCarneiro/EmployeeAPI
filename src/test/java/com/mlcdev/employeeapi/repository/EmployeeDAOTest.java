package com.mlcdev.employeeapi.repository;

import com.mlcdev.employeeapi.config.DatabaseConfig;
import com.mlcdev.employeeapi.exception.DatabaseException;
import com.mlcdev.employeeapi.model.Employee;
import com.mlcdev.employeeapi.model.Role;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class EmployeeDAOTest {

    private EmployeeDAO dao;

    @BeforeAll
    static void setupDatabase(){
        try(Connection conn = DatabaseConfig.getConnection()){
            String query = """
            CREATE TABLE IF NOT EXISTS employee (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(255) NOT NULL,
                salary DECIMAL(19, 2) NOT NULL,
                hiring_date DATE NOT NULL,
                role VARCHAR(50) NOT NULL
            );
        """;
            conn.createStatement().execute(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void clearDatabase(){
        String query = "TRUNCATE TABLE employee;";
        try(Connection conn = DatabaseConfig.getConnection()){
            conn.createStatement().execute(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        dao = new EmployeeDAO();
    }

    private void addBaseEmployeeToDatabase(){
        String query = "INSERT INTO employee (name, salary, hiring_date, role) VALUES ('name', '1.00', '2000-01-01', 'INTERN');";
        try(Connection conn = DatabaseConfig.getConnection()){
            conn.createStatement().execute(query);
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    private void assertNotNullAndEqualsEmployee(Employee expected, Employee actual){
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getName(), actual.getName());
        Assertions.assertEquals(0, expected.getSalary().compareTo(actual.getSalary()));
        Assertions.assertEquals(expected.getHiringDate(), actual.getHiringDate());
        Assertions.assertEquals(expected.getRole(), actual.getRole());
    }

    private Employee getBaseEmployee(){
        return new Employee("name", new BigDecimal("1.00"), LocalDate.of(2000, 1, 1), Role.INTERN);
    }

    private Employee getBaseEmployee(Long id){
        return new Employee(id,"name", new BigDecimal("1.00"), LocalDate.of(2000, 1, 1), Role.INTERN);
    }

    @Nested
    class HappyPath{

        @Test
        void findByIdShouldReturnAnOptionalOfEmployee(){
            addBaseEmployeeToDatabase();
            Employee result = dao.findById(1L).get();
            assertNotNullAndEqualsEmployee(getBaseEmployee(1L) ,result);
        }

        @Test
        void findAllShouldReturnAListOfEmployee(){
            addBaseEmployeeToDatabase();
            addBaseEmployeeToDatabase();
            List<Employee> employeeList = dao.findAll(10, 0);
            assertNotNullAndEqualsEmployee(getBaseEmployee(1L), employeeList.get(0));
            assertNotNullAndEqualsEmployee(getBaseEmployee(2L), employeeList.get(1));
        }

        @Test
        void saveShouldPersistAndReturnEmployee(){
            Employee result = dao.save(getBaseEmployee());
            assertNotNullAndEqualsEmployee(getBaseEmployee(1L), result);
        }

        @Test
        void updateShouldModifyDatabaseData(){
            addBaseEmployeeToDatabase();
            Employee employee = getBaseEmployee();
            employee.setId(1L);
            employee.setName("name2");
            dao.update(employee);
            assertNotNullAndEqualsEmployee(employee,dao.findById(1L).get());
        }

        @Test
        void deleteShouldRemoveEmployee(){
            addBaseEmployeeToDatabase();
            Assertions.assertTrue(dao.delete(1L));
            Assertions.assertEquals(Optional.empty(), dao.findById(1L));
        }



    }

    private void dropTable(){
        String query = "DROP TABLE employee";
        try(Connection conn = DatabaseConfig.getConnection()){
            conn.createStatement().execute(query);
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Nested
    class Exceptions{

        @Test
        void saveShouldThrowExceptionWhenDataIsInvalid(){
           Employee employee = getBaseEmployee();
           employee.setName("a".repeat(500));
           Assertions.assertThrows(DatabaseException.class, () -> {
               dao.save(employee);
           });
        }

        @Test
        void updateShouldThrowExceptionWhenDataIsInvalid(){
            addBaseEmployeeToDatabase();
            Employee employee = getBaseEmployee(1L);
            employee.setName("a".repeat(500));
            Assertions.assertThrows(DatabaseException.class, () -> {
                dao.update(employee);
            });
        }

        @Test
        void deleteShouldThrowExceptionWithInstableDatabase(){
            addBaseEmployeeToDatabase();
            dropTable();
            try {
                Assertions.assertThrows(DatabaseException.class, () -> {
                    dao.delete(1L);
                });
            }
            finally {
                setupDatabase();
            }
        }

        @Test
        void findByIdShouldThrowExceptionWithInstableDatabase(){
            addBaseEmployeeToDatabase();
            dropTable();
            try {
                Assertions.assertThrows(DatabaseException.class, () -> {
                    dao.findById(1L);
                });
            }finally {
                setupDatabase();
            }
        }

        @Test
        void findAllShouldThrowExceptionWithInstableDatabase(){
            addBaseEmployeeToDatabase();
            addBaseEmployeeToDatabase();
            dropTable();
            try {
                Assertions.assertThrows(DatabaseException.class, () -> {
                    dao.findAll(10, 0);
                });
            }
            finally {
                setupDatabase();
            }
        }



    }
    @AfterAll
    static void endTableTest(){
        String query = "DROP TABLE employee IF EXISTS;";
        try(Connection conn = DatabaseConfig.getConnection()){
            conn.createStatement().execute(query);
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
