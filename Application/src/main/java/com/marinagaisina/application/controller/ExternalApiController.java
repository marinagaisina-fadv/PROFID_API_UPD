package com.marinagaisina.application.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marinagaisina.application.entity.EmployeeEntity;
import com.marinagaisina.application.model.ApiRecordResponse;
import com.marinagaisina.application.service.ApiClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/queryData")
public class ExternalApiController {

    @Autowired
    private ApiClientService apiClientService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiRecordResponse> getInfoFromExternalAPI(@PathVariable("id") Long id) {
        ApiRecordResponse response = apiClientService.getRecord(id);
        if (response == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Record not found");
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
