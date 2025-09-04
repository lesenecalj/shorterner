package com.shorterner.shorterner.repository;

import com.shorterner.shorterner.domain.Url;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UrlRepository extends JpaRepository<Url, UUID> {
}
