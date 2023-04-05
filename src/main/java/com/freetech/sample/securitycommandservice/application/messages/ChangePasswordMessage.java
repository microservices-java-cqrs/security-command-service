package com.freetech.sample.securitycommandservice.application.messages;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Builder
@Getter
public class ChangePasswordMessage {
    private Long id;
    private String password;
    private String logUpdateUser;
    private Date logUpdateDate;
}
