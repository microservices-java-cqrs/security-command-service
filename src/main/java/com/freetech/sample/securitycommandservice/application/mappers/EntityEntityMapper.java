package com.freetech.sample.securitycommandservice.application.mappers;

import com.freetech.sample.securitycommandservice.domain.models.Entity;
import com.freetech.sample.securitycommandservice.infraestructure.adapters.out.entities.EntityEntity;
import com.freetech.sample.securitycommandservice.infraestructure.adapters.out.entities.EntityTypeEntity;
import utils.StringUtil;

import java.util.function.Function;
import java.util.stream.Stream;

public class EntityEntityMapper {

    public static EntityEntity toEntity(Entity entity) {
        if (entity == null) return null;
        return Stream.of(entity).map(toEntity()).findFirst().get();
    }

    private static Function<Entity, EntityEntity> toEntity() {
        return entity -> {
            var entityEntity = new EntityEntity();
            entityEntity.setId(entity.getId());
            entityEntity.setNumberDocument(entity.getNumberDocument());
            entityEntity.setBussinessName(entity.getBussinessName());
            entityEntity.setName(entity.getName());
            entityEntity.setLastname(entity.getLastname());

            if (entity.getEntityType() != null) {
                entityEntity.setEntityTypeEntity(new EntityTypeEntity());
                entityEntity.getEntityTypeEntity().setId(entity.getEntityType().getId());
                entityEntity.getEntityTypeEntity().setName(entity.getEntityType().getName());
                entityEntity.getEntityTypeEntity().setDescription(entity.getEntityType().getDescription());
            }

            return entityEntity;
        };
    }

}
