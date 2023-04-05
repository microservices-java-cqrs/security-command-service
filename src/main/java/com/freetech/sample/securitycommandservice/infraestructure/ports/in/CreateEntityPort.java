package com.freetech.sample.securitycommandservice.infraestructure.ports.in;

import com.freetech.sample.securitycommandservice.domain.models.Entity;

public interface CreateEntityPort {
    Entity createEntity(Entity entity);
}
