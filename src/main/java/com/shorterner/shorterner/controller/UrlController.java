package com.shorterner.shorterner.controller;

import com.shorterner.shorterner.dto.input.CreateUrlInput;
import com.shorterner.shorterner.dto.output.UrlOutput;
import com.shorterner.shorterner.service.UrlService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/urls")
public class UrlController {
    private final UrlService urlService;

    UrlController(UrlService urlService) {
        this.urlService = urlService;
    }
    @PostMapping
    public ResponseEntity<UrlOutput> create (@Valid @RequestBody CreateUrlInput input) {
        UrlOutput url = this.urlService.createUrl(input);
        return ResponseEntity.status(201).body(url);
    }
}
