package com.freetech.sample.securitycommandservice.application;

import com.freetech.sample.securitycommandservice.application.config.ServiceConfig;
import com.freetech.sample.securitycommandservice.application.enums.ExceptionEnum;
import com.freetech.sample.securitycommandservice.application.exceptions.BussinessException;
import com.freetech.sample.securitycommandservice.application.validations.UserValidation;
import com.freetech.sample.securitycommandservice.domain.models.User;
import com.freetech.sample.securitycommandservice.infraestructure.adapters.out.entities.EntityEntity;
import com.freetech.sample.securitycommandservice.infraestructure.adapters.out.entities.UserEntity;
import com.freetech.sample.securitycommandservice.infraestructure.ports.in.ChangePasswordPort;
import com.freetech.sample.securitycommandservice.infraestructure.ports.out.EntityRepository;
import com.freetech.sample.securitycommandservice.infraestructure.ports.out.EventMessage;
import crypto.Cryptography;
import enums.OperationEnum;
import enums.TableEnum;
import interfaces.UseCase;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import messages.PersistenceMessage;
import messages.UserMessage;
import org.springframework.http.HttpStatus;
import utils.DateUtil;
import utils.JsonUtil;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@UseCase
public class ChangePasswordUseCase implements ChangePasswordPort {
    private final EventMessage eventMessage;
    private final EntityRepository entityRepository;
    private final ServiceConfig serviceConfig;
    private final UserValidation userValidation;

    @Transactional
    @Override
    public User changePassword(User user) {
        userValidation.validateChangePassword(user);

        String messagePersistence = "";
        try {
            var userEntity = entityRepository.getByField(
                    "username", user.getLogUsername(), UserEntity.class);

            userEntity.setPassword(
                    Cryptography.encrypt(user.getNewPassword(),
                            serviceConfig.getCryptoSecretKey()));
            userEntity.setLogUpdateUser(user.getLogUsername());
            userEntity.setLogUpdateDate(DateUtil.getDate());

            userEntity = entityRepository.update(userEntity);

            messagePersistence = JsonUtil.convertoToJson(createChangePasswordMessage(OperationEnum.UPDATE.getValue(), userEntity));
        } catch (Exception ex) {
            throw new BussinessException(
                    ExceptionEnum.ERROR_CHANGE_PASSWORD_USER.getCode(),
                    ExceptionEnum.ERROR_CHANGE_PASSWORD_USER.getMessage(),
                    ex.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            eventMessage.sendMessage(serviceConfig.getTopicUsers(), messagePersistence);
        } catch (Exception ex) {
            throw new BussinessException(
                    ExceptionEnum.ERROR_SEND_USER.getCode(),
                    ExceptionEnum.ERROR_SEND_USER.getMessage(),
                    ex.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

        return user;
    }

    private List<PersistenceMessage> createChangePasswordMessage(char operation, UserEntity userEntity) {
        var changePasswordMessage = UserMessage.builder()
                .id(userEntity.getId())
                .entityId(userEntity.getEntityEntity().getId())
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .status(userEntity.getStatus())
                .logCreationUser(userEntity.getLogCreationUser())
                .logUpdateUser(userEntity.getLogUpdateUser())
                .logCreationDate(userEntity.getLogCreationDate())
                .logUpdateDate(userEntity.getLogUpdateDate())
                .logState(userEntity.getLogState())
                .build();

        return Arrays.asList(PersistenceMessage.builder().operation(operation).tableName(TableEnum.USERS.getValue()).message(changePasswordMessage).build());
    }
}
