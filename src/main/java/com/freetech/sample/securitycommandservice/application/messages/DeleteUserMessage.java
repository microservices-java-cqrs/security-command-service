package com.freetech.sample.securitycommandservice.application.messages;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Builder
@Getter
public class DeleteUserMessage {
    private Long id;
    private String logUpdateUser;
    private Date logUpdateDate;
    private Integer logState;
    private String entityLogUpdateUser;
    private Date entityLogUpdateDate;
    private Integer entityLogState;
}
