package com.shorterner.shorterner.service;

import com.shorterner.shorterner.domain.Url;
import com.shorterner.shorterner.dto.input.CreateUrlInput;
import com.shorterner.shorterner.dto.output.UrlOutput;
import com.shorterner.shorterner.repository.UrlRepository;
import org.springframework.stereotype.Service;

@Service
public class UrlService {
    UrlRepository urlRepository;
    UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    public UrlOutput createUrl(CreateUrlInput input) {
        Url url = Url.builder().longUrl(input.longUrl()).build();
        url.setShortUrl("test");
        System.out.println(url);
        Url urlCreated = this.urlRepository.save(url);
        return UrlOutput.fromEntity(urlCreated);
    }
}
