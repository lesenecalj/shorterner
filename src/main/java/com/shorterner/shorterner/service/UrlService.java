package com.shorterner.shorterner.service;

import com.shorterner.shorterner.domain.Url;
import com.shorterner.shorterner.dto.input.CreateUrlInput;
import com.shorterner.shorterner.dto.output.UrlOutput;
import com.shorterner.shorterner.exception.UrlNotFoundException;
import com.shorterner.shorterner.repository.UrlRepository;
import com.shorterner.shorterner.utils.CodeGen;
import com.shorterner.shorterner.utils.Helper;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class UrlService {
    UrlRepository urlRepository;

    UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    public Url getUrlByCode(String code) {
        return this.urlRepository.findByCode(code).orElse(null);
    }

    @Transactional
    public UrlOutput createUrl(CreateUrlInput input) {
        String longUrl = input.longUrl();
        String canonicalizeLongUrl = Helper.canonicalize(longUrl);
        return this.urlRepository
                .findByCanonicalLongUrl(canonicalizeLongUrl)
                .map(UrlOutput::fromEntity)
                .orElseGet(() -> {
                    String code = CodeGen.codeFor(canonicalizeLongUrl);
                    Url url = Url.builder()
                            .longUrl(longUrl)
                            .canonicalLongUrl(canonicalizeLongUrl)
                            .code(code)
                            .build();
                    try {
                        return UrlOutput.fromEntity(urlRepository.save(url));
                    } catch (DataIntegrityViolationException e) {
                        return urlRepository.findByCanonicalLongUrl(canonicalizeLongUrl)
                                .map(UrlOutput::fromEntity)
                                .orElseThrow(() -> e);
                    }
                });
    }

    public String lookupUrl(String code) {
        Url url = this.getUrlByCode(code);
        if (url == null) {
            throw new UrlNotFoundException("No URL found for code: " + code);
        }
        return url.getLongUrl();
    }

    @Transactional
    public UrlOutput update(String code, String newLongUrl) {
        Url url = this.getUrlByCode(code);
        if (url == null) {
            throw new UrlNotFoundException("No URL found for code: " + code);
        }

        String canonical = Helper.canonicalize(newLongUrl);

        this.urlRepository.findByCanonicalLongUrl(canonical).ifPresent(existing -> {
            if (!existing.getId().equals(url.getId())) {
                throw new DataIntegrityViolationException("This long URL is already used by another short code.");
            }
        });

        try {
            url.setLongUrl(newLongUrl);
            url.setCanonicalLongUrl(canonical);
            return UrlOutput.fromEntity(this.urlRepository.save(url));
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityViolationException("This long URL is already used by another short code.");
        }
    }
}
