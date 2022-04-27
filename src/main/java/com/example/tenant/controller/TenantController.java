package com.example.tenant.controller;

import com.example.tenant.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin(origins = "*")
public class TenantController {

    @Autowired
    private TenantService tenantService;

    @GetMapping(path = "/emissor")
    public ResponseEntity<Void> emissor(@RequestParam String nome) {
        tenantService.retornaCiclos(nome);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
