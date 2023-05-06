package com.freetech.sample.securitycommandservice.application;

import com.freetech.sample.securitycommandservice.application.config.ServiceConfig;
import com.freetech.sample.securitycommandservice.application.enums.ExceptionEnum;
import com.freetech.sample.securitycommandservice.application.exceptions.BussinessException;
import com.freetech.sample.securitycommandservice.application.validations.UserValidation;
import com.freetech.sample.securitycommandservice.domain.models.User;
import com.freetech.sample.securitycommandservice.infraestructure.adapters.out.entities.EntityEntity;
import com.freetech.sample.securitycommandservice.infraestructure.adapters.out.entities.EntityTypeEntity;
import com.freetech.sample.securitycommandservice.infraestructure.adapters.out.entities.UserEntity;
import com.freetech.sample.securitycommandservice.infraestructure.ports.in.UpdateUserPort;
import com.freetech.sample.securitycommandservice.infraestructure.ports.out.EntityRepository;
import com.freetech.sample.securitycommandservice.infraestructure.ports.out.EventMessage;
import enums.OperationEnum;
import enums.TableEnum;
import interfaces.UseCase;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import messages.EntityMessage;
import messages.PersistenceMessage;
import messages.UserMessage;
import org.springframework.http.HttpStatus;
import utils.DateUtil;
import utils.JsonUtil;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@UseCase
public class UpdateUserUseCase implements UpdateUserPort {

    private final EventMessage eventMessage;
    private final EntityRepository entityRepository;
    private final ServiceConfig serviceConfig;
    private final UserValidation userValidation;

    @Transactional
    @Override
    public User updateUser(User user) {
        userValidation.validateUpdateUser(user);

        String messagePersistence = "";
        try {
            var userEntity = entityRepository.getByField(
                    "id", user.getId(), UserEntity.class);

            var entityEntity = entityRepository.getByField(
                    "id", userEntity.getEntityEntity().getId(),
                    EntityEntity.class);

            if (user.getEntity() != null) {
                if (user.getEntity().getEntityType().getId() != null ) {
                    entityEntity.setEntityTypeEntity(new EntityTypeEntity());
                    entityEntity.getEntityTypeEntity().setId(user.getEntity().getEntityType().getId());
                }
                if (user.getEntity().getNumberDocument() != null) {
                    entityEntity.setNumberDocument(user.getEntity().getNumberDocument());
                }
                if (user.getEntity().getBussinessName() != null) {
                    entityEntity.setBussinessName(user.getEntity().getBussinessName());
                }
                if (user.getEntity().getName() != null) {
                    entityEntity.setName(user.getEntity().getName());
                }
                if (user.getEntity().getLastname() != null) {
                    entityEntity.setLastname(user.getEntity().getLastname());
                }
                entityEntity.setLogUpdateUser(user.getLogUsername());
                entityEntity.setLogUpdateDate(DateUtil.getDate());
                entityEntity = entityRepository.update(entityEntity);
            }

            if (user.getUsername() != null) {
                userEntity.setUsername(user.getUsername());
            }
            if (user.getStatus() != null) {
                userEntity.setStatus(user.getStatus());
            }
            userEntity.setLogUpdateUser(user.getLogUsername());
            userEntity.setLogUpdateDate(DateUtil.getDate());
            userEntity = entityRepository.update(userEntity);

            messagePersistence = JsonUtil.convertoToJson(createUpdateUserMessage(OperationEnum.UPDATE.getValue(),
                    entityEntity, userEntity));
        } catch(Exception ex) {
            throw new BussinessException(
                    ExceptionEnum.ERROR_UPDATE_USER.getCode(),
                    ExceptionEnum.ERROR_UPDATE_USER.getMessage(),
                    ex.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

        try {
            eventMessage.sendMessage(serviceConfig.getTopicUsers(), messagePersistence);
        } catch(Exception ex) {
            throw new BussinessException(
                    ExceptionEnum.ERROR_SEND_USER.getCode(),
                    ExceptionEnum.ERROR_SEND_USER.getMessage(),
                    ex.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

        return user;
    }

    private List<PersistenceMessage> createUpdateUserMessage(char operation, EntityEntity entityEntity, UserEntity userEntity) {
        var updateUserMessage = UserMessage.builder()
                .id(userEntity.getId())
                .entityId(entityEntity.getId())
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .status(userEntity.getStatus())
                .logCreationUser(userEntity.getLogCreationUser())
                .logUpdateUser(userEntity.getLogUpdateUser())
                .logCreationDate(userEntity.getLogCreationDate())
                .logUpdateDate(userEntity.getLogUpdateDate())
                .logState(userEntity.getLogState())
                .build();

        var updateEntityMessage = EntityMessage.builder()
                .id(entityEntity.getId())
                .parentId(entityEntity.getEntityEntity() != null ? entityEntity.getEntityEntity().getId() : null)
                .entityTypeId(entityEntity.getEntityTypeEntity().getId())
                .numberDocument(entityEntity.getNumberDocument())
                .bussinessName(entityEntity.getBussinessName())
                .name(entityEntity.getName())
                .lastname(entityEntity.getLastname())
                .logCreationUser(entityEntity.getLogCreationUser())
                .logUpdateUser(entityEntity.getLogUpdateUser())
                .logCreationDate(entityEntity.getLogCreationDate())
                .logUpdateDate(entityEntity.getLogUpdateDate())
                .logState(entityEntity.getLogState())
                .build();

        return Arrays.asList(
                PersistenceMessage.builder().operation(operation).tableName(TableEnum.ENTITIES.getValue()).message(updateEntityMessage).build(),
                PersistenceMessage.builder().operation(operation).tableName(TableEnum.USERS.getValue()).message(updateUserMessage).build()
        );
    }

}
