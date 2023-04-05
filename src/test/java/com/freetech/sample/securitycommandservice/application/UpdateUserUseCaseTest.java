/*package com.freetech.sample.securitycommandservice.application;

import com.freetech.sample.securitycommandservice.application.mappers.UserEntityMapper;
import com.freetech.sample.securitycommandservice.domain.models.Entity;
import com.freetech.sample.securitycommandservice.domain.models.EntityType;
import com.freetech.sample.securitycommandservice.domain.models.User;
import com.freetech.sample.securitycommandservice.infraestructure.adapters.out.entities.EntityEntity;
import com.freetech.sample.securitycommandservice.infraestructure.adapters.out.entities.EntityTypeEntity;
import com.freetech.sample.securitycommandservice.infraestructure.adapters.out.entities.UserEntity;
import com.freetech.sample.securitycommandservice.infraestructure.ports.out.EntityRepository;
import com.freetech.sample.securitycommandservice.infraestructure.ports.out.EventMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class UpdateUserUseCaseTest {

    private AutoCloseable closeable;

    @Mock
    private EntityRepository entityRepository;

    @Mock
    private EventMessage eventMessage;

    @InjectMocks
    private UpdateUserUseCase updateUserUseCase;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }

    @Test
    void updateUser() {
        var userMock = new User();
        userMock.setId(1);
        userMock.setEntity(new Entity());
        userMock.getEntity().setId(1);
        userMock.getEntity().setEntityType(new EntityType());
        userMock.getEntity().getEntityType().setId(4);
        userMock.getEntity().setName("Alejandra");
        userMock.getEntity().setLastname("palacios");
        userMock.setUsername("spalacios");
        userMock.setPassword("micronics");

        var userEntity = UserEntityMapper.toEntity(userMock);
        userEntity.setLogState(1);
        userEntity.getEntityEntity().setLogState(1);
        userEntity.getEntityEntity().getEntityType().setLogState(1);

        when(entityRepository.getById(anyInt(), eq(UserEntity.class))).thenReturn(userEntity);
        when(entityRepository.getById(anyInt(), eq(EntityEntity.class))).thenReturn(userEntity.getEntityEntity());
        when(entityRepository.getById(anyInt(), eq(EntityTypeEntity.class))).thenReturn(userEntity.getEntityEntity().getEntityType());
        when(entityRepository.update(any(EntityEntity.class))).thenReturn(userEntity.getEntityEntity());
        when(entityRepository.update(any(UserEntity.class))).thenReturn(userEntity);
        when(eventMessage.sendMessage(anyString())).thenReturn(true);

        assertNotNull(updateUserUseCase.updateUser(userMock));
    }
}*/