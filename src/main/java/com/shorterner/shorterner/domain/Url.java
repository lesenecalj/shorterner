package com.shorterner.shorterner.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity()
@Table(name = "url", indexes = {
        @Index(name = "idx_url_code", columnList = "code", unique = true),
        @Index(name = "idx_url_canonical", columnList = "canonical_long_url", unique = true)
})
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Url {
    @Id
    @GeneratedValue
    @org.hibernate.annotations.UuidGenerator
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, name = "long_url", columnDefinition = "text")
    private String longUrl;

    @Column(nullable = false, name = "canonical_long_url", columnDefinition = "text", unique = true)
    private String canonicalLongUrl;

    @Column(nullable = false, name = "code", unique = true)
    private String code;

    @CreationTimestamp
    @Column(nullable = false, name = "created_at", updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false, name = "updated_at")
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }
}
