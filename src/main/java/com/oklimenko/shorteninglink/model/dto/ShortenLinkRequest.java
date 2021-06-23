package com.oklimenko.shorteninglink.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for incoming request to shorten URL.
 * @author oklimenko@gmal.com
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShortenLinkRequest {
    private String url;
}
