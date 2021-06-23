package com.oklimenko.shorteninglink.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.keyvalue.annotation.KeySpace;

import java.io.Serializable;

/**
 * Short link DTO, stored in Hazelcast.
 * @author oklimenko@gmail.com
 */
@Data
@AllArgsConstructor
@KeySpace("shortLink")
public class ShortLink implements Serializable {
    @Id
    private Long id;
    private String shortLink;
}
