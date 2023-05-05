package com.freetech.sample.securitycommandservice.infraestructure.adapters.in.http.controllers;

import com.freetech.sample.securitycommandservice.infraestructure.adapters.in.http.dtos.NewRolDto;
import com.freetech.sample.securitycommandservice.infraestructure.adapters.in.http.mappers.RolMapper;
import com.freetech.sample.securitycommandservice.infraestructure.ports.in.CreateRolPort;
import interfaces.HttpAdapter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "v1/cmd/roles")
@HttpAdapter
public class CommandRolController {
    private final CreateRolPort createRolPort;

    @PostMapping("")
    public ResponseEntity<NewRolDto> createRol(@RequestBody @Valid NewRolDto newRolDto) {
        var rol = RolMapper.toDomain(newRolDto);
        rol.setLogUsername("USER");
        return new ResponseEntity<>(RolMapper.toDto(createRolPort.createRol(rol), NewRolDto.class), HttpStatus.CREATED);
    }
}
