package com.freetech.sample.securitycommandservice.infraestructure.ports.out;

public interface EventMessage {
    boolean sendMessage(String topic, String message);
}
