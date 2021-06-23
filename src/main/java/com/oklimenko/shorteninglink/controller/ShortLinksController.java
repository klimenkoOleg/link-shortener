package com.oklimenko.shorteninglink.controller;

import com.oklimenko.shorteninglink.model.dto.LinkDto;
import com.oklimenko.shorteninglink.model.dto.ShortenLinkRequest;
import com.oklimenko.shorteninglink.service.ShortLinksService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

/**
 * REST Controller.
 * Also uses Swagger to create Swagger UI.
 * Limitation: ACCEPTS ONLY ENGLISH URLS.
 *
 * @author oklimenko@gmail.com
 */
@Slf4j
@Tag(name = "ShortLinks")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/short-link")
public class ShortLinksController {

    private final ShortLinksService shortLinksService;

    /**
     * Creates short link from full link.
     *
     * @param shortenRequest request body with full url.
     * @param request        instance of ServerHttpRequest.
     * @return object with short url.
     */
    @PostMapping
    public Mono<LinkDto> createShortenLink(@RequestBody @Valid final ShortenLinkRequest shortenRequest, ServerHttpRequest request) {
        String localURL = request.getURI().toString();
        log.debug("Received url to shorten: " + shortenRequest.getUrl());
        return shortLinksService.createShortenLink(localURL, shortenRequest.getUrl());
    }

    /**
     * Unwraps URL: by short ID it returns full URL.
     *
     * @param id       short ID.
     * @param response instance of ServerHttpResponse.
     * @return object with full URL.
     */
    @GetMapping(value = "/{id}")
    public Mono<LinkDto> redirectUrl(@PathVariable String id, ServerHttpResponse response) {
        log.debug("Received shortened url to redirect: " + id);
        return shortLinksService.getLongURLFromID(id)
                .map(redirectUrlString -> new LinkDto(redirectUrlString));
    }
}
