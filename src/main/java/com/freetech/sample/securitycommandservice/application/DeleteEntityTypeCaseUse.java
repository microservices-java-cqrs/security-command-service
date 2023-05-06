package com.freetech.sample.securitycommandservice.application;

import com.freetech.sample.securitycommandservice.application.config.ServiceConfig;
import com.freetech.sample.securitycommandservice.application.enums.ExceptionEnum;
import com.freetech.sample.securitycommandservice.application.exceptions.BussinessException;
import com.freetech.sample.securitycommandservice.application.validations.EntityTypeValidation;
import com.freetech.sample.securitycommandservice.domain.models.EntityType;
import com.freetech.sample.securitycommandservice.infraestructure.adapters.out.entities.EntityTypeEntity;
import com.freetech.sample.securitycommandservice.infraestructure.ports.in.DeleteEntityTypePort;
import com.freetech.sample.securitycommandservice.infraestructure.ports.out.EntityRepository;
import com.freetech.sample.securitycommandservice.infraestructure.ports.out.EventMessage;
import enums.OperationEnum;
import enums.StateEnum;
import enums.TableEnum;
import interfaces.UseCase;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import messages.EntityTypeMessage;
import messages.PersistenceMessage;
import org.springframework.http.HttpStatus;
import utils.DateUtil;
import utils.JsonUtil;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@UseCase
public class DeleteEntityTypeCaseUse implements DeleteEntityTypePort {
    private final EventMessage eventMessage;
    private final EntityRepository entityRepository;
    private final ServiceConfig serviceConfig;
    private final EntityTypeValidation entityTypeValidation;

    @Transactional
    @Override
    public EntityType deleteEntityType(EntityType entityType) {
        entityTypeValidation.validateDeleteEntityType(entityType);

        var messagePersistence = "";
        try {
            var entityTypeEntity = entityRepository.getByField(
                    "id", entityType.getId(), EntityTypeEntity.class
            );

            entityTypeEntity.setLogUpdateUser(entityType.getLogUsername());
            entityTypeEntity.setLogUpdateDate(DateUtil.getDate());
            entityTypeEntity.setLogState(StateEnum.DELETE.getValue());
            entityTypeEntity = entityRepository.update(entityTypeEntity);

            messagePersistence = JsonUtil.convertoToJson(createDeleteEntityTypeMessage(OperationEnum.DELETE.getValue(),
                    entityTypeEntity));
        } catch (Exception ex) {
            throw new BussinessException(
                    ExceptionEnum.ERROR_DELETE_ENTITY_TYPE.getCode(),
                    ExceptionEnum.ERROR_DELETE_ENTITY_TYPE.getMessage(),
                    ex.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

        try {
            eventMessage.sendMessage(serviceConfig.getTopicUsers(), messagePersistence);
        } catch (Exception ex) {
            throw new BussinessException(
                    ExceptionEnum.ERROR_SEND_ENTITY_TYPE.getCode(),
                    ExceptionEnum.ERROR_SEND_ENTITY_TYPE.getMessage(),
                    ex.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
        return entityType;
    }

    private List<PersistenceMessage> createDeleteEntityTypeMessage(char operation, EntityTypeEntity entityTypeEntity) {
        var deleteEntityTypeMessage = EntityTypeMessage.builder()
                .id(entityTypeEntity.getId())
                .name(entityTypeEntity.getName())
                .description(entityTypeEntity.getDescription())
                .logCreationUser(entityTypeEntity.getLogCreationUser())
                .logUpdateUser(entityTypeEntity.getLogUpdateUser())
                .logCreationDate(entityTypeEntity.getLogCreationDate())
                .logUpdateDate(entityTypeEntity.getLogUpdateDate())
                .logState(entityTypeEntity.getLogState())
                .build();

        return Arrays.asList(PersistenceMessage.builder().operation(operation).tableName(TableEnum.ENTITY_TYPES.getValue()).message(deleteEntityTypeMessage).build());
    }
}
