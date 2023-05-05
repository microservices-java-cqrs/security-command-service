package com.freetech.sample.securitycommandservice.application.mappers;

import com.freetech.sample.securitycommandservice.domain.models.Rol;
import com.freetech.sample.securitycommandservice.infraestructure.adapters.out.entities.RolEntity;

import java.util.function.Function;
import java.util.stream.Stream;

public class RolEntityMapper {
    public static RolEntity toEntity(Rol rol) {
        if (rol == null) return null;
        return Stream.of(rol).map(toEntity()).findFirst().get();
    }

    private static Function<Rol, RolEntity> toEntity() {
        return rol -> {
            var rolEntity = new RolEntity();
            rolEntity.setName(rol.getName());
            rolEntity.setDescription(rol.getDescription());
            return rolEntity;
        };
    }
}
