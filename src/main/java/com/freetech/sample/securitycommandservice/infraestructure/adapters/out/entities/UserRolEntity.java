package com.freetech.sample.securitycommandservice.infraestructure.adapters.out.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
@Entity
@Table(schema="public", name="users_roles")
public class UserRolEntity {
    @Id
    @SequenceGenerator(name = "USR_ROL_GENERATOR", sequenceName = "public.seq_users_roles", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USR_ROL_GENERATOR")
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rol_id")
    private RolEntity rolEntity;
}
