package com.oklimenko.shorteninglink.service;

import com.oklimenko.shorteninglink.model.dto.LinkDto;
import com.oklimenko.shorteninglink.repository.Incrementer;
import com.oklimenko.shorteninglink.repository.URLRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class ShortLinksServiceTest {

    public static final String LOCAL_URL = "http://localhost:8080/v1/shortUrl";
    public static final String SHORT_ID = "/b";
    @Mock
    private URLRepository urlRepository;

    @Test
    public void test_incrementID_StartsAt0AndIncrements() {
        ShortLinksService shortLinksService = new ShortLinksService(
                urlRepository, new IdConverter(), new UrlValidator(), new Incrementer());
       Mono<LinkDto> shortUrl = shortLinksService.createShortenLink(LOCAL_URL, "facebook.com");

       assertThat(Objects.requireNonNull(shortUrl.block()).getUrl()).isEqualTo(LOCAL_URL + SHORT_ID);
    }

}