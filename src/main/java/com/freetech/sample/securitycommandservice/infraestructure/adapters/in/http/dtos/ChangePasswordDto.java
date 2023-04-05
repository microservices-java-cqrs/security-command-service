package com.freetech.sample.securitycommandservice.infraestructure.adapters.in.http.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class ChangePasswordDto {
    @NotEmpty(message = "{validation.dto.user.password.no_empty}")
    @NotNull(message = "{validation.dto.user.password.no_empty}")
    private String password;

    @NotEmpty(message = "{validation.dto.user.new_password.no_empty}")
    @NotNull(message = "{validation.dto.user.new_password.no_empty}")
    private String newPassword;

    @NotEmpty(message = "{validation.dto.user.confirm_new_password.no_empty}")
    @NotNull(message = "{validation.dto.user.confirm_new_password.no_empty}")
    private String confirmNewPassword;
}
