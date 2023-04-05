package com.freetech.sample.securitycommandservice.application.messages;

import lombok.*;

import java.util.Date;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
public class DeleteEntityTypeMessage {
    private Long id;
    private String logUpdateUser;
    private Date logUpdateDate;
    private int logState;
}
