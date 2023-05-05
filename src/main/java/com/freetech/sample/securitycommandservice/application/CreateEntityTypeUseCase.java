package com.freetech.sample.securitycommandservice.application;

import com.freetech.sample.securitycommandservice.application.config.ServiceConfig;
import com.freetech.sample.securitycommandservice.application.enums.ExceptionEnum;
import com.freetech.sample.securitycommandservice.application.exceptions.BussinessException;
import com.freetech.sample.securitycommandservice.application.mappers.EntityTypeEntityMapper;
import com.freetech.sample.securitycommandservice.application.validations.EntityTypeValidation;
import com.freetech.sample.securitycommandservice.domain.models.EntityType;
import com.freetech.sample.securitycommandservice.infraestructure.adapters.out.entities.EntityTypeEntity;
import com.freetech.sample.securitycommandservice.infraestructure.ports.in.CreateEntityTypePort;
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
import utils.JsonUtil;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@UseCase
public class CreateEntityTypeUseCase implements CreateEntityTypePort {

    private final EventMessage eventMessage;
    private final EntityRepository entityRepository;
    private final ServiceConfig serviceConfig;
    private final EntityTypeValidation entityTypeValidation;

    @Transactional
    @Override
    public EntityType createEntityType(EntityType entityType) {
        entityTypeValidation.validateCreateEntityType(entityType);

        String messagePersistence = "";
        try {
            var entityTypeEntity = EntityTypeEntityMapper.toEntity(entityType);
            entityTypeEntity.setLogCreationUser(entityType.getLogUsername());
            entityTypeEntity.setLogUpdateUser(entityTypeEntity.getLogCreationUser());

            entityTypeEntity = entityRepository.save(entityTypeEntity);
            entityType.setId(entityTypeEntity.getId());

            messagePersistence = JsonUtil.convertoToJson(createNewEntityTypeMessage(OperationEnum.CREATE.getValue(),
                    entityTypeEntity));
        } catch (Exception ex) {
            throw new BussinessException(
                    ExceptionEnum.ERROR_CREATE_ENTITY_TYPE.getCode(),
                    ExceptionEnum.ERROR_CREATE_ENTITY_TYPE.getMessage(),
                    ex.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            eventMessage.sendMessage(serviceConfig.getTopicEntityTypes(), messagePersistence);
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

    private List<PersistenceMessage> createNewEntityTypeMessage(char operation, EntityTypeEntity entityTypeEntity) {
        var newEntityTypeMessage = EntityTypeMessage.builder()
                .id(entityTypeEntity.getId())
                .name(entityTypeEntity.getName())
                .description(entityTypeEntity.getDescription())
                .logCreationUser(entityTypeEntity.getLogCreationUser())
                .logCreationDate(entityTypeEntity.getLogCreationDate())
                .logUpdateUser(entityTypeEntity.getLogUpdateUser())
                .logUpdateDate(entityTypeEntity.getLogUpdateDate())
                .logState(entityTypeEntity.getLogState())
                .build();

        return Arrays.asList(PersistenceMessage.builder().operation(operation).tableName(TableEnum.ENTITY_TYPES.getValue()).message(newEntityTypeMessage).build());
    }
}
