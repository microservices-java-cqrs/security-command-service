package com.freetech.sample.securitycommandservice.infraestructure.adapters.in.http.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class NewUserDto {
    private Long id;

    @NotNull(message = "{validation.dto.user.entity_type_id.no_empty}")
    private Long entityTypeId;

    @NotEmpty(message = "{validation.dto.user.entity_number_document.no_empty}")
    @NotNull(message = "{validation.dto.user.entity_number_document.no_empty}")
    private String numberDocument;

    @NotEmpty(message = "{validation.dto.user.entity_name.no_empty}")
    @NotNull(message = "{validation.dto.user.entity_name.no_empty}")
    private String entityName;

    @NotEmpty(message = "{validation.dto.user.entity_lastname.no_empty}")
    @NotNull(message = "{validation.dto.user.entity_lastname.no_empty}")
    private String entityLastname;

    @NotEmpty(message = "{validation.dto.user.username.no_empty}")
    @NotNull(message = "{validation.dto.user.username.no_empty}")
    private String username;

    @NotEmpty(message = "{validation.dto.user.password.no_empty}")
    @NotNull(message = "{validation.dto.user.password.no_empty}")
    private String password;

    @NotEmpty(message = "{validation.dto.user.confirm_password.no_empty}")
    @NotNull(message = "{validation.dto.user.confirm_password.no_empty}")
    private String confirmPassword;

    @NotEmpty(message = "{validation.dto.user.status.no_empty}")
    @NotNull(message = "{validation.dto.user.status.no_empty}")
    private String status;
}
