package com.freetech.sample.securitycommandservice.application;

import com.freetech.sample.securitycommandservice.application.config.ServiceConfig;
import com.freetech.sample.securitycommandservice.application.enums.ExceptionEnum;
import com.freetech.sample.securitycommandservice.application.exceptions.BussinessException;
import com.freetech.sample.securitycommandservice.application.mappers.RolEntityMapper;
import com.freetech.sample.securitycommandservice.domain.models.Rol;
import com.freetech.sample.securitycommandservice.infraestructure.adapters.out.entities.RolEntity;
import com.freetech.sample.securitycommandservice.infraestructure.ports.in.CreateRolPort;
import com.freetech.sample.securitycommandservice.infraestructure.ports.out.EntityRepository;
import com.freetech.sample.securitycommandservice.infraestructure.ports.out.EventMessage;
import enums.OperationEnum;
import enums.TableEnum;
import interfaces.UseCase;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import messages.PersistenceMessage;
import messages.RolMessage;
import org.springframework.http.HttpStatus;
import utils.JsonUtil;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@UseCase
public class CreateRolUseCase implements CreateRolPort {
    private final EntityRepository entityRepository;
    private final EventMessage eventMessage;
    private final ServiceConfig serviceConfig;

    @Transactional
    @Override
    public Rol createRol(Rol rol) {
        var rolEntity = RolEntityMapper.toEntity(rol);
        rolEntity.setLogCreationUser(rol.getLogUsername());
        rolEntity.setLogUpdateUser(rolEntity.getLogCreationUser());

        String messagePersistence = "";
        try {
            rolEntity = entityRepository.save(rolEntity);
            rol.setId(rolEntity.getId());

            messagePersistence = JsonUtil.convertoToJson(createNewRolMessage(OperationEnum.CREATE.getValue(),
                    rolEntity));
        } catch (Exception ex) {
            throw new BussinessException(
                    ExceptionEnum.ERROR_CREATE_ROL.getCode(),
                    ExceptionEnum.ERROR_CREATE_ROL.getMessage(),
                    ex.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

        try {
            eventMessage.sendMessage(serviceConfig.getTopicUsers(), messagePersistence);
        } catch (Exception ex) {
            throw new BussinessException(
                    ExceptionEnum.ERROR_SEND_ROL.getCode(),
                    ExceptionEnum.ERROR_SEND_ROL.getMessage(),
                    ex.getMessage() + " --> " + ex.getCause().getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

        return rol;
    }

    private List<PersistenceMessage> createNewRolMessage(char operation, RolEntity rolEntity) {
        var newRolMessage = RolMessage.builder()
                .id(rolEntity.getId())
                .name(rolEntity.getName())
                .description(rolEntity.getDescription())
                .logCreationUser(rolEntity.getLogCreationUser())
                .logUpdateUser(rolEntity.getLogUpdateUser())
                .logCreationDate(rolEntity.getLogCreationDate())
                .logUpdateDate(rolEntity.getLogUpdateDate())
                .logState(rolEntity.getLogState())
                .build();

        return Arrays.asList(
                PersistenceMessage.builder().operation(operation).tableName(TableEnum.ROLES.getValue()).message(newRolMessage).build());
    }
}
