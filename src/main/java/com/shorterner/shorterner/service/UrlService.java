package com.shorterner.shorterner.service;

import com.shorterner.shorterner.domain.Url;
import com.shorterner.shorterner.dto.input.CreateUrlInput;
import com.shorterner.shorterner.dto.output.UrlOutput;
import com.shorterner.shorterner.repository.UrlRepository;
import com.shorterner.shorterner.utils.CodeGen;
import org.springframework.stereotype.Service;

@Service
public class UrlService {
    UrlRepository urlRepository;
    UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    public UrlOutput createUrl(CreateUrlInput input) {
        String code = CodeGen.codeFor(input.longUrl());
        Url url = Url.builder()
                .longUrl(input.longUrl())
                .code(code)
                .build();
        Url urlCreated = this.urlRepository.save(url);
        return UrlOutput.fromEntity(urlCreated);
    }

}
