package com.freetech.sample.securitycommandservice.infraestructure.adapters.in.http.mappers;

import com.freetech.sample.securitycommandservice.domain.models.Entity;
import com.freetech.sample.securitycommandservice.domain.models.EntityType;
import com.freetech.sample.securitycommandservice.domain.models.User;
import com.freetech.sample.securitycommandservice.infraestructure.adapters.in.http.dtos.ChangePasswordDto;
import com.freetech.sample.securitycommandservice.infraestructure.adapters.in.http.dtos.NewUserDto;
import com.freetech.sample.securitycommandservice.infraestructure.adapters.in.http.dtos.UpdateUserDto;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserMapper {

    public static <T> User toDomain(T dto) {
        if (dto == null) return null;
        return Stream.of(dto).map(toDomain()).findFirst().get();
    }

    public static <T> T toDto(User user, Class<T> clazz) {
        if (user == null) return null;
        return (T) Stream.of(user).map(toDto(clazz)).findFirst().get();
    }

    private static <T> Function<T, User> toDomain() {
        return dto -> {
            var user = new User();
            if (dto instanceof NewUserDto) {
                var newUserDto = (NewUserDto) dto;
                user.setId(newUserDto.getId());
                user.setUsername(newUserDto.getUsername());
                user.setPassword(newUserDto.getPassword());
                user.setConfirmPassword(newUserDto.getConfirmPassword());
                user.setStatus(newUserDto.getStatus());
                user.setEntity(new Entity());
                user.getEntity().setNumberDocument(newUserDto.getNumberDocument());
                user.getEntity().setName(newUserDto.getEntityName());
                user.getEntity().setLastname(newUserDto.getEntityLastname());
                user.getEntity().setEntityType(new EntityType());
                user.getEntity().getEntityType().setId(newUserDto.getEntityTypeId());
            } else if (dto instanceof UpdateUserDto) {
                var updateUserDto = (UpdateUserDto) dto;
                user.setId(updateUserDto.getId());
                user.setUsername(updateUserDto.getUsername());
                user.setStatus(updateUserDto.getStatus());
                user.setEntity(new Entity());
                user.getEntity().setNumberDocument(updateUserDto.getNumberDocument());
                user.getEntity().setName(updateUserDto.getEntityName());
                user.getEntity().setLastname(updateUserDto.getEntityLastname());
                user.getEntity().setEntityType(new EntityType());
                user.getEntity().getEntityType().setId(updateUserDto.getEntityTypeId());
            } else if (dto instanceof ChangePasswordDto) {
                var changePasswordDto = (ChangePasswordDto) dto;
                user.setPassword(changePasswordDto.getPassword());
                user.setNewPassword(changePasswordDto.getNewPassword());
                user.setConfirmNewPassword(changePasswordDto.getConfirmNewPassword());
            }

            return user;
        };
    }

    private static <T> Function<User, T> toDto(Class<T> clazz) {
        return user -> {
            try {
                var dto = clazz.getDeclaredConstructor().newInstance();
                if (dto instanceof NewUserDto) {
                    ((NewUserDto) dto).setId(user.getId());
                    ((NewUserDto) dto).setUsername(user.getUsername());
                    ((NewUserDto) dto).setPassword(user.getPassword());
                    ((NewUserDto) dto).setStatus(user.getStatus());
                    if (user.getEntity() != null) {
                        ((NewUserDto) dto).setNumberDocument(user.getEntity().getNumberDocument());
                        ((NewUserDto) dto).setEntityName(user.getEntity().getName());
                        ((NewUserDto) dto).setEntityLastname(user.getEntity().getLastname());

                        if (user.getEntity().getEntityType() != null) {
                            ((NewUserDto) dto).setEntityTypeId(user.getEntity().getEntityType().getId());
                        }
                    }
                } else if (dto instanceof UpdateUserDto) {
                    ((UpdateUserDto) dto).setId(user.getId());
                    ((UpdateUserDto) dto).setUsername(user.getUsername());
                    ((UpdateUserDto) dto).setStatus(user.getStatus());
                    if (user.getEntity() != null) {
                        ((UpdateUserDto) dto).setNumberDocument(user.getEntity().getNumberDocument());
                        ((UpdateUserDto) dto).setEntityName(user.getEntity().getName());
                        ((UpdateUserDto) dto).setEntityLastname(user.getEntity().getLastname());

                        if (user.getEntity().getEntityType() != null) {
                            ((UpdateUserDto) dto).setEntityTypeId(user.getEntity().getEntityType().getId());
                        }
                    }
                }

                return dto;
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        };
    }

}
