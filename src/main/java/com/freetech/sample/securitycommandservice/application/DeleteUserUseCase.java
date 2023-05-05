package com.freetech.sample.securitycommandservice.application;

import com.freetech.sample.securitycommandservice.application.config.ServiceConfig;
import com.freetech.sample.securitycommandservice.application.enums.ExceptionEnum;
import com.freetech.sample.securitycommandservice.application.exceptions.BussinessException;
import com.freetech.sample.securitycommandservice.application.messages.DeleteUserMessage;
import com.freetech.sample.securitycommandservice.application.validations.UserValidation;
import com.freetech.sample.securitycommandservice.domain.models.User;
import com.freetech.sample.securitycommandservice.infraestructure.adapters.out.entities.EntityEntity;
import com.freetech.sample.securitycommandservice.infraestructure.adapters.out.entities.UserEntity;
import com.freetech.sample.securitycommandservice.infraestructure.ports.in.DeleteUserPort;
import com.freetech.sample.securitycommandservice.infraestructure.ports.out.EntityRepository;
import com.freetech.sample.securitycommandservice.infraestructure.ports.out.EventMessage;
import enums.OperationEnum;
import enums.StateEnum;
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
public class DeleteUserUseCase implements DeleteUserPort {

    private final EventMessage eventMessage;
    private final EntityRepository entityRepository;
    private final ServiceConfig serviceConfig;
    private final UserValidation userValidation;

    @Transactional
    @Override
    public User deleteUser(User user) {
        userValidation.validateDeleteUser(user);

        String messagePersistence = "";
        try {
            var userEntity = entityRepository.getByField(
                    "id", user.getId(), UserEntity.class);

            var entityEntity = entityRepository.getByField(
                    "id", userEntity.getEntityEntity().getId(),
                    EntityEntity.class);

            entityEntity.setLogUpdateUser(user.getLogUsername());
            entityEntity.setLogUpdateDate(DateUtil.getDate());
            entityEntity.setLogState(StateEnum.DELETE.getValue());
            entityEntity = entityRepository.update(entityEntity);

            userEntity.setLogUpdateUser(user.getLogUsername());
            userEntity.setLogUpdateDate(DateUtil.getDate());
            userEntity.setLogState(StateEnum.DELETE.getValue());
            userEntity = entityRepository.update(userEntity);

            messagePersistence = JsonUtil.convertoToJson(createDeleteUserMessage(OperationEnum.DELETE.getValue(),
                    entityEntity, userEntity));
        } catch (Exception ex) {
            throw new BussinessException(
                    ExceptionEnum.ERROR_DELETE_USER.getCode(),
                    ExceptionEnum.ERROR_DELETE_USER.getMessage(),
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

    private PersistenceMessage createDeleteUserMessage(char operation, EntityEntity entityEntity, UserEntity userEntity) {
        var deleteUserMessage = DeleteUserMessage.builder()
                .id(userEntity.getId())
                .logUpdateUser(userEntity.getLogUpdateUser())
                .logUpdateDate(userEntity.getLogUpdateDate())
                .logState(userEntity.getLogState())
                .entityLogUpdateUser(entityEntity.getLogUpdateUser())
                .entityLogUpdateDate(entityEntity.getLogUpdateDate())
                .entityLogState(entityEntity.getLogState())
                .build();

        return PersistenceMessage.builder().operation(operation).tableName(TableEnum.USERS.getValue()).message(deleteUserMessage).build();
    }
}
