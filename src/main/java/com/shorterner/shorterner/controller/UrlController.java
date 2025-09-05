package com.shorterner.shorterner.controller;

import com.shorterner.shorterner.dto.input.CreateUrlInput;
import com.shorterner.shorterner.dto.input.UpdateUrlInput;
import com.shorterner.shorterner.dto.output.UrlOutput;
import com.shorterner.shorterner.service.UrlService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/urls")
public class UrlController {
    private final UrlService urlService;

    UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping
    public ResponseEntity<UrlOutput> create(@Valid @RequestBody CreateUrlInput input) {
        UrlOutput url = this.urlService.createUrl(input);
        return ResponseEntity.status(201).body(url);
    }

    @GetMapping("/r/{code}")
    public ResponseEntity<Void> lookup(@PathVariable("code") String code) {
        String longUrl = this.urlService.lookupUrl(code);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(longUrl))
                .build();
    }

    @PutMapping("/{code}")
    public ResponseEntity<UrlOutput> update(
            @PathVariable String code,
            @Valid @RequestBody UpdateUrlInput input
    ) {
        UrlOutput updated = this.urlService.update(code, input.longUrl());
        return ResponseEntity.ok(updated);
    }
}
