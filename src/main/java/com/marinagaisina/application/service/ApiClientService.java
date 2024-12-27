package com.marinagaisina.application.service;

import com.marinagaisina.application.entity.EmployeeEntity;
import com.marinagaisina.application.exception.CustomNetworkException;
import com.marinagaisina.application.exception.CustomTimeoutException;
import com.marinagaisina.application.model.VendorRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.SocketTimeoutException;
import java.net.http.HttpConnectTimeoutException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ApiClientService {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private EmployeeService employeeService;

    public VendorRecord getData(Long id) throws ResourceAccessException {
        String url = "http://example.com/api/records/"+id;
        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create Basic Auth Header
        String auth = "user:password";
        byte[] encodedAuth = Base64Utils.encode(auth.getBytes());
        String authHeader = "Basic " + new String(encodedAuth);
        headers.set("Authorization", authHeader);

        // Create the entity with the headers
        HttpEntity<String> requestEntity = new HttpEntity<>("parameters", headers);

        // Perform the GET request
        ResponseEntity<VendorRecord> response;
        try {
            response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    VendorRecord.class
            );
            // real-world scenario:
            //return response.getBody();

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        //real-world scenario
        //return null;

        //fake scenario:
        VendorRecord vendorRecord = VendorRecord.builder()
                .id(id)
                .firstName("John")
                .lastName("Doe")
                .gender("M")
                .address("34 18th Ave")
                .dateOfBirth(LocalDate.now().minusYears(20))
                .email("johndoe@gmail.com")
                .records(new ArrayList<String>(List.of("record1", "record2", "record3")))
                .build();

        // Calling storeResult that push data into the DB
        storeResult(vendorRecord);

        return vendorRecord;
    }

    public VendorRecord postData(EmployeeEntity employeeEntity) throws ResourceAccessException {
        VendorRecord vendorRecord = VendorRecord.builder()
                .firstName(employeeEntity.getFirstName())
                .lastName(employeeEntity.getLastName())
                .dateOfBirth(employeeEntity.getDateOfBirth())
                .email(employeeEntity.getEmail())
                .records(employeeEntity.getRecordsList())
                .build();
        String url = "http://example.com/api/records/";
        // Create headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create request entity with headers and employee object
        HttpEntity<VendorRecord> requestEntity = new HttpEntity<>(vendorRecord, headers);

        try {
            // Send POST request
            ResponseEntity<VendorRecord> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    VendorRecord.class
            );
        /*} catch (ResourceAccessException e) {
            Throwable cause = e.getCause();
            if (cause instanceof HttpConnectTimeoutException) {
                log.error("Connection timeout: {}", cause.getMessage());
                throw new CustomTimeoutException("Connection timeout", e);
            } else if (cause instanceof SocketTimeoutException) {
                log.error("Read timeout: {}", cause.getMessage());
                throw new CustomTimeoutException("Read timeout", e);
            } else {
                log.error("Resource access exception: {}", cause.getMessage());
                throw new CustomNetworkException("Network error: " + cause.getMessage(), e);
            }*/
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        //real-world scenario:
        //return responseEntity.getBody();

        //fake scenario:
        return vendorRecord;
    }

    public void storeResult(VendorRecord vendorRecord) {
        //try {
            EmployeeEntity employee = EmployeeEntity.builder()
                .firstName(vendorRecord.getFirstName())
                .lastName(vendorRecord.getLastName())
                .dateOfBirth(vendorRecord.getDateOfBirth())
                .email(vendorRecord.getEmail())
                .build();

            // Convert List<String> to a comma-separated string
            List<String> recordsList = vendorRecord.getRecords();
            if (recordsList != null && !recordsList.isEmpty()) {
                employee.setRecordsFromList(recordsList);
            }
            employeeService.addEmployee(employee);
            log.info("New record added: {}", vendorRecord.toString());
        /*} catch (Exception e) {
            log.error("Failed to add new record: {}", vendorRecord.toString());
            log.error(e.getMessage());
        }*/
    }
}