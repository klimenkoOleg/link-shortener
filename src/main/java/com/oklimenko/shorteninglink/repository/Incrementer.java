package com.oklimenko.shorteninglink.repository;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

/**
 * It requires minimum 3 instance of Hazelcast to use distributed IAtomicLong.
 * For simplicity in memory counter is used, standard Java AtomicLong.
 *
 * @author oklimenko@gmail.com
 */
@Component
public class Incrementer {
    //    Use this for multi HZ instances with CP Subsystem enabled:
    //    private HazelcastInstance hazelcastInstance;
    private AtomicLong counter = new AtomicLong(1);

    /**
     * Increment ID of next request.
     *
     * @return
     */
    public Long incrementId() {
        // Use this for multi HZ instances with CP Subsystem enabled: IAtomicLong counter = hazelcastInstance.getCPSubsystem().getAtomicLong( "counter" );
        return counter.getAndIncrement();
    }
}
