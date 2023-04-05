package com.freetech.sample.securitycommandservice.infraestructure.adapters.in.http.controllers;

import com.freetech.sample.securitycommandservice.domain.models.EntityType;
import com.freetech.sample.securitycommandservice.infraestructure.adapters.in.http.dtos.NewEntityTypeDto;
import com.freetech.sample.securitycommandservice.infraestructure.adapters.in.http.dtos.UpdateEntityTypeDto;
import com.freetech.sample.securitycommandservice.infraestructure.adapters.in.http.mappers.EntityTypeMapper;
import com.freetech.sample.securitycommandservice.infraestructure.ports.in.CreateEntityTypePort;
import com.freetech.sample.securitycommandservice.infraestructure.ports.in.DeleteEntityTypePort;
import com.freetech.sample.securitycommandservice.infraestructure.ports.in.UpdateEntityTypePort;
import interfaces.HttpAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "v1/cmd/entity-types")
@HttpAdapter
public class CommandEntityTypeController {
    private final CreateEntityTypePort createEntityTypePort;
    private final UpdateEntityTypePort updateEntityTypePort;
    private final DeleteEntityTypePort deleteEntityTypePort;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<NewEntityTypeDto> createEntityType(@RequestBody NewEntityTypeDto newEntityTypeDto) {
        var entityType = EntityTypeMapper.toDomain(newEntityTypeDto);
        entityType.setLogUsername("USER");
        return new ResponseEntity<>(EntityTypeMapper.toDto(createEntityTypePort.createEntityType(entityType), NewEntityTypeDto.class), HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    public ResponseEntity<UpdateEntityTypeDto> updateEntityType(@RequestBody UpdateEntityTypeDto updateEntityTypeDto) {
        var entityType = EntityTypeMapper.toDomain(updateEntityTypeDto);
        entityType.setLogUsername("USER");
        return new ResponseEntity<>(EntityTypeMapper.toDto(updateEntityTypePort.updateEntityType(entityType), UpdateEntityTypeDto.class), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Long> deleteEntityType(@PathVariable("id") Long id) {
        var entityType = new EntityType();
        entityType.setId(id);
        entityType.setLogUsername("USER");
        return new ResponseEntity<>(deleteEntityTypePort.deleteEntityType(entityType).getId(), HttpStatus.OK);
    }
}