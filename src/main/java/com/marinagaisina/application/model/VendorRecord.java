package com.marinagaisina.application.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true) // Useful in case of extra JSON properties
public class VendorRecord {
    private Long id;
    private String firstName;
    private String lastName;
    private String gender;
    private String address;
    private LocalDate dateOfBirth;
    private String email;
    private List<String> records;
}

