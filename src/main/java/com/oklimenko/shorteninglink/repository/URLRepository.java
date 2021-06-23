package com.oklimenko.shorteninglink.repository;

import com.oklimenko.shorteninglink.model.domain.ShortLink;
import org.springframework.data.hazelcast.repository.HazelcastRepository;
import org.springframework.stereotype.Repository;

/**
 * Stores ShortLink in HZ cache.
 *
 * @author oklimenko@gmail.com
 */
@Repository
public interface URLRepository extends HazelcastRepository<ShortLink, Long> {
}
