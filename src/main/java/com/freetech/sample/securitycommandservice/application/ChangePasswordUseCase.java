package com.freetech.sample.securitycommandservice.application;

import com.freetech.sample.securitycommandservice.application.config.ServiceConfig;
import com.freetech.sample.securitycommandservice.application.enums.ExceptionEnum;
import com.freetech.sample.securitycommandservice.application.exceptions.BussinessException;
import com.freetech.sample.securitycommandservice.application.messages.ChangePasswordMessage;
import com.freetech.sample.securitycommandservice.application.validations.UserValidation;
import com.freetech.sample.securitycommandservice.domain.models.User;
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
import org.springframework.http.HttpStatus;
import utils.DateUtil;
import utils.JsonUtil;

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
                    ex.getMessage() + " --> " + ex.getCause().getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            eventMessage.sendMessage(serviceConfig.getTopicUsers(), messagePersistence);
        } catch (Exception ex) {
            throw new BussinessException(
                    ExceptionEnum.ERROR_SEND_USER.getCode(),
                    ExceptionEnum.ERROR_SEND_USER.getMessage(),
                    ex.getMessage() + " --> " + ex.getCause().getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

        return user;
    }

    private PersistenceMessage createChangePasswordMessage(char operation, UserEntity userEntity) {
        var changePasswordMessage = ChangePasswordMessage
                .builder()
                .id(userEntity.getId())
                .password(userEntity.getPassword())
                .logUpdateUser(userEntity.getLogUpdateUser())
                .logUpdateDate(userEntity.getLogUpdateDate())
                .build();

        return PersistenceMessage.builder().operation(operation).tableName(TableEnum.USERS.getValue()).message(changePasswordMessage).build();
    }
}
