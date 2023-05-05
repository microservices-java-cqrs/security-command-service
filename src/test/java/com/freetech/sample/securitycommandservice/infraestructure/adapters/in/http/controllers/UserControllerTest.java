package com.freetech.sample.securitycommandservice.infraestructure.adapters.in.http.controllers;

import com.freetech.sample.securitycommandservice.domain.models.User;
import com.freetech.sample.securitycommandservice.infraestructure.adapters.in.http.dtos.NewUserDto;
import com.freetech.sample.securitycommandservice.infraestructure.adapters.in.http.dtos.UpdateUserDto;
import com.freetech.sample.securitycommandservice.infraestructure.adapters.in.http.mappers.UserMapper;
import com.freetech.sample.securitycommandservice.infraestructure.ports.in.CreateUserPort;
import com.freetech.sample.securitycommandservice.infraestructure.ports.in.DeleteUserPort;
import com.freetech.sample.securitycommandservice.infraestructure.ports.in.UpdateUserPort;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UserControllerTest {

    private AutoCloseable closeable;

    @Mock
    private CreateUserPort createUserPort;

    @Mock
    private UpdateUserPort updateUserPort;

    @Mock
    private DeleteUserPort deleteUserPort;

    @InjectMocks
    private CommandUserController userController;

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
        var newUserDtoMock = new NewUserDto();
        newUserDtoMock.setEntityTypeId(4L);
        newUserDtoMock.setEntityName("Fiorella Yahaira");
        newUserDtoMock.setEntityLastname("Gomez Monteagudo");
        newUserDtoMock.setUsername("fgomez");
        newUserDtoMock.setPassword("micronics");
        newUserDtoMock.setStatus("CREATED");

        var user = UserMapper.toDomain(newUserDtoMock);

        when(createUserPort.createUser(any(User.class))).thenReturn(user);
        assertNotNull(userController.createUser(newUserDtoMock));
    }

    @Test
    void updateUser() {
        var updateUserDtoMock = new UpdateUserDto();
        updateUserDtoMock.setEntityTypeId(4L);
        updateUserDtoMock.setEntityName("Fiorella Yahaira");
        updateUserDtoMock.setEntityLastname("Gomez Monteagudo");
        updateUserDtoMock.setUsername("fgomez");
        updateUserDtoMock.setStatus("CREATED");

        var user = UserMapper.toDomain(updateUserDtoMock);

        when(updateUserPort.updateUser(any(User.class))).thenReturn(user);
        assertNotNull(userController.updateUser(updateUserDtoMock));
    }

    @Test
    void deleteUser() {
        var user = new User();
        user.setId(1L);

        when(deleteUserPort.deleteUser(any(User.class))).thenReturn(user);
        assertNotNull(userController.deleteUser(1L));
    }

}