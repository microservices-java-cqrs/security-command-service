package com.freetech.sample.securitycommandservice.application.mappers;

import com.freetech.sample.securitycommandservice.domain.models.UserRol;
import com.freetech.sample.securitycommandservice.infraestructure.adapters.out.entities.RolEntity;
import com.freetech.sample.securitycommandservice.infraestructure.adapters.out.entities.UserEntity;
import com.freetech.sample.securitycommandservice.infraestructure.adapters.out.entities.UserRolEntity;

import java.util.function.Function;
import java.util.stream.Stream;

public class UserRolEntityMapper {
    public static UserRolEntity toEntity(UserRol userRol) {
        if (userRol == null) return null;
        return Stream.of(userRol).map(toEntity()).findFirst().get();
    }

    private static Function<UserRol, UserRolEntity> toEntity() {
        return userRol -> {
            var userRolEntity = new UserRolEntity();
            userRolEntity.setUserEntity(new UserEntity());
            userRolEntity.getUserEntity().setId(userRol.getUser().getId());
            userRolEntity.setRolEntity(new RolEntity());
            userRolEntity.getRolEntity().setId(userRol.getRol().getId());
            return userRolEntity;
        };
    }
}
