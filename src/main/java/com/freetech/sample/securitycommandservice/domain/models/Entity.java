package com.freetech.sample.securitycommandservice.domain.models;

import lombok.*;

@Getter
@NoArgsConstructor
@Setter
public class Entity {
    private Long id;
    private EntityType entityType;
    private String numberDocument;
    private String bussinessName;
    private String name;
    private String lastname;
    private String logUsername;
}
