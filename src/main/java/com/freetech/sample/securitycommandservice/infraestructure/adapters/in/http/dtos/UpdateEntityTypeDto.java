package com.freetech.sample.securitycommandservice.infraestructure.adapters.in.http.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class UpdateEntityTypeDto {
    @NotEmpty(message = "{validation.dto.entity_type.id.no_empty}")
    @NotNull(message = "{validation.dto.entity_type.id.no_empty}")
    private Long id;

    @NotEmpty(message = "{validation.dto.entity_type.name.no_empty}")
    @NotNull(message = "{validation.dto.entity_type.name.no_empty}")
    private String name;

    @NotEmpty(message = "{validation.dto.entity_type.description.no_empty}")
    @NotNull(message = "{validation.dto.entity_type.description.no_empty}")
    private String description;
}
