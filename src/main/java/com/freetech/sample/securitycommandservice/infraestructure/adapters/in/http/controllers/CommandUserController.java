package com.freetech.sample.securitycommandservice.infraestructure.adapters.in.http.controllers;

import com.freetech.sample.securitycommandservice.domain.models.Rol;
import com.freetech.sample.securitycommandservice.domain.models.User;
import com.freetech.sample.securitycommandservice.domain.models.UserRol;
import com.freetech.sample.securitycommandservice.infraestructure.adapters.in.http.dtos.AddRolesToUserDto;
import com.freetech.sample.securitycommandservice.infraestructure.adapters.in.http.dtos.ChangePasswordDto;
import com.freetech.sample.securitycommandservice.infraestructure.adapters.in.http.dtos.NewUserDto;
import com.freetech.sample.securitycommandservice.infraestructure.adapters.in.http.dtos.UpdateUserDto;
import com.freetech.sample.securitycommandservice.infraestructure.adapters.in.http.mappers.UserMapper;
import com.freetech.sample.securitycommandservice.infraestructure.ports.in.*;
import interfaces.HttpAdapter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "v1/cmd/users")
@HttpAdapter
public class CommandUserController {
    private final CreateUserPort createUserPort;
    private final UpdateUserPort updateUserPort;
    private final DeleteUserPort deleteUserPort;
    private final ChangePasswordPort changePasswordPort;
    private final AddRolesToUserPort addRolesToUserPort;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<NewUserDto> createUser(@RequestBody @Valid NewUserDto newUserDto) {
        var user = UserMapper.toDomain(newUserDto);
        user.setLogUsername("USER");
        return new ResponseEntity<>(UserMapper.toDto(createUserPort.createUser(user), NewUserDto.class), HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    public ResponseEntity<UpdateUserDto> updateUser(@RequestBody @Valid UpdateUserDto updateUserDto) {
        var user = UserMapper.toDomain(updateUserDto);
        user.setLogUsername("USER");
        return new ResponseEntity<>(UserMapper.toDto(updateUserPort.updateUser(user), UpdateUserDto.class), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Long> deleteUser(@PathVariable("id") Long id) {
        var user = new User();
        user.setId(id);
        user.setLogUsername("USER");
        return new ResponseEntity<>(deleteUserPort.deleteUser(user).getId(), HttpStatus.OK);
    }

    @RequestMapping(value = "/new-password", method = RequestMethod.PUT)
    public ResponseEntity changePassword(@RequestBody @Valid ChangePasswordDto changePasswordDto) {
        var user = UserMapper.toDomain(changePasswordDto);
        user.setLogUsername("lamat@gmail.com");
        changePasswordPort.changePassword(user);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PostMapping("/{id}/roles")
    public ResponseEntity addRolesToUser(@PathVariable("id") Long id, @RequestBody AddRolesToUserDto addRolesToUserDto) {
        var userRoles = addRolesToUserDto.getRoles().stream().map( idRol -> {
            var userRol = new UserRol();
            userRol.setUser(new User());
            userRol.getUser().setId(id);
            userRol.setRol(new Rol());
            userRol.getRol().setId(idRol);
            return userRol;
        }).collect(Collectors.toList());
        addRolesToUserPort.addRolesToUser(id, userRoles);
        return new ResponseEntity(null, HttpStatus.CREATED);
    }
}