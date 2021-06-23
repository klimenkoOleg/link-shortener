package com.oklimenko.shorteninglink.service;

import com.oklimenko.shorteninglink.exceptions.LongUrlInvalidException;
import com.oklimenko.shorteninglink.exceptions.ShortUrlNotFound;
import com.oklimenko.shorteninglink.model.domain.ShortLink;
import com.oklimenko.shorteninglink.model.dto.LinkDto;
import com.oklimenko.shorteninglink.repository.Incrementer;
import com.oklimenko.shorteninglink.repository.URLRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * Facade for shorten and unwrapping URLs.
 *
 * @author oklimenko@gmail.com
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ShortLinksService {

    private final URLRepository urlRepository;
    private final IdConverter idConverter;
    private final UrlValidator urlValidator;
    private final Incrementer incrementer;

    /**
     * From long URL to short URL.
     *
     * @param localURL local HTTP URL.
     * @param longUrl  long URL to be shortened.
     * @return short URL.
     */
    public Mono<LinkDto> createShortenLink(String localURL, String longUrl) {
        log.info("Shortening longUrl={}", longUrl);
        if (!urlValidator.validateURL(longUrl)) {
            throw new LongUrlInvalidException("Url is invalid: " + longUrl);
        }
        Long id = incrementer.incrementId();
        String uniqueID = idConverter.createUniqueID(id);
        urlRepository.save(new ShortLink(id, longUrl));

        String baseString = formatLocalURLFromShortener(localURL);
        String shortenedURL = baseString + uniqueID;
        return Mono.just(new LinkDto(shortenedURL));
    }

    /**
     * Restore full URL by short URL (ID).
     *
     * @param uniqueID short URL (ID).
     * @return restored full URL.
     */
    public Mono<String> getLongURLFromID(String uniqueID) {
        log.info("Converting shortened URL back to Long Url using uniqueID={}", uniqueID);
        idConverter.getDictionaryKeyFromUniqueID(uniqueID);
        Long dictionaryKey = idConverter.getDictionaryKeyFromUniqueID(uniqueID);
        Optional<ShortLink> longUrl = urlRepository.findById(dictionaryKey);
        return Mono.just(longUrl
                .orElseThrow(() -> new ShortUrlNotFound("Not found Short Url by uniqueID: " + uniqueID))
                .getShortLink());
    }

    private String formatLocalURLFromShortener(String localURL) {
        return localURL + "/";
    }
}
