package com.marinagaisina.application.service;

import com.marinagaisina.application.entity.EmployeeEntity;
import com.marinagaisina.application.exception.CustomNetworkException;
import com.marinagaisina.application.exception.CustomTimeoutException;
import com.marinagaisina.application.model.ApiRecordResponse;
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
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
public class ApiClientService {
    @Autowired
    private RestTemplate restTemplate;

    public ApiRecordResponse getRecord(Long id) {
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
        ResponseEntity<ApiRecordResponse> response;
        try {
            response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    ApiRecordResponse.class
            );
            // real-world scenario:
            //return response.getBody();

        } catch (ResourceAccessException e) {
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
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    //real-world scenario
    //return null;

    //fake scenario:
    return ApiRecordResponse.builder()
            .id(54698L)
            .firstName("John")
            .lastName("Doe")
            .gender("M")
            .address("34 18th Ave")
            .dateOfBirth(LocalDate.now().minusYears(20))
            .email("johndoe@gmail.com")
            .records(new LinkedList<String>(List.of("record1", "record2", "record3")))
            .build();

    }
}