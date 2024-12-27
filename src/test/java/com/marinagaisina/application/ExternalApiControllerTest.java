package com.marinagaisina.application;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.marinagaisina.application.controller.ExternalApiController;
import com.marinagaisina.application.entity.EmployeeEntity;
import com.marinagaisina.application.model.VendorRecord;
import com.marinagaisina.application.service.ApiClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Arrays;

public class ExternalApiControllerTest {

    @InjectMocks
    private ExternalApiController externalApiController;

    @Mock
    private ApiClientService apiClientService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(externalApiController).build();
    }

    @Test
    public void testGetInfoFromExternalAPI_Success() throws Exception {
        // Arrange
        Long id = 1L;
        VendorRecord mockResponse = VendorRecord.builder()
                .id(id)
                .firstName("John")
                .lastName("Doe")
                .gender("M")
                .address("34 18th Ave")
                .dateOfBirth(LocalDate.now().minusYears(20))
                .email("johndoe@gmail.com")
                .records(Arrays.asList("record1", "record2"))
                .build();

        when(apiClientService.getData(id)).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/vendorData/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    public void testGetInfoFromExternalAPI_NotFound() throws Exception {
        // Arrange
        Long id = 1L;
        when(apiClientService.getData(id)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(get("/vendorData/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Disabled
    @Test
    public void testPostInfoIntoExternalAPI_Success() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Arrange
        EmployeeEntity employee = EmployeeEntity.builder()
                .firstName("Jane")
                .lastName("Doe")
                .dateOfBirth(LocalDate.now().minusYears(25))
                .email("janedoe@gmail.com")
                .records("record1, record2")
                .build();

        VendorRecord mockResponse = VendorRecord.builder()
                .firstName("Jane")
                .lastName("Doe")
                .dateOfBirth(LocalDate.now().minusYears(25))
                .email("janedoe@gmail.com")
                .records(Arrays.asList("record1", "record2"))
                .build();

        when(apiClientService.postData(employee)).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(post("/vendorData")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Disabled
    @Test
    public void testPostInfoIntoExternalAPI_NotFound() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Arrange
        EmployeeEntity employee = EmployeeEntity.builder()
                .firstName("Jane")
                .lastName("Doe")
                .dateOfBirth(LocalDate.now().minusYears(25))
                .email("janedoe@gmail.com")
                .records("record1, record2")
                .build();

        when(apiClientService.postData(employee)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(post("/vendorData")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isNotFound());
    }
}
