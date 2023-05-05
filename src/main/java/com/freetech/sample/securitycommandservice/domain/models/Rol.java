package com.freetech.sample.securitycommandservice.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class Rol {
    private Long id;
    private String name;
    private String description;
    private String logUsername;
}
