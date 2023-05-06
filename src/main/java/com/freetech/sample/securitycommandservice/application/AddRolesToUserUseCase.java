package com.freetech.sample.securitycommandservice.application;

import com.freetech.sample.securitycommandservice.application.config.ServiceConfig;
import com.freetech.sample.securitycommandservice.application.enums.ExceptionEnum;
import com.freetech.sample.securitycommandservice.application.exceptions.BussinessException;
import com.freetech.sample.securitycommandservice.application.mappers.UserRolEntityMapper;
import com.freetech.sample.securitycommandservice.application.validations.UserValidation;
import com.freetech.sample.securitycommandservice.domain.models.UserRol;
import com.freetech.sample.securitycommandservice.infraestructure.adapters.out.entities.UserRolEntity;
import com.freetech.sample.securitycommandservice.infraestructure.ports.in.AddRolesToUserPort;
import com.freetech.sample.securitycommandservice.infraestructure.ports.out.EntityRepository;
import com.freetech.sample.securitycommandservice.infraestructure.ports.out.EventMessage;
import enums.OperationEnum;
import enums.TableEnum;
import interfaces.UseCase;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import messages.PersistenceMessage;
import messages.UserRolMessage;
import org.springframework.http.HttpStatus;
import utils.JsonUtil;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@UseCase
public class AddRolesToUserUseCase implements AddRolesToUserPort {
    private final EntityRepository entityRepository;
    private final EventMessage eventMessage;
    private final ServiceConfig serviceConfig;
    private final UserValidation userValidation;

    @Transactional
    @Override
    public void addRolesToUser(Long id, List<UserRol> userRoles) {
        userValidation.validateAddRolesToUser(id);

        String messagePersistence = "";
        try {
            List<PersistenceMessage> listPersistenceMessage = new ArrayList<>();
            for (var userRol : userRoles) {
                var userRolEntity = UserRolEntityMapper.toEntity(userRol);
                userRolEntity = entityRepository.save(userRolEntity);
                listPersistenceMessage.add(createNewUserRolMessage(OperationEnum.CREATE.getValue(), userRolEntity));
            }
            messagePersistence = JsonUtil.convertoToJson(listPersistenceMessage);
        } catch (Exception ex) {
            throw new BussinessException(
                    ExceptionEnum.ERROR_ADD_ROLES_TO_USER.getCode(),
                    ExceptionEnum.ERROR_ADD_ROLES_TO_USER.getMessage(),
                    ex.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

        try {
            eventMessage.sendMessage(serviceConfig.getTopicUsers(), messagePersistence);
        } catch (Exception ex) {
            throw new BussinessException(
                    ExceptionEnum.ERROR_SEND_ROLES_TO_USER.getCode(),
                    ExceptionEnum.ERROR_SEND_ROLES_TO_USER.getMessage(),
                    ex.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    private PersistenceMessage createNewUserRolMessage(char operation, UserRolEntity userRolEntity) {
        var newUserRolMessage = UserRolMessage.builder()
                .id(userRolEntity.getId())
                .userId(userRolEntity.getUserEntity().getId())
                .rolId(userRolEntity.getRolEntity().getId())
                .build();

        return PersistenceMessage.builder()
                .operation(operation)
                .tableName(TableEnum.USERS_ROLES.getValue())
                .message(newUserRolMessage).build();
    }
}
