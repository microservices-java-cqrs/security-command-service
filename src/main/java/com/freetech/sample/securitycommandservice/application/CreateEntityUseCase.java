package com.freetech.sample.securitycommandservice.application;

import com.freetech.sample.securitycommandservice.application.config.ServiceConfig;
import com.freetech.sample.securitycommandservice.application.enums.ExceptionEnum;
import com.freetech.sample.securitycommandservice.application.exceptions.BussinessException;
import com.freetech.sample.securitycommandservice.application.mappers.EntityEntityMapper;
import com.freetech.sample.securitycommandservice.application.messages.NewEntityMessage;
import com.freetech.sample.securitycommandservice.application.validations.EntityValidation;
import com.freetech.sample.securitycommandservice.domain.models.Entity;
import com.freetech.sample.securitycommandservice.infraestructure.adapters.out.entities.EntityEntity;
import com.freetech.sample.securitycommandservice.infraestructure.ports.in.CreateEntityPort;
import com.freetech.sample.securitycommandservice.infraestructure.ports.out.EntityRepository;
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
public class CreateEntityUseCase implements CreateEntityPort {
    private final EntityRepository entityRepository;
    private final ServiceConfig serviceConfig;
    private final EntityValidation entityValidation;

    @Transactional
    @Override
    public Entity createEntity(Entity entity) {
        entityValidation.validateCreateEntity(entity);

        String messagePersistence = "";
        try {
            var entityEntity = EntityEntityMapper.toEntity(entity);
            entityEntity.setLogCreationUser(entity.getLogUsername());
            entityEntity.setLogUpdateUser(entityEntity.getLogCreationUser());

            entityRepository.save(entityEntity);

            messagePersistence = JsonUtil.convertoToJson(createNewEntityMessage(OperationEnum.CREATE.getValue(),
                    entityEntity));
        } catch(Exception ex) {
            throw new BussinessException(
                    ExceptionEnum.ERROR_CREATE_ENTITY.getCode(),
                    ExceptionEnum.ERROR_CREATE_ENTITY.getMessage(),
                    ex.getMessage() + " --> " + ex.getCause().getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
        return entity;
    }

    private MessagePersistence createNewEntityMessage(char operation, EntityEntity entityEntity) {
        var newEntityMessage = NewEntityMessage.builder().build();
        return MessagePersistence.builder().operation(operation).tableName(TableEnum.ENTITIES.getValue()).message(newEntityMessage).build();
    }
}
