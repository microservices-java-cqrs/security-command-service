package com.freetech.sample.securitycommandservice.application.messages;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Builder
@Getter
@Setter
public class NewEntityMessage {
    private Long id;
    private Long parentId;
    private Long entityTypeId;
    private String numberDocument;
    private String bussinessName;
    private String name;
    private String lastname;
    private String logCreationUser;
    private String logUpdateUser;
    private Date logCreationDate;
    private Date logUpdateDate;
    private Integer logState;
}
