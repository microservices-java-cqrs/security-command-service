package com.freetech.sample.securitycommandservice.application;

import com.freetech.sample.securitycommandservice.application.config.ServiceConfig;
import com.freetech.sample.securitycommandservice.application.enums.ExceptionEnum;
import com.freetech.sample.securitycommandservice.application.mappers.EntityEntityMapper;
import com.freetech.sample.securitycommandservice.application.mappers.UserEntityMapper;
import com.freetech.sample.securitycommandservice.application.messages.NewUserMessage;
import com.freetech.sample.securitycommandservice.application.exceptions.BussinessException;
import com.freetech.sample.securitycommandservice.application.validations.UserValidation;
import com.freetech.sample.securitycommandservice.domain.models.User;
import com.freetech.sample.securitycommandservice.infraestructure.ports.in.CreateUserPort;
import com.freetech.sample.securitycommandservice.infraestructure.ports.out.EntityRepository;
import com.freetech.sample.securitycommandservice.infraestructure.ports.out.EventMessage;
import com.freetech.sample.securitycommandservice.infraestructure.adapters.out.entities.EntityEntity;
import com.freetech.sample.securitycommandservice.infraestructure.adapters.out.entities.UserEntity;
import crypto.Cryptography;
import enums.OperationEnum;
import enums.TableEnum;
import interfaces.UseCase;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import messages.MessagePersistence;
import org.springframework.http.HttpStatus;
import utils.JsonUtil;

@RequiredArgsConstructor
@UseCase
public class CreateUserUseCase implements CreateUserPort {

    private final EventMessage eventMessage;
    private final EntityRepository entityRepository;
    private final ServiceConfig serviceConfig;
    private final UserValidation userValidation;

    @Transactional
    @Override
    public User createUser(User user) {
        userValidation.validateCreateUser(user);

        String messagePersistence = "";
        try {
            var entityEntity = EntityEntityMapper.toEntity(user.getEntity());
            entityEntity.setLogCreationUser(user.getLogUsername());
            entityEntity.setLogUpdateUser(entityEntity.getLogCreationUser());

            entityEntity = entityRepository.save(entityEntity);

            var userEntity = UserEntityMapper.toEntity(user);
            userEntity.setPassword(
                    Cryptography.encrypt(userEntity.getPassword(), serviceConfig.getCryptoSecretKey())
            );
            userEntity.setEntityEntity(entityEntity);
            userEntity.setLogCreationUser(user.getLogUsername());
            userEntity.setLogUpdateUser(userEntity.getLogCreationUser());

            userEntity = entityRepository.save(userEntity);
            user.setId(userEntity.getId());
            user.setPassword(userEntity.getPassword());

            messagePersistence = JsonUtil.convertoToJson(createNewUserMessage(OperationEnum.CREATE.getValue(),
                    entityEntity, userEntity));
        } catch(Exception ex) {
            throw new BussinessException(
                    ExceptionEnum.ERROR_CREATE_USER.getCode(),
                    ExceptionEnum.ERROR_CREATE_USER.getMessage(),
                    ex.getMessage() + " --> " + ex.getCause().getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
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

    private MessagePersistence createNewUserMessage(char operation, EntityEntity entityEntity, UserEntity userEntity) {
        var newUserMessage = NewUserMessage.builder()
                .id(userEntity.getId())
                .entityTypeId(entityEntity.getEntityTypeEntity().getId())
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .status(userEntity.getStatus())
                .logCreationUser(userEntity.getLogCreationUser())
                .logUpdateUser(userEntity.getLogUpdateUser())
                .logCreationDate(userEntity.getLogCreationDate())
                .logUpdateDate(userEntity.getLogUpdateDate())
                .logState(userEntity.getLogState())
                .entityId(entityEntity.getId())
                .entityParentId(entityEntity.getEntityEntity() != null ? entityEntity.getEntityEntity().getId() : null)
                .entityNumberDocument(entityEntity.getNumberDocument())
                .entityBussinessName(entityEntity.getBussinessName())
                .entityName(entityEntity.getName())
                .entityLastname(entityEntity.getLastname())
                .entityLogCreationUser(entityEntity.getLogCreationUser())
                .entityLogUpdateUser(entityEntity.getLogUpdateUser())
                .entityLogCreationDate(entityEntity.getLogCreationDate())
                .entityLogUpdateDate(entityEntity.getLogUpdateDate())
                .entityLogState(entityEntity.getLogState())
                .build();

        return MessagePersistence.builder().operation(operation).tableName(TableEnum.USERS.getValue()).message(newUserMessage).build();
    }

}
