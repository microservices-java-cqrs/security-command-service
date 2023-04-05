package com.freetech.sample.securitycommandservice.application;

import com.freetech.sample.securitycommandservice.application.config.ServiceConfig;
import com.freetech.sample.securitycommandservice.application.mappers.UserEntityMapper;
import com.freetech.sample.securitycommandservice.domain.models.Entity;
import com.freetech.sample.securitycommandservice.domain.models.EntityType;
import com.freetech.sample.securitycommandservice.domain.models.User;
import com.freetech.sample.securitycommandservice.infraestructure.adapters.out.entities.EntityTypeEntity;
import com.freetech.sample.securitycommandservice.infraestructure.ports.out.EntityRepository;
import com.freetech.sample.securitycommandservice.infraestructure.ports.out.EventMessage;
import com.freetech.sample.securitycommandservice.infraestructure.adapters.out.entities.EntityEntity;
import com.freetech.sample.securitycommandservice.infraestructure.adapters.out.entities.UserEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class CreateUserUseCaseTest {

    private AutoCloseable closeable;

    @Mock
    private EntityRepository entityRepository;

    @Mock
    private EventMessage eventMessage;

    @Spy
    private ServiceConfig serviceConfig;

    @InjectMocks
    private CreateUserUseCase createUserUseCase;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }

    @Test
    void createUser() {
        var userMock = new User();
        userMock.setId(1L);
        userMock.setEntity(new Entity());
        userMock.getEntity().setId(1L);
        userMock.getEntity().setEntityType(new EntityType());
        userMock.getEntity().getEntityType().setId(4L);
        userMock.getEntity().setName("Alejandra");
        userMock.getEntity().setLastname("palacios");
        userMock.setUsername("spalacios");
        userMock.setPassword("micronics");
        userMock.setConfirmPassword("micronics");

        var userEntity = UserEntityMapper.toEntity(userMock);
        userEntity.getEntityEntity().getEntityTypeEntity().setLogState(1);

        when(serviceConfig.getCryptoSecretKey()).thenReturn("sandrasandrasandrasandra");

        when(entityRepository.getByField(anyString(), any(), eq(UserEntity.class))).thenReturn(null);
        when(entityRepository.getByField(anyString(), any(), eq(EntityTypeEntity.class))).thenReturn(userEntity.getEntityEntity().getEntityTypeEntity());

        when(entityRepository.save(any(EntityEntity.class))).thenReturn(userEntity.getEntityEntity());
        when(entityRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(eventMessage.sendMessage(anyString(), anyString())).thenReturn(true);

        assertNotNull(createUserUseCase.createUser(userMock));
    }
}