package com.freetech.sample.securitycommandservice.infraestructure.ports.in;

import com.freetech.sample.securitycommandservice.domain.models.Rol;

public interface CreateRolPort {
    Rol createRol(Rol rol);
}
