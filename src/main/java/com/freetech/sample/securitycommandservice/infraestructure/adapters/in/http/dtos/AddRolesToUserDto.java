package com.freetech.sample.securitycommandservice.infraestructure.adapters.in.http.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@NoArgsConstructor
@Setter
public class AddRolesToUserDto {
    List<Long> roles;
}
