package com.freetech.sample.securitycommandservice.infraestructure.adapters.in.http.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewEntityDto {
    private Long id;
    private Long entityTypeId;
    private String numberDocument;
    private String bussinessName;
}
