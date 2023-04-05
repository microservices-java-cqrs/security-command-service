package com.freetech.sample.securitycommandservice.application.validations;

import com.freetech.sample.securitycommandservice.domain.models.Entity;
import com.freetech.sample.securitycommandservice.infraestructure.ports.out.EntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EntityValidation {
    private final EntityRepository entityRepository;

    public void validateCreateEntity(Entity entity) {

    }
}
