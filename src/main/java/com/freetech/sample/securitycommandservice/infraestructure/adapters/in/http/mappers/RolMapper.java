package com.freetech.sample.securitycommandservice.infraestructure.adapters.in.http.mappers;

import com.freetech.sample.securitycommandservice.domain.models.Rol;
import com.freetech.sample.securitycommandservice.infraestructure.adapters.in.http.dtos.NewRolDto;

import java.util.function.Function;
import java.util.stream.Stream;

public class RolMapper {
    public static <T> Rol toDomain(T dto) {
        if (dto == null) return null;
        return Stream.of(dto).map(toDomain()).findFirst().get();
    }

    public static <T> T toDto(Rol rol, Class<T> clazz) {
        if (rol == null) return null;
        return (T) Stream.of(rol).map(toDto(clazz)).findFirst().get();
    }

    private static <T> Function<T, Rol> toDomain() {
        return dto -> {
            var rol = new Rol();
            if (dto instanceof NewRolDto) {
                var newRolDto = (NewRolDto) dto;
                rol.setName(newRolDto.getName());
                rol.setDescription(newRolDto.getDescription());
            }

            return rol;
        };
    }

    private static <T> Function<Rol, T> toDto(Class<T> clazz) {
        return rol -> {
            try {
                var dto = clazz.getDeclaredConstructor().newInstance();
                if (dto instanceof NewRolDto) {
                    ((NewRolDto) dto).setName(rol.getName());
                    ((NewRolDto) dto).setDescription(rol.getDescription());
                }

                return dto;
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        };
    }
}
