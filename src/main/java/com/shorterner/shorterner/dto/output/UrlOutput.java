package com.shorterner.shorterner.dto.output;

import com.shorterner.shorterner.domain.Url;

import java.time.Instant;
import java.util.UUID;

public record UrlOutput(
        UUID id,
        String longUrl,
        String code,
        Instant createdAt,
        Instant updatedAt
) {
    public static UrlOutput fromEntity(Url url) {
        return new UrlOutput(
                url.getId(),
                url.getLongUrl(),
                url.getCode(),
                url.getCreatedAt(),
                url.getUpdatedAt()
        );
    }
}