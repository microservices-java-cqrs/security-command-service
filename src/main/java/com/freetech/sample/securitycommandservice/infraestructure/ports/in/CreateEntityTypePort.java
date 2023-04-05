package com.freetech.sample.securitycommandservice.infraestructure.ports.in;

import com.freetech.sample.securitycommandservice.domain.models.EntityType;

public interface CreateEntityTypePort {
    EntityType createEntityType(EntityType entityType);
}
