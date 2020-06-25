package com.testapp.producer.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "spring.kafka", ignoreUnknownFields = false)
@Component
@Setter
@Getter
public class KafkaProperties {

    private String bootstrapServers;

    private Template template = new Template();

    @Setter
    @Getter
    public static final class Template {
        private String defaultTopic;
    }

}
