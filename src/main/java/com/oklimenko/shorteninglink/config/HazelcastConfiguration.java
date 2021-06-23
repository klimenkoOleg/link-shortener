package com.oklimenko.shorteninglink.config;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.hazelcast.repository.config.EnableHazelcastRepositories;

/**
 * Configures Hazelcase client.
 *
 * @author oklimenko@gmail.com
 */
@Configuration
@EnableHazelcastRepositories(basePackages = {"com.oklimenko.shorteninglink.repository"})
public class HazelcastConfiguration {

    @Value("${hazelcast.address}")
    private String hazelcastAddress;

    /**
     * Client config for HZ.
     *
     * @return ClientConfig with connection properties set.
     */
    @Bean
    public ClientConfig clientConfig() {
        ClientConfig clientConfig = new ClientConfig();
        ClientNetworkConfig networkConfig = clientConfig.getNetworkConfig();
        networkConfig.addAddress(hazelcastAddress)
                .setSmartRouting(true)
                .addOutboundPortDefinition("5701-5710")
                .setRedoOperation(true)
                .setConnectionTimeout(1000);
        return clientConfig;
    }

    /**
     * Constructs new instance of HZ client.
     *
     * @param clientConfig - config with connection properties.
     * @return instance of HZ client
     */
    @Bean
    public HazelcastInstance hazelcastInstance(ClientConfig clientConfig) {
        return HazelcastClient.newHazelcastClient(clientConfig);
    }
}