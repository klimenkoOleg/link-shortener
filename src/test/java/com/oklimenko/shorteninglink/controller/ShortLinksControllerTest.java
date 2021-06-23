package com.oklimenko.shorteninglink.controller;

import com.oklimenko.shorteninglink.model.dto.LinkDto;
import com.oklimenko.shorteninglink.model.dto.ShortenLinkRequest;
import com.oklimenko.shorteninglink.service.ShortLinksService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = ShortLinksController.class)
@AutoConfigureWebTestClient
class ShortLinksControllerTest {

    private final static String SHORT_LINK = "asd.ru";

    @MockBean
    ShortLinksService shortLinksService;

    @InjectMocks
    ShortLinksController shortLinksController;

    @Autowired
    protected WebTestClient webTestClient;

    @Test
    public void test_CreateShortLink_ReturnsShortLink() {
        BDDMockito.given(shortLinksService.createShortenLink(anyString(), anyString())).willReturn(Mono.just(new LinkDto(SHORT_LINK)));

        ShortenLinkRequest body = new ShortenLinkRequest("http://facebook.com");
        webTestClient
                .post()
                .uri(uriBuilder -> uriBuilder.path( "/v1/short-link")
                        .build())
                .body(Mono.just(body), ShortenLinkRequest.class)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectHeader().contentType(APPLICATION_JSON)
                .expectStatus().isOk()
                .expectBody(LinkDto.class).isEqualTo(new LinkDto(SHORT_LINK));
    }
}