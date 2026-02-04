package com.mlcdev.employeeapi.service;

import com.mlcdev.employeeapi.dto.EmployeeDTO;
import com.mlcdev.employeeapi.exception.BusinessRuleException;
import com.mlcdev.employeeapi.exception.NotFoundException;
import com.mlcdev.employeeapi.model.Employee;
import com.mlcdev.employeeapi.model.Role;
import com.mlcdev.employeeapi.repository.EmployeeDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @InjectMocks
    EmployeeService service;

    @Mock
    EmployeeDAO dao;

    @Nested
    class HappyPath {

        private void assertNotNullAndEquals(EmployeeDTO expected, EmployeeDTO actual) {
            Assertions.assertNotNull(actual, "The object returned shouldn't be null");
            Assertions.assertEquals(expected.getId(), actual.getId(), "The ID isn't the same");
            Assertions.assertEquals(expected.getName(), actual.getName(), "The name isn't the same");
            Assertions.assertEquals(expected.getSalary(), actual.getSalary(), "the salary isn't the same");
            Assertions.assertEquals(expected.getHiringDate(), actual.getHiringDate(), "The hiring date isn't the same");
            Assertions.assertEquals(expected.getRole(), actual.getRole(), "The role isn't the same");
        }

        @Test
        void findByIdShouldReturnADTO() {
            LocalDate testDate = LocalDate.now();
            Employee employee = new Employee(1L, "name", new BigDecimal("100.00"), testDate, Role.INTERN);
            Mockito.when(dao.findById(1L)).thenReturn(Optional.of(employee));
            EmployeeDTO expected = new EmployeeDTO(employee);

            EmployeeDTO actual = service.findById(1L);

            Mockito.verify(dao).findById(1L);
            assertNotNullAndEquals(expected, actual);
        }

        @Test
        void findAllShouldReturnAListOfDTO() {
            LocalDate testDate = LocalDate.now();
            Employee employee1 = new Employee(1L, "name", new BigDecimal("100.00"), testDate, Role.INTERN);
            Employee employee2 = new Employee(2L, "name2", new BigDecimal("1000.00"), testDate, Role.MID_LEVEL);
            List<Employee> inputList = List.of(employee1, employee2);
            Mockito.when(dao.findAll(10, 0)).thenReturn(inputList);
            List<EmployeeDTO> expected = inputList.stream().map(EmployeeDTO::new).toList();

            List<EmployeeDTO> result = service.findAll(1, 10);

            Mockito.verify(dao).findAll(10, 0);
            for (int i = 0; i < result.size(); i++) {
                assertNotNullAndEquals(expected.get(i), result.get(i));
            }
        }

        @Test
        void addEmployeeShouldReturnADTO() {
            LocalDate testDate = LocalDate.now();
            EmployeeDTO inputDto = new EmployeeDTO("name", new BigDecimal("100.00"), testDate, Role.INTERN.name());
            Employee savedEntity = new Employee(1L, inputDto.getName(), inputDto.getSalary(), inputDto.getHiringDate(), Role.valueOf(inputDto.getRole()));
            Mockito.when(dao.save(any(Employee.class))).thenReturn(savedEntity);

            EmployeeDTO result = service.add(inputDto);

            Mockito.verify(dao).save(any(Employee.class));
            Assertions.assertNotNull(result, "The object returned shouldn't be null");
            Assertions.assertEquals(1L, result.getId(), "The ID isn't the same");
            Assertions.assertEquals(inputDto.getName(), result.getName(), "The name isn't the same");
            Assertions.assertEquals(inputDto.getSalary(), result.getSalary(), "the salary isn't the same");
            Assertions.assertEquals(inputDto.getHiringDate(), result.getHiringDate(), "The hiring date isn't the same");
            Assertions.assertEquals(inputDto.getRole(), result.getRole(), "The role isn't the same");
        }

        @Test
        void updateShouldReturnADTO() {
            LocalDate testDate = LocalDate.now();
            EmployeeDTO inputDto = new EmployeeDTO(1L, "Updated Name", new BigDecimal("5000.00"), testDate, Role.SENIOR.name());
            Employee updatedEntity = new Employee(1L, "Updated Name", new BigDecimal("5000.00"), testDate, Role.SENIOR);
            Mockito.when(dao.update(any(Employee.class))).thenReturn(Optional.of(updatedEntity));

            EmployeeDTO result = service.update(inputDto);

            Mockito.verify(dao).update(any(Employee.class));
            assertNotNullAndEquals(inputDto, result);
        }

        @Test
        void deleteShouldSucceedWhenEmployeeExists() {
            Mockito.when(dao.delete(1L)).thenReturn(true);

            service.delete(1L);

            Mockito.verify(dao).delete(1L);

        }
    }

    @Nested
    class Validations{
        @Test
        void addNullNameShouldThrowException() {
            EmployeeDTO dto = new EmployeeDTO(1L, null, new BigDecimal("1.0"), LocalDate.now(), Role.INTERN.name());

            Assertions.assertThrows(BusinessRuleException.class, () -> {
                service.add(dto);
            }, "It shouldn't accept a null name");

            Mockito.verifyNoInteractions(dao);
        }

        @Test
        void addBlankNameShouldThrowException() {
            EmployeeDTO dto = new EmployeeDTO(1L, " ", new BigDecimal("1.0"), LocalDate.now(), Role.INTERN.name());

            Assertions.assertThrows(BusinessRuleException.class, () -> {
                service.add(dto);
            }, "It shouldn't accept a blank name");

            Mockito.verifyNoInteractions(dao);
        }

        @Test
        void addNullSalaryShouldThrowException() {
            EmployeeDTO dto = new EmployeeDTO(1L, "name", null, LocalDate.now(), Role.INTERN.name());

            Assertions.assertThrows(BusinessRuleException.class, () -> {
                service.add(dto);
            }, "It shouldn't accept a null salary");

            Mockito.verifyNoInteractions(dao);
        }

        @Test
        void addNegativeSalaryShouldThrowException() {
            EmployeeDTO dto = new EmployeeDTO(1L, "name", new BigDecimal("-1.0"), LocalDate.now(), Role.INTERN.name());

            Assertions.assertThrows(BusinessRuleException.class, () -> {
                service.add(dto);
            }, "It shouldn't accept a salary under 0");

            Mockito.verifyNoInteractions(dao);
        }

        @Test
        void addNullHiringDateShouldThrowException() {
            EmployeeDTO dto = new EmployeeDTO(1L, "name", new BigDecimal("1.0"), null, Role.INTERN.name());

            Assertions.assertThrows(BusinessRuleException.class, () -> {
                service.add(dto);
            }, "It shouldn't accept a null hiring date");

            Mockito.verifyNoInteractions(dao);
        }

        @Test
        void addNullRoleShouldThrowException() {
            EmployeeDTO dto = new EmployeeDTO(1L, "name", new BigDecimal("1.0"), LocalDate.now(), null);

            Assertions.assertThrows(BusinessRuleException.class, () -> {
                service.add(dto);
            }, "It shouldn't accept a null role");

            Mockito.verifyNoInteractions(dao);
        }

        @Test
        void addBlankRoleShouldThrowException() {
            EmployeeDTO dto = new EmployeeDTO(1L, "name", new BigDecimal("1.0"), LocalDate.now(), " ");

            Assertions.assertThrows(BusinessRuleException.class, () -> {
                service.add(dto);
            }, "It shouldn't accept a blank role name");

            Mockito.verifyNoInteractions(dao);
        }

        @Test
        void addInvalidRoleShouldThrowException() {
            EmployeeDTO dto = new EmployeeDTO(1L, "name", new BigDecimal("1.0"), LocalDate.now(), "NotExistingRole");

            Assertions.assertThrows(BusinessRuleException.class, () -> {
                service.add(dto);
            }, "It shouldn't accept a invalid role");

            Mockito.verifyNoInteractions(dao);
        }

        @Test
        void findAllShouldThrowExceptionWhenSizeIsZero() {
            Assertions.assertThrows(BusinessRuleException.class, () -> {
                service.findAll(1, 0);
            }, "Should throw BusinessRuleException when size is 0");

            Mockito.verifyNoInteractions(dao);
        }

        @Test
        void findAllShouldThrowExceptionWhenSizeIsNegative() {
            Assertions.assertThrows(BusinessRuleException.class, () -> {
                service.findAll(1, -5);
            }, "Should throw BusinessRuleException when size is negative");

            Mockito.verifyNoInteractions(dao);
        }

        @Test
        void findAllShouldThrowExceptionWhenPageIsZero() {
            Assertions.assertThrows(BusinessRuleException.class, () -> {
                service.findAll(0, 10);
            }, "Should throw BusinessRuleException when page is 0");

            Mockito.verifyNoInteractions(dao);
        }

        @Test
        void findAllShouldThrowExceptionWhenPageIsNegative() {
            Assertions.assertThrows(BusinessRuleException.class, () -> {
                service.findAll(-1, 10);
            }, "Should throw BusinessRuleException when page is negative");

            Mockito.verifyNoInteractions(dao);
        }
    }

    @Nested
    class Exceptions{
        @Test
        void findByIdShouldThrowNotFoundExceptionWhenEmployeeNotExists() {
            Mockito.when(dao.findById(1L)).thenReturn(Optional.empty());

            Assertions.assertThrows(NotFoundException.class, () -> {
                service.findById(1L);
            });

            Mockito.verify(dao).findById(1L);
        }

        @Test
        void updateShouldThrowNotFoundExceptionWhenEmployeeNotExists() {
            EmployeeDTO inputDto = new EmployeeDTO(999L, "name", new BigDecimal("100.00"), LocalDate.now(), Role.JUNIOR.name());
            Mockito.when(dao.update(any(Employee.class))).thenReturn(Optional.empty());

            Assertions.assertThrows(NotFoundException.class, () -> {
                service.update(inputDto);
            });

            Mockito.verify(dao).update(any(Employee.class));
        }

        @Test
        void deleteShouldThrowNotFoundExceptionWhenEmployeeNotExists() {
            Mockito.when(dao.delete(999L)).thenReturn(false);

            Assertions.assertThrows(NotFoundException.class, () -> {
                service.delete(999L);
            });

            Mockito.verify(dao).delete(999L);
        }
    }
}