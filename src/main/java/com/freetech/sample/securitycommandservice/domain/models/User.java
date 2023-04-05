package com.freetech.sample.securitycommandservice.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class User {
    private Long id;
    private Entity entity;
    private String username;
    private String password;
    private String confirmPassword;
    private String newPassword;
    private String confirmNewPassword;
    private String status;
    private String logUsername;

    public boolean isPasswordConfirm() {
        return password.equals(confirmPassword);
    }
    public boolean isNewPasswordConfirm() {
        return newPassword.equals(confirmNewPassword);
    }
}
