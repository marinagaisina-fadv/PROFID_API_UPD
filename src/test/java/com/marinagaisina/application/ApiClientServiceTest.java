package com.marinagaisina.application;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.marinagaisina.application.entity.EmployeeEntity;
import com.marinagaisina.application.exception.CustomTimeoutException;
import com.marinagaisina.application.model.VendorRecord;
import com.marinagaisina.application.service.ApiClientService;
import com.marinagaisina.application.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.SocketTimeoutException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class ApiClientServiceTest {

    @InjectMocks
    private ApiClientService apiClientService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private EmployeeService employeeService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetData_Success() {
        // Arrange
        Long id = 1L;
        String url = "http://example.com/api/records/" + id;
        VendorRecord mockResponse = VendorRecord.builder()
                .id(id)
                .firstName("John")
                .lastName("Doe")
                .gender("M")
                .address("34 18th Ave")
                .dateOfBirth(LocalDate.now().minusYears(20))
                .email("johndoe@gmail.com")
                .records(Arrays.asList("record1", "record2", "record3"))
                .build();

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> requestEntity = new HttpEntity<>("parameters", headers);

        ResponseEntity<VendorRecord> responseEntity = mock(ResponseEntity.class);
        when(restTemplate.exchange(url, HttpMethod.GET, requestEntity, VendorRecord.class)).thenReturn(responseEntity);
        when(responseEntity.getBody()).thenReturn(mockResponse);

        // Act
        VendorRecord result = apiClientService.getData(id);

        // Assert
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        verify(employeeService, times(1)).addEmployee(any(EmployeeEntity.class));
    }

    @Disabled
    @Test
    public void testGetData_TimeoutException() {
        // Arrange
        Long id = 1L;
        String url = "http://example.com/api/records/" + id;
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> requestEntity = new HttpEntity<>("parameters", headers);

        ResourceAccessException exception = new ResourceAccessException("Timeout", new SocketTimeoutException("Read timeout"));
        when(restTemplate.exchange(url, HttpMethod.GET, requestEntity, VendorRecord.class)).thenThrow(exception);

        // Act & Assert
        CustomTimeoutException thrown = assertThrows(CustomTimeoutException.class, () -> apiClientService.getData(id));
        assertEquals("Read timeout", thrown.getMessage());
    }

    @Test
    public void testPostData_Success() {
        // Arrange
        EmployeeEntity employeeEntity = EmployeeEntity.builder()
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

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<VendorRecord> requestEntity = new HttpEntity<>(mockResponse, headers);

        ResponseEntity<VendorRecord> responseEntity = mock(ResponseEntity.class);
        when(restTemplate.exchange("http://example.com/api/records/", HttpMethod.POST, requestEntity, VendorRecord.class)).thenReturn(responseEntity);
        when(responseEntity.getBody()).thenReturn(mockResponse);

        // Act
        VendorRecord result = apiClientService.postData(employeeEntity);

        // Assert
        assertNotNull(result);
        assertEquals("Jane", result.getFirstName());
        assertEquals("Doe", result.getLastName());
    }

    @Test
    public void testStoreResult_Success() {
        // Arrange
        VendorRecord vendorRecord = VendorRecord.builder()
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(LocalDate.now().minusYears(30))
                .email("john.doe@example.com")
                .records(Arrays.asList("record1", "record2"))
                .build();

        // Act
        apiClientService.storeResult(vendorRecord);

        // Assert
        verify(employeeService, times(1)).addEmployee(any(EmployeeEntity.class));
    }
}
