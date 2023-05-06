package com.freetech.sample.securitycommandservice.application;

import com.freetech.sample.securitycommandservice.application.config.ServiceConfig;
import com.freetech.sample.securitycommandservice.application.enums.ExceptionEnum;
import com.freetech.sample.securitycommandservice.application.exceptions.BussinessException;
import com.freetech.sample.securitycommandservice.application.validations.EntityTypeValidation;
import com.freetech.sample.securitycommandservice.domain.models.EntityType;
import com.freetech.sample.securitycommandservice.infraestructure.adapters.out.entities.EntityTypeEntity;
import com.freetech.sample.securitycommandservice.infraestructure.ports.in.UpdateEntityTypePort;
import com.freetech.sample.securitycommandservice.infraestructure.ports.out.EntityRepository;
import com.freetech.sample.securitycommandservice.infraestructure.ports.out.EventMessage;
import enums.OperationEnum;
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
public class UpdateEntityTypeUseCase implements UpdateEntityTypePort {
    private final EventMessage eventMessage;
    private final EntityRepository entityRepository;
    private final ServiceConfig serviceConfig;
    private final EntityTypeValidation entityTypeValidation;

    @Transactional
    @Override
    public EntityType updateEntityType(EntityType entityType) {
        entityTypeValidation.validateUpdateEntityType(entityType);

        String messagePersistence = "";
        try {
            var entityTypeEntity = entityRepository.getByField(
                    "id", entityType.getId(), EntityTypeEntity.class
            );

            if (entityType.getName() != null) {
                entityTypeEntity.setName(entityType.getName());
            }
            if (entityType.getDescription() != null) {
                entityTypeEntity.setDescription(entityType.getDescription());
            }

            entityTypeEntity.setLogUpdateUser(entityType.getLogUsername());
            entityTypeEntity.setLogUpdateDate(DateUtil.getDate());
            entityTypeEntity = entityRepository.update(entityTypeEntity);

            messagePersistence = JsonUtil.convertoToJson(createUpdateEntityTypeMessage(OperationEnum.UPDATE.getValue(),
                    entityTypeEntity));
        } catch (Exception ex) {
            throw new BussinessException(
                    ExceptionEnum.ERROR_UPDATE_ENTITY_TYPE.getCode(),
                    ExceptionEnum.ERROR_UPDATE_ENTITY_TYPE.getMessage(),
                    ex.getMessage() + " --> " + ex.getCause().getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            eventMessage.sendMessage(serviceConfig.getTopicEntityTypes(), messagePersistence);
        } catch (Exception ex) {
            throw new BussinessException(
                    ExceptionEnum.ERROR_SEND_ENTITY_TYPE.getCode(),
                    ExceptionEnum.ERROR_SEND_ENTITY_TYPE.getMessage(),
                    ex.getMessage() + " --> " + ex.getCause().getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

        return entityType;
    }

    private List<PersistenceMessage> createUpdateEntityTypeMessage(char operation, EntityTypeEntity entityTypeEntity) {
        var updateEntityTypeMessage = EntityTypeMessage.builder()
                .id(entityTypeEntity.getId())
                .name(entityTypeEntity.getName())
                .description(entityTypeEntity.getDescription())
                .logUpdateUser(entityTypeEntity.getLogUpdateUser())
                .logUpdateDate(entityTypeEntity.getLogUpdateDate())
                .build();

        return Arrays.asList(PersistenceMessage.builder().operation(operation).tableName(TableEnum.ENTITY_TYPES.getValue()).message(updateEntityTypeMessage).build());
    }
}
