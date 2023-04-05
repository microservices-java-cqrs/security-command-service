package com.freetech.sample.securitycommandservice.application.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class ServiceConfig {
    @Value("${security.service.cryptograpy.secretkey}")
    private String cryptoSecretKey;

    @Value("${security.kafka.event.message.topic.users}")
    private String topicUsers;

    @Value("${security.kafka.event.message.topic.entity_types}")
    private String topicEntityTypes;
}
