package com.freetech.sample.securitycommandservice.application.mappers;

import com.freetech.sample.securitycommandservice.domain.models.EntityType;
import com.freetech.sample.securitycommandservice.infraestructure.adapters.out.entities.EntityTypeEntity;

import java.util.function.Function;
import java.util.stream.Stream;

public class EntityTypeEntityMapper {

    public static EntityTypeEntity toEntity(EntityType entityType) {
        if (entityType == null) return null;
        return Stream.of(entityType).map(toEntity()).findFirst().get();
    }

    public static EntityType toDomain(EntityTypeEntity entityTypeEntity) {
        if (entityTypeEntity == null) return null;
        return Stream.of(entityTypeEntity).map(toDomain()).findFirst().get();
    }

    private static Function<EntityType, EntityTypeEntity> toEntity() {
        return entityType -> {
            var entityTypeEntity = new EntityTypeEntity();
            entityTypeEntity.setId(entityType.getId());
            entityTypeEntity.setName(entityType.getName());
            entityTypeEntity.setDescription(entityType.getDescription());

            return entityTypeEntity;
        };
    }

    private static Function<EntityTypeEntity, EntityType> toDomain() {
        return entityTypeEntity -> {
            var entityType = new EntityType();
            entityType.setId(entityTypeEntity.getId());
            entityType.setName(entityTypeEntity.getName());
            entityType.setDescription(entityTypeEntity.getDescription());

            return entityType;
        };
    }

}
