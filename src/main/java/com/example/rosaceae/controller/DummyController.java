package com.example.rosaceae.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/lmao")
@PreAuthorize("hasRole('SUPPER_ADMIN')")
@RequiredArgsConstructor
public class DummyController {

    @GetMapping("/yo")
    @PreAuthorize("hasAuthority('staff:read')")
    public ResponseEntity<String> lmao(){
        return ResponseEntity.ok("ayy lmao");
    }
}
