package com.freetech.sample.securitycommandservice.application.mappers;

import com.freetech.sample.securitycommandservice.domain.models.User;
import com.freetech.sample.securitycommandservice.infraestructure.adapters.out.entities.EntityEntity;
import com.freetech.sample.securitycommandservice.infraestructure.adapters.out.entities.EntityTypeEntity;
import com.freetech.sample.securitycommandservice.infraestructure.adapters.out.entities.UserEntity;

import java.util.function.Function;
import java.util.stream.Stream;

public class UserEntityMapper {

    public static UserEntity toEntity(User user) {
        if (user == null) return null;
        return Stream.of(user).map(toEntity()).findFirst().get();
    }

    private static Function<User, UserEntity> toEntity() {
        return user -> {
            var userEntity = new UserEntity();
            userEntity.setId( user.getId() );
            userEntity.setUsername( user.getUsername() );
            userEntity.setPassword( user.getPassword() );
            userEntity.setStatus( user.getStatus() );

            if (user.getEntity() != null) {
                userEntity.setEntityEntity(new EntityEntity());
                userEntity.getEntityEntity().setId(user.getEntity().getId());
                userEntity.getEntityEntity().setNumberDocument(user.getEntity().getNumberDocument());
                userEntity.getEntityEntity().setBussinessName(user.getEntity().getBussinessName());
                userEntity.getEntityEntity().setName(user.getEntity().getName());
                userEntity.getEntityEntity().setLastname(user.getEntity().getLastname());

                if(user.getEntity().getEntityType() != null) {
                    userEntity.getEntityEntity().setEntityTypeEntity(new EntityTypeEntity());
                    userEntity.getEntityEntity().getEntityTypeEntity().setId(user.getEntity().getEntityType().getId());
                    userEntity.getEntityEntity().getEntityTypeEntity().setName(user.getEntity().getEntityType().getName());
                    userEntity.getEntityEntity().getEntityTypeEntity().setDescription(user.getEntity().getEntityType().getDescription());
                }
            }
            return userEntity;
        };
    }

}
