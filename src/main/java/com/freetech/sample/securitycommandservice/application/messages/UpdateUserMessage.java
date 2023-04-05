package com.freetech.sample.securitycommandservice.application.messages;

import lombok.*;

import java.util.Date;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
public class UpdateUserMessage {
    private Long id;
    private Long entityTypeId;
    private String username;
    private String status;
    private String logUpdateUser;
    private Date logUpdateDate;
    private String entityNumberDocument;
    private String entityBussinessName;
    private String entityName;
    private String entityLastname;
    private String entityLogUpdateUser;
    private Date entityLogUpdateDate;
}
