package com.freetech.sample.securitycommandservice.infraestructure.adapters.out.entities;

import enums.StateEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utils.DateUtil;

import java.util.Date;
import java.util.List;

@Getter
@NoArgsConstructor
@Setter
@Entity
@Table(schema="public", name="roles")
public class RolEntity {
    @Id
    @SequenceGenerator(name = "ROL_GENERATOR", sequenceName = "public.seq_roles", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROL_GENERATOR")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "log_creation_user", nullable = false)
    private String logCreationUser;

    @Column(name = "log_update_user", nullable = false)
    private String logUpdateUser;

    @Column(name = "log_creation_date", nullable = false)
    private Date logCreationDate;

    @Column(name = "log_update_date", nullable = false)
    private Date logUpdateDate;

    @Column(name = "log_state", nullable = false)
    private int logState;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "rolEntity")
    private List<UserRolEntity> users;

    @PrePersist
    public void prePersist() {
        logCreationDate = DateUtil.getDate();
        logUpdateDate = DateUtil.getDate();
        logState = StateEnum.ACTIVE.getValue();
    }
}
