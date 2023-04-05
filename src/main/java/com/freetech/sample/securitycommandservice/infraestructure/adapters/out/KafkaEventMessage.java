package com.freetech.sample.securitycommandservice.infraestructure.adapters.out;

import com.freetech.sample.securitycommandservice.infraestructure.ports.out.EventMessage;

import interfaces.EventAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;

@RequiredArgsConstructor
@EventAdapter
public class KafkaEventMessage implements EventMessage {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public boolean sendMessage(String topic, String message) {
        kafkaTemplate.send(topic, message);
        return true;
    }
}