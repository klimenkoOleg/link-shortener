package com.oklimenko.shorteninglink.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for links incoming/outcoming.
 * @author oklimenko@gmail.com
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LinkDto {
    private String url;
}
