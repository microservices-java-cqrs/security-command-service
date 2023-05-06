package com.freetech.sample.securitycommandservice.application.validations;

import com.freetech.sample.securitycommandservice.application.config.ServiceConfig;
import com.freetech.sample.securitycommandservice.application.enums.ExceptionEnum;
import com.freetech.sample.securitycommandservice.application.exceptions.BussinessException;
import com.freetech.sample.securitycommandservice.domain.models.User;
import com.freetech.sample.securitycommandservice.infraestructure.adapters.out.entities.EntityEntity;
import com.freetech.sample.securitycommandservice.infraestructure.adapters.out.entities.EntityTypeEntity;
import com.freetech.sample.securitycommandservice.infraestructure.adapters.out.entities.UserEntity;
import com.freetech.sample.securitycommandservice.infraestructure.ports.out.EntityRepository;
import crypto.Cryptography;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserValidation {
    private final EntityRepository entityRepository;
    private final ServiceConfig serviceConfig;

    public void validateCreateUser(User user) {
        if (!user.isPasswordConfirm())
            throw new BussinessException(
                    ExceptionEnum.ERROR_NOT_CONFIRM_PASSWORD.getCode(),
                    ExceptionEnum.ERROR_NOT_CONFIRM_PASSWORD.getMessage(),
                    ExceptionEnum.ERROR_NOT_CONFIRM_PASSWORD.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);

        var entityTypeEntity = entityRepository.getByField(
                "id",
                user.getEntity().getEntityType().getId(),
                EntityTypeEntity.class);

        if (entityTypeEntity == null)
            throw new BussinessException(
                    ExceptionEnum.ERROR_NOT_FOUND_ENTITY_TYPE.getCode(),
                    ExceptionEnum.ERROR_NOT_FOUND_ENTITY_TYPE.getMessage(),
                    ExceptionEnum.ERROR_NOT_FOUND_ENTITY_TYPE.getMessage(),
                    HttpStatus.NOT_FOUND);

        var userEntity = entityRepository.getByField(
                "username",
                user.getUsername(),
                UserEntity.class);

        if (userEntity != null)
            throw new BussinessException(
                    ExceptionEnum.ERROR_EXIST_USERNAME_USER.getCode(),
                    ExceptionEnum.ERROR_EXIST_USERNAME_USER.getMessage(),
                    ExceptionEnum.ERROR_EXIST_USERNAME_USER.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);

        userEntity = entityRepository.getByField(
                "entityEntity.numberDocument",
                user.getEntity().getNumberDocument(),
                UserEntity.class);

        if (userEntity != null)
            throw new BussinessException(
                    ExceptionEnum.ERROR_EXIST_NUMBER_DOCUMENT_ENTITY.getCode(),
                    ExceptionEnum.ERROR_EXIST_NUMBER_DOCUMENT_ENTITY.getMessage(),
                    ExceptionEnum.ERROR_EXIST_NUMBER_DOCUMENT_ENTITY.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);

    }

    public void validateUpdateUser(User user) {
        var userEntity = entityRepository.getByField(
                "id", user.getId(), UserEntity.class);

        if (userEntity == null)
            throw new BussinessException(
                    ExceptionEnum.ERROR_NOT_FOUND_USER.getCode(),
                    ExceptionEnum.ERROR_NOT_FOUND_USER.getMessage(),
                    ExceptionEnum.ERROR_NOT_FOUND_USER.getMessage(),
                    HttpStatus.NOT_FOUND);

        var entityEntity = entityRepository.getByField(
                "id", userEntity.getEntityEntity().getId(),
                EntityEntity.class);

        if (entityEntity == null)
            throw new BussinessException(
                    ExceptionEnum.ERROR_NOT_FOUND_ENTITY.getCode(),
                    ExceptionEnum.ERROR_NOT_FOUND_ENTITY.getMessage(),
                    ExceptionEnum.ERROR_NOT_FOUND_ENTITY.getMessage(),
                    HttpStatus.NOT_FOUND);

        var entityTypeEntity = entityRepository.getByField(
                "id", user.getEntity().getEntityType().getId(),
                EntityTypeEntity.class);

        if (entityTypeEntity == null)
            throw new BussinessException(
                    ExceptionEnum.ERROR_NOT_FOUND_ENTITY_TYPE.getCode(),
                    ExceptionEnum.ERROR_NOT_FOUND_ENTITY_TYPE.getMessage(),
                    ExceptionEnum.ERROR_NOT_FOUND_ENTITY_TYPE.getMessage(),
                    HttpStatus.NOT_FOUND);

        userEntity = entityRepository.getByField(
                "username",
                user.getUsername(),
                UserEntity.class);

        if (userEntity != null && userEntity.getId().intValue() != user.getId().intValue())
            throw new BussinessException(
                    ExceptionEnum.ERROR_EXIST_USERNAME_USER.getCode(),
                    ExceptionEnum.ERROR_EXIST_USERNAME_USER.getMessage(),
                    ExceptionEnum.ERROR_EXIST_USERNAME_USER.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);

        userEntity = entityRepository.getByField(
                "entityEntity.numberDocument",
                user.getEntity().getNumberDocument(),
                UserEntity.class);

        if (userEntity != null && userEntity.getId().intValue() != user.getId().intValue())
            throw new BussinessException(
                    ExceptionEnum.ERROR_EXIST_NUMBER_DOCUMENT_ENTITY.getCode(),
                    ExceptionEnum.ERROR_EXIST_NUMBER_DOCUMENT_ENTITY.getMessage(),
                    ExceptionEnum.ERROR_EXIST_NUMBER_DOCUMENT_ENTITY.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public void validateDeleteUser(User user) {
        var userEntity = entityRepository.getByField(
                "id", user.getId(), UserEntity.class);

        if (userEntity == null)
            throw new BussinessException(
                    ExceptionEnum.ERROR_NOT_FOUND_USER.getCode(),
                    ExceptionEnum.ERROR_NOT_FOUND_USER.getMessage(),
                    ExceptionEnum.ERROR_NOT_FOUND_USER.getMessage(),
                    HttpStatus.NOT_FOUND);

        var entityEntity = entityRepository.getByField(
                "id", userEntity.getEntityEntity().getId(),
                EntityEntity.class);

        if (entityEntity == null)
            throw new BussinessException(
                    ExceptionEnum.ERROR_NOT_FOUND_ENTITY.getCode(),
                    ExceptionEnum.ERROR_NOT_FOUND_ENTITY.getMessage(),
                    ExceptionEnum.ERROR_NOT_FOUND_ENTITY.getMessage(),
                    HttpStatus.NOT_FOUND);
    }

    public void validateChangePassword(User user) {
        if (!user.isNewPasswordConfirm())
            throw new BussinessException(
                    ExceptionEnum.ERROR_NOT_CONFIRM_NEW_PASSWORD.getCode(),
                    ExceptionEnum.ERROR_NOT_CONFIRM_NEW_PASSWORD.getMessage(),
                    ExceptionEnum.ERROR_NOT_CONFIRM_NEW_PASSWORD.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);

        var userEntity = entityRepository.getByField(
                "username", user.getLogUsername(), UserEntity.class);

        if (userEntity == null)
            throw new BussinessException(
                    ExceptionEnum.ERROR_NOT_FOUND_USER.getCode(),
                    ExceptionEnum.ERROR_NOT_FOUND_USER.getMessage(),
                    ExceptionEnum.ERROR_NOT_FOUND_USER.getMessage(),
                    HttpStatus.NOT_FOUND);

        if (!userEntity.getPassword().equals(Cryptography.encrypt(user.getPassword(),
                serviceConfig.getCryptoSecretKey())))
            throw new BussinessException(
                    ExceptionEnum.ERROR_PASSWORD_INCORRECT_USER.getCode(),
                    ExceptionEnum.ERROR_PASSWORD_INCORRECT_USER.getMessage(),
                    ExceptionEnum.ERROR_PASSWORD_INCORRECT_USER.getMessage(),
                    HttpStatus.NOT_FOUND);
    }

    public void validateAddRolesToUser(Long id) {
        var userEntity = entityRepository.getByField(
                "id", id, UserEntity.class);

        if (userEntity == null)
            throw new BussinessException(
                    ExceptionEnum.ERROR_NOT_FOUND_USER.getCode(),
                    ExceptionEnum.ERROR_NOT_FOUND_USER.getMessage(),
                    ExceptionEnum.ERROR_NOT_FOUND_USER.getMessage(),
                    HttpStatus.NOT_FOUND);
    }
}
