package com.freetech.sample.securitycommandservice.infraestructure.adapters.in.http.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class NewRolDto {

    @NotEmpty(message = "{validation.dto.rol.name.no_empty}")
    @NotNull(message = "{validation.dto.rol.name.no_empty}")
    private String name;

    @NotEmpty(message = "{validation.dto.rol.description.no_empty}")
    @NotNull(message = "{validation.dto.rol.description.no_empty}")
    private String description;
}
