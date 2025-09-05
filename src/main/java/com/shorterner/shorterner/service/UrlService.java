package com.shorterner.shorterner.service;

import com.shorterner.shorterner.domain.Url;
import com.shorterner.shorterner.dto.input.CreateUrlInput;
import com.shorterner.shorterner.dto.output.UrlOutput;
import com.shorterner.shorterner.exception.UrlNotFoundException;
import com.shorterner.shorterner.repository.UrlRepository;
import com.shorterner.shorterner.utils.CodeGen;
import com.shorterner.shorterner.utils.Helper;
import org.springframework.stereotype.Service;

@Service
public class UrlService {
    UrlRepository urlRepository;

    UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    public UrlOutput createUrl(CreateUrlInput input) {
        String longUrl = input.longUrl();
        if (longUrl == null || longUrl.isBlank()) {
            throw new IllegalArgumentException("URL cannot be empty");
        }

        if (!Helper.validLongUrl(longUrl)) {
            throw new IllegalArgumentException("Wrong Url");
        }

        String code = CodeGen.codeFor(longUrl);
        Url url = Url.builder()
                .longUrl(longUrl)
                .code(code)
                .build();
        Url urlCreated = this.urlRepository.save(url);
        return UrlOutput.fromEntity(urlCreated);
    }

    public String lookupUrl(String code) {
        Url url = this.getUrlByCode(code);
        if (url == null) {
            throw new UrlNotFoundException("No URL found for code: " + code);
        }
        return url.getLongUrl();
    }

    public Url getUrlByCode(String code) {
        return this.urlRepository.findByCode(code).orElse(null);
    }

}
