package com.freetech.sample.securitycommandservice.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class UserRol {
    private Long id;
    private User user;
    private Rol rol;
}
