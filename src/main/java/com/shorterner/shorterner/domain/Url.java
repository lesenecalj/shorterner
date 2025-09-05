package com.shorterner.shorterner.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity()
@Data
@Builder
public class Url {
    @Id
    @GeneratedValue private UUID id;

    @Column(nullable = false, name="long_url")
    private String longUrl;

    @Column(nullable = false, name="code")
    private String code;

    @CreationTimestamp
    @Column(nullable = false, name="created_at", updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false, name="updated_at")
    private Instant updatedAt;

}
