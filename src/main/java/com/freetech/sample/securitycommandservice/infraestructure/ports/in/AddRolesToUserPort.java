package com.freetech.sample.securitycommandservice.infraestructure.ports.in;

import com.freetech.sample.securitycommandservice.domain.models.UserRol;

import java.util.List;

public interface AddRolesToUserPort {
    void addRolesToUser(Long id, List<UserRol> userRoles);
}
