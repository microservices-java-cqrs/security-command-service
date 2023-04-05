package com.freetech.sample.securitycommandservice.application.validations;

import com.freetech.sample.securitycommandservice.application.enums.ExceptionEnum;
import com.freetech.sample.securitycommandservice.application.exceptions.BussinessException;
import com.freetech.sample.securitycommandservice.domain.models.EntityType;
import com.freetech.sample.securitycommandservice.infraestructure.adapters.out.entities.EntityEntity;
import com.freetech.sample.securitycommandservice.infraestructure.adapters.out.entities.EntityTypeEntity;
import com.freetech.sample.securitycommandservice.infraestructure.ports.out.EntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EntityTypeValidation {
    private final EntityRepository entityRepository;

    public void validateCreateEntityType(EntityType entityType) {
        var entityTypeEntity = entityRepository.getByField(
                "name", entityType.getName(), EntityTypeEntity.class
        );

        if (entityTypeEntity != null)
            throw new BussinessException(
                    ExceptionEnum.ERROR_EXIST_NAME_ENTITY_TYPE.getCode(),
                    ExceptionEnum.ERROR_EXIST_NAME_ENTITY_TYPE.getMessage(),
                    ExceptionEnum.ERROR_EXIST_NAME_ENTITY_TYPE.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public void validateUpdateEntityType(EntityType entityType) {
        var entityTypeEntity = entityRepository.getByField(
                "id", entityType.getId(), EntityTypeEntity.class
        );

        if (entityTypeEntity == null)
            throw new BussinessException(
                    ExceptionEnum.ERROR_NOT_FOUND_ENTITY_TYPE.getCode(),
                    ExceptionEnum.ERROR_NOT_FOUND_ENTITY_TYPE.getMessage(),
                    ExceptionEnum.ERROR_NOT_FOUND_ENTITY_TYPE.getMessage(),
                    HttpStatus.NOT_FOUND);

        var entityTypeEntityExist = entityRepository.getByField(
                "name", entityType.getName(), EntityTypeEntity.class
        );

        if (entityTypeEntityExist != null
                && entityTypeEntityExist.getId().intValue() != entityTypeEntity.getId())
            if (entityTypeEntity != null)
                throw new BussinessException(
                        ExceptionEnum.ERROR_EXIST_NAME_ENTITY_TYPE.getCode(),
                        ExceptionEnum.ERROR_EXIST_NAME_ENTITY_TYPE.getMessage(),
                        ExceptionEnum.ERROR_EXIST_NAME_ENTITY_TYPE.getMessage(),
                        HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public void validateDeleteEntityType(EntityType entityType) {
        var entityTypeEntity = entityRepository.getByField(
                "id", entityType.getId(), EntityTypeEntity.class
        );

        if (entityTypeEntity == null)
            throw new BussinessException(
                    ExceptionEnum.ERROR_NOT_FOUND_ENTITY_TYPE.getCode(),
                    ExceptionEnum.ERROR_NOT_FOUND_ENTITY_TYPE.getMessage(),
                    ExceptionEnum.ERROR_NOT_FOUND_ENTITY_TYPE.getMessage(),
                    HttpStatus.NOT_FOUND);

        var numEntities = entityRepository.countByField(
                "entityTypeEntity.id",
                entityTypeEntity.getId(),
                EntityEntity.class);

        if (numEntities > 0)
            throw new BussinessException(
                    ExceptionEnum.ERROR_EXITS_BY_ENTITY_TYPE_ENTITY.getCode(),
                    ExceptionEnum.ERROR_EXITS_BY_ENTITY_TYPE_ENTITY.getMessage(),
                    ExceptionEnum.ERROR_EXITS_BY_ENTITY_TYPE_ENTITY.getMessage(),
                    HttpStatus.NOT_FOUND);
    }
}
