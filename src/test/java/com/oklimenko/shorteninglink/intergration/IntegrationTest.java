package com.oklimenko.shorteninglink.intergration;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.core.HazelcastInstance;
import com.oklimenko.shorteninglink.config.HazelcastConfiguration;
import com.oklimenko.shorteninglink.model.dto.LinkDto;
import com.oklimenko.shorteninglink.model.dto.ShortenLinkRequest;
import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.data.hazelcast.repository.config.EnableHazelcastRepositories;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureWebTestClient
@Import(IntegrationTest.Config.class)
public class IntegrationTest {

    private final static String SHORT_LINK = "facebook.com";

    @ClassRule
    public static Network network = Network.newNetwork();

    @Container
    public static GenericContainer hzContainer = new GenericContainer(DockerImageName.parse("hazelcast/hazelcast:4.2"))
            .withExposedPorts(5701)
            .withNetwork(network)
            .waitingFor(Wait.forListeningPort());
//            .withEnv("HZ_NETWORK_PUBLICADDRESS", "127.0.0.1:5701");

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void test_CreateShortLink_ReturnsShortLink() {
        ShortenLinkRequest body = new ShortenLinkRequest("facebook.com");
        webTestClient
                .post()
                .uri(uriBuilder -> uriBuilder.path( "/v1/short-link")
                        .build())
                .body(Mono.just(body), ShortenLinkRequest.class)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectHeader().contentType(APPLICATION_JSON)
                .expectStatus().isOk()
                .expectBody(LinkDto.class).isEqualTo(new LinkDto("/v1/short-link/b"));
    }

    @Configuration
    @EnableHazelcastRepositories(basePackages = {"com.oklimenko.shorteninglink.repository"})
    @ComponentScan(basePackages = {"com.oklimenko.shorteninglink"}, excludeFilters={
            @ComponentScan.Filter(type= FilterType.ASSIGNABLE_TYPE, value=HazelcastConfiguration.class)})
    public static class Config {
        public ClientConfig clientConfig() {
            ClientConfig clientConfig = new ClientConfig();
            ClientNetworkConfig networkConfig = clientConfig.getNetworkConfig();
            networkConfig.addAddress(hzContainer.getContainerIpAddress() + ":" + hzContainer.getFirstMappedPort())
                    .setSmartRouting(true)
                    .setConnectionTimeout(1000);
            return clientConfig;
        }

        @Bean
        @Primary
        public HazelcastInstance hazelcastInstance() {
            ClientConfig clientConfig = new ClientConfig();
            clientConfig.getNetworkConfig().addAddress(hzContainer.getContainerIpAddress()
                    + ":" + hzContainer.getFirstMappedPort());
            return HazelcastClient.newHazelcastClient(clientConfig);
        }
    }
}
