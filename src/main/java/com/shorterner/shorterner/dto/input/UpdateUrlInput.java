package com.shorterner.shorterner.dto.input;

import jakarta.validation.constraints.NotBlank;

public record UpdateUrlInput(@NotBlank String longUrl) {
}