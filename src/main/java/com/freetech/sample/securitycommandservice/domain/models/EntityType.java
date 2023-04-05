package com.freetech.sample.securitycommandservice.domain.models;

import lombok.*;

@Setter
@NoArgsConstructor
@Getter
public class EntityType {
    private Long id;
    private String name;
    private String description;
    private String logUsername;
}
