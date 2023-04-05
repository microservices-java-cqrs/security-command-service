package com.freetech.sample.securitycommandservice.infraestructure.adapters.in.http.mappers;

import com.freetech.sample.securitycommandservice.domain.models.EntityType;
import com.freetech.sample.securitycommandservice.infraestructure.adapters.in.http.dtos.NewEntityTypeDto;
import com.freetech.sample.securitycommandservice.infraestructure.adapters.in.http.dtos.UpdateEntityTypeDto;

import java.util.function.Function;
import java.util.stream.Stream;

public class EntityTypeMapper {

    public static <T> T toDto(EntityType entityType, Class<T> clazz) {
        if (entityType == null) return null;
        return (T) Stream.of(entityType).map(toDto(clazz)).findFirst().get();
    }

    public static <T> EntityType toDomain(T dto) {
        if (dto == null) return null;
        return Stream.of(dto).map(toDomain()).findFirst().get();
    }

    private static <T> Function<EntityType, T> toDto(Class<T> clazz) {
        return entityType -> {
            try {
                var dto = clazz.getDeclaredConstructor().newInstance();
                if (dto instanceof NewEntityTypeDto) {
                    ((NewEntityTypeDto) dto).setId(entityType.getId());
                    ((NewEntityTypeDto) dto).setName(entityType.getName());
                    ((NewEntityTypeDto) dto).setDescription(entityType.getDescription());
                } else if (dto instanceof UpdateEntityTypeDto) {
                    ((UpdateEntityTypeDto) dto).setId(entityType.getId());
                    ((UpdateEntityTypeDto) dto).setName(entityType.getName());
                    ((UpdateEntityTypeDto) dto).setDescription(entityType.getDescription());
                }
                return dto;
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        };
    }

    private static <T> Function<T, EntityType> toDomain() {
        return dto -> {
            var entityType = new EntityType();
            if (dto instanceof NewEntityTypeDto) {
                var newEntityTypeDto = (NewEntityTypeDto) dto;
                entityType.setName(newEntityTypeDto.getName());
                entityType.setDescription(newEntityTypeDto.getDescription());
            } else if (dto instanceof UpdateEntityTypeDto) {
                var updateEntityTypeDto = (UpdateEntityTypeDto) dto;
                entityType.setId(updateEntityTypeDto.getId());
                entityType.setName(updateEntityTypeDto.getName());
                entityType.setDescription(updateEntityTypeDto.getDescription());
            }

            return entityType;
        };
    }

}
