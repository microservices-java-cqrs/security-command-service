package com.freetech.sample.securitycommandservice.infraestructure.adapters.in.http.mappers;

import com.freetech.sample.securitycommandservice.domain.models.Entity;
import com.freetech.sample.securitycommandservice.domain.models.EntityType;
import com.freetech.sample.securitycommandservice.domain.models.User;
import com.freetech.sample.securitycommandservice.infraestructure.adapters.in.http.dtos.NewEntityDto;
import com.freetech.sample.securitycommandservice.infraestructure.adapters.in.http.dtos.NewUserDto;
import com.freetech.sample.securitycommandservice.infraestructure.adapters.in.http.dtos.UpdateUserDto;

import java.util.function.Function;
import java.util.stream.Stream;

public class EntityMapper {
    public static <T> Entity toDomain(T dto) {
        if (dto == null) return null;
        return Stream.of(dto).map(toDomain()).findFirst().get();
    }

    public static <T> T toDto(Entity entity, Class<T> clazz) {
        if (entity == null) return null;
        return (T) Stream.of(entity).map(toDto(clazz)).findFirst().get();
    }

    private static <T> Function<T, Entity> toDomain() {
        return dto -> {
            var entity = new Entity();
            if (dto instanceof NewEntityDto) {
                var newEntityDto = (NewEntityDto) dto;
                entity.setId(newEntityDto.getId());
                entity.setEntityType(new EntityType());
                entity.getEntityType().setId(newEntityDto.getEntityTypeId());
                entity.setNumberDocument(newEntityDto.getNumberDocument());
                entity.setBussinessName(newEntityDto.getBussinessName());
            }

            return entity;
        };
    }

    private static <T> Function<Entity, T> toDto(Class<T> clazz) {
        return entity -> {
            try {
                var dto = clazz.getDeclaredConstructor().newInstance();
                if (dto instanceof NewEntityDto) {
                    ((NewEntityDto) dto).setId(entity.getId());
                    ((NewEntityDto) dto).setNumberDocument(entity.getNumberDocument());
                    ((NewEntityDto) dto).setBussinessName(entity.getBussinessName());
                    if (entity.getEntityType() != null) {
                        ((NewEntityDto) dto).setEntityTypeId(entity.getEntityType().getId());
                    }
                }

                return dto;
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        };
    }
}
