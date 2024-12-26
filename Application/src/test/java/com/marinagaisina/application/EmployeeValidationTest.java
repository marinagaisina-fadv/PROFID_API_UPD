package com.marinagaisina.application;

import com.marinagaisina.application.entity.EmployeeEntity;
import com.marinagaisina.application.service.EmployeeService;
import com.marinagaisina.application.validator.AllEmployees;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class EmployeeValidationTest {
    protected static Validator validator;
    private static ValidatorFactory validatorFactory;
    @Autowired
    private EmployeeService employeeService;

    @BeforeAll
    static void setUp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void tearDown() {
        validatorFactory.close();
    }

    @ParameterizedTest
    @MethodSource("invalidTestData_addEmployeeEntity")
    @DisplayName("Add EmployeeEntity - Should fail validation")
    void addEmployeeEntity_shouldFailValidation(EmployeeEntity EmployeeEntity, String invalidProperty, String message) {
        Set<ConstraintViolation<EmployeeEntity>> constraintViolations = validator.validate(EmployeeEntity, AllEmployees.class);
        assertThat(constraintViolations)
                .anyMatch(violation -> violation.getPropertyPath().toString()
                        .equals(invalidProperty) && violation.getMessage().equals(message));
    }

    @Test
    @DisplayName("Add EmployeeEntity - Should pass validation")
    void addEmployeeEntity_shouldPassValidation() {
        EmployeeEntity employee = EmployeeEntity.builder()
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(LocalDate.now().minusYears(20))
                .email("johndoe@gmail.com")
                .build();
        Set<ConstraintViolation<EmployeeEntity>> constraintViolations = validator.validate(employee, AllEmployees.class);
        assertThat(constraintViolations).isEmpty();

    }

    @ParameterizedTest
    @MethodSource("invalidTestData_updateEmployeeEntity")
    @DisplayName("Update EmployeeEntity - should fail validation")
    void updateEmployeeEntity_shouldFailValidation(EmployeeEntity EmployeeEntity, String invalidProperty, String message) {
        Set<ConstraintViolation<EmployeeEntity>> constraintViolations = validator.validate(EmployeeEntity);
        assertThat(constraintViolations).anyMatch(violation -> violation.getPropertyPath().toString().equals(invalidProperty) && violation.getMessage().equals(message));
    }

    private static Stream<Arguments> invalidTestData_addEmployeeEntity() {
        return Stream.of(
                //Pass first name, last name and email as blank strings:
                Arguments.of(EmployeeEntity.builder()
                        .firstName(" ")
                        .lastName("Doe")
                        .dateOfBirth(LocalDate.now().minusYears(20))
                        .email("johndoe@gmail.com")
                        .build(), "firstName", "First name must not be blank"),
                Arguments.of(EmployeeEntity.builder()
                        .firstName("John")
                        .lastName(" ")
                        .dateOfBirth(LocalDate.now().minusYears(20))
                        .email("johndoe@gmail.com")
                        .build(), "lastName", "Last name must not be blank"),
                Arguments.of(EmployeeEntity.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .dateOfBirth(LocalDate.now().minusYears(20))
                        .email(" ")
                        .build(), "email", "email must not be blank"),

                //Pass first name, last name and email as null:
                Arguments.of(EmployeeEntity.builder()
                        .lastName("Doe")
                        .dateOfBirth(LocalDate.now().minusYears(20))
                        .email("johndoe@gmail.com")
                        .build(), "firstName", "First name must not be blank"),
                Arguments.of(EmployeeEntity.builder()
                        .firstName("John")
                        .dateOfBirth(LocalDate.now().minusYears(20))
                        .email("johndoe@gmail.com")
                        .build(), "lastName", "Last name must not be blank"),
                Arguments.of(EmployeeEntity.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .dateOfBirth(LocalDate.now().minusYears(20))
                        .build(), "email", "email must not be blank"),

                //Pass invalid date of birth
                Arguments.of(EmployeeEntity.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .dateOfBirth(LocalDate.now().minusYears(17))
                        .email("johndoe@gmail.com")
                        .build(), "dateOfBirth", "Age must be between 18 and 65"),
                Arguments.of(EmployeeEntity.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .dateOfBirth(LocalDate.now().minusYears(66))
                        .email("johndoe@gmail.com")
                        .build(), "dateOfBirth", "Age must be between 18 and 65"),
                Arguments.of(EmployeeEntity.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .email("johndoe@gmail.com")
                        .build(), "dateOfBirth", "Age must be between 18 and 65"),

                //Pass invalid email
                Arguments.of(EmployeeEntity.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .dateOfBirth(LocalDate.now().minusYears(20))
                        .email("johndoe-gmail.com")
                        .build(), "email", "Please provide a valid email address"),
                Arguments.of(EmployeeEntity.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .dateOfBirth(LocalDate.now().minusYears(20))
                        .email("johndoe.gmail.com")
                        .build(), "email", "Please provide a valid email address"),
                Arguments.of(EmployeeEntity.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .dateOfBirth(LocalDate.now().minusYears(20))
                        .email("@gmail.com")
                        .build(), "email", "Please provide a valid email address"),
                Arguments.of(EmployeeEntity.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .dateOfBirth(LocalDate.now().minusYears(20))
                        .email("johndoe$gmail.com")
                        .build(), "email", "Please provide a valid email address")
        );
    }

    private static Stream<Arguments> invalidTestData_updateEmployeeEntity() {
        return Stream.of(
                Arguments.of(EmployeeEntity.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .dateOfBirth(LocalDate.now().minusYears(20))
                        .build(), "email", "email must not be blank"),
                Arguments.of(EmployeeEntity.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .dateOfBirth(LocalDate.now().minusYears(20))
                        .email("               ")
                        .build(), "email", "email must not be blank"),

                //Pass invalid email
                Arguments.of(EmployeeEntity.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .dateOfBirth(LocalDate.now().minusYears(20))
                        .email("johndoe-gmail.com")
                        .build(), "email", "Please provide a valid email address"),
                Arguments.of(EmployeeEntity.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .dateOfBirth(LocalDate.now().minusYears(20))
                        .email("johndoe.gmail.com")
                        .build(), "email", "Please provide a valid email address"),
                Arguments.of(EmployeeEntity.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .dateOfBirth(LocalDate.now().minusYears(20))
                        .email("@gmail.com")
                        .build(), "email", "Please provide a valid email address"),
                Arguments.of(EmployeeEntity.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .dateOfBirth(LocalDate.now().minusYears(20))
                        .email("johndoe$gmail.com")
                        .build(), "email", "Please provide a valid email address")
        );
    }

    @Test
    @DisplayName("Update EmployeeEntity - Should pass validation")
    void updateEmployeeEntity_shouldPassValidation() {
        EmployeeEntity employee = EmployeeEntity.builder()
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(LocalDate.now().minusYears(20))
                .email("johndoe@gmail.com")
                .build();
        employeeService.addEmployee(employee);
        employeeService.updateEmployee(new EmployeeEntity(employee.getId(), "Larisa","Johnson", LocalDate.of(1990, 12, 10), "johnson@gmail.com"));
        Set<ConstraintViolation<EmployeeEntity>> constraintViolations = validator.validate(employee, AllEmployees.class);
        assertThat(constraintViolations).isEmpty();
        //clean
        employeeService.deleteEmployee(employee.getId());
    }
}
