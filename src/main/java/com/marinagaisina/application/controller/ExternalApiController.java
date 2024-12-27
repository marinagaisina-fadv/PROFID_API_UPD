package com.marinagaisina.application.controller;

import com.marinagaisina.application.entity.EmployeeEntity;
import com.marinagaisina.application.model.VendorRecord;
import com.marinagaisina.application.service.ApiClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping("/vendorData")
public class ExternalApiController {

    @Autowired
    private ApiClientService apiClientService;

    @GetMapping("/{id}")
    public ResponseEntity<VendorRecord> getInfoFromExternalAPI(@PathVariable("id") Long id) {
        VendorRecord response = apiClientService.getData(id);
        if (response == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Record not found");
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<VendorRecord> postInfoIntoExternalAPI(@RequestBody EmployeeEntity employee) {
        VendorRecord response = apiClientService.postData(employee);
        if (response == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Record not found");
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
