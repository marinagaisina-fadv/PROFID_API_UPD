package com.marinagaisina.application.entity;

import com.marinagaisina.application.validator.Age;
import com.marinagaisina.application.validator.AllEmployees;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@Entity
@Table(name = "employee")
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emp_id")
    private Long id;

    @NotBlank(message = "First name must not be blank", groups = AllEmployees.class)
    @Size(max = 50, message = "First name must be less than or equal to 50 characters")
    @Column(name = "first_name")
    private String firstName;

    @NotBlank(message = "Last name must not be blank", groups = AllEmployees.class)
    @Size(max = 50, message = "Last name must be less than or equal to 50 characters")
    @Column(name = "last_name")
    private String lastName;

    @Age(min = 18L, max = 65L, groups = AllEmployees.class)
    @Column(name = "dob")
    private LocalDate dateOfBirth;

    @Email(message = "Please provide a valid email address")
    @NotBlank(message = "email must not be blank")
    @Column(name = "email")
    private  String email;
}
