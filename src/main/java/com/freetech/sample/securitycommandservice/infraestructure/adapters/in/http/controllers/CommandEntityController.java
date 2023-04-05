package com.freetech.sample.securitycommandservice.infraestructure.adapters.in.http.controllers;

import com.freetech.sample.securitycommandservice.infraestructure.adapters.in.http.dtos.NewEntityDto;
import com.freetech.sample.securitycommandservice.infraestructure.adapters.in.http.mappers.EntityMapper;
import com.freetech.sample.securitycommandservice.infraestructure.ports.in.CreateEntityPort;
import interfaces.HttpAdapter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequiredArgsConstructor
@RequestMapping(value = "v1/cmd/entities")
@HttpAdapter
public class CommandEntityController {
    private final CreateEntityPort createEntityPort;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<NewEntityDto> createEntity(@Valid NewEntityDto newEntityDto) {
        var entity = EntityMapper.toDomain(newEntityDto);
        entity.setLogUsername("USER");
        return new ResponseEntity<>(EntityMapper.toDto(createEntityPort.createEntity(entity), NewEntityDto.class), HttpStatus.OK);
    }
}