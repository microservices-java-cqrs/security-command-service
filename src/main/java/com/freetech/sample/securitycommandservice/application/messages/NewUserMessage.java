package com.freetech.sample.securitycommandservice.application.messages;

import lombok.*;

import java.util.Date;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
public class NewUserMessage {
    private Long id;
    private Long entityTypeId;
    private String username;
    private String password;
    private String status;
    private String logCreationUser;
    private String logUpdateUser;
    private Date logCreationDate;
    private Date logUpdateDate;
    private Integer logState;
    private Long entityId;
    private Long entityParentId;
    private String entityNumberDocument;
    private String entityBussinessName;
    private String entityName;
    private String entityLastname;
    private String entityLogCreationUser;
    private String entityLogUpdateUser;
    private Date entityLogCreationDate;
    private Date entityLogUpdateDate;
    private Integer entityLogState;
}
