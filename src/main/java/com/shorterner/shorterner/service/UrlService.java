package com.shorterner.shorterner.service;

import com.shorterner.shorterner.domain.Url;
import com.shorterner.shorterner.dto.input.CreateUrlInput;
import com.shorterner.shorterner.dto.output.UrlOutput;
import com.shorterner.shorterner.exception.UrlNotFoundException;
import com.shorterner.shorterner.repository.UrlRepository;
import com.shorterner.shorterner.utils.CodeGen;
import com.shorterner.shorterner.utils.Helper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UrlService {
    UrlRepository urlRepository;

    UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    public Url getUrlByCode(String code) {
        return this.urlRepository.findByCode(code).orElseGet(() -> {
            log.warn("[UrlService][getUrlByCode]: url not found for code={}", code);
            return null;
        });
    }

    @Transactional
    public UrlOutput createUrl(CreateUrlInput input) {
        String longUrl = input.longUrl();
        String canonicalizeLongUrl = Helper.canonicalize(longUrl);
        return this.urlRepository
                .findByCanonicalLongUrl(canonicalizeLongUrl)
                .map(existing -> {
                    log.info("[UrlService][createUrl]: canonicalizeLongUrl found, canonical={} code={}", canonicalizeLongUrl, existing.getCode());
                    return UrlOutput.fromEntity(existing);
                })
                .orElseGet(() -> {
                    String code = CodeGen.codeFor(canonicalizeLongUrl);
                    Url url = Url.builder()
                            .longUrl(longUrl)
                            .canonicalLongUrl(canonicalizeLongUrl)
                            .code(code)
                            .build();
                    try {
                        log.info("[UrlService][createUrl]: url build with code={} canonical={}", url.getCode(), url.getCanonicalLongUrl());
                        return UrlOutput.fromEntity(urlRepository.save(url));
                    } catch (DataIntegrityViolationException e) {
                        log.warn("[UrlService][createUrl] error on url saving with: code={} canonical={}", url.getCode(), url.getCanonicalLongUrl());
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
            log.warn("[UrlService][update]: url not found for code={}", code);
            throw new UrlNotFoundException("No URL found for code: " + code);
        }

        String canonical = Helper.canonicalize(newLongUrl);

        this.urlRepository.findByCanonicalLongUrl(canonical).ifPresent(existing -> {
            if (!existing.getId().equals(url.getId())) {
                log.warn("[UrlService][update]: url is already existing for id={}", existing.getId());
                throw new DataIntegrityViolationException("This long URL is already used by another short code.");
            }
        });

        try {
            url.setLongUrl(newLongUrl);
            url.setCanonicalLongUrl(canonical);
            log.info("[UrlService][update] url build with longUrl={} for code={}", newLongUrl, code);
            return UrlOutput.fromEntity(this.urlRepository.save(url));
        } catch (DataIntegrityViolationException ex) {
            log.warn("[UrlService][update] error on code={} canonical={}", code, canonical, ex);
            throw new DataIntegrityViolationException("This long URL is already used by another short code.");
        }
    }
}
