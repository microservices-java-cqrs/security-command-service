package com.freetech.sample.securitycommandservice.infraestructure.ports.in;

import com.freetech.sample.securitycommandservice.domain.models.User;

public interface DeleteUserPort {
    User deleteUser(User user);
}
