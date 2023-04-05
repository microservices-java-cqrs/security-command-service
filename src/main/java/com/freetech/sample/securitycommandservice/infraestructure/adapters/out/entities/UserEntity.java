package com.freetech.sample.securitycommandservice.infraestructure.adapters.out.entities;

import enums.StateEnum;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utils.DateUtil;

import java.util.Date;

@Getter
@NoArgsConstructor
@Setter
@Entity
@Table(schema="public", name="users")
public class UserEntity {
    @Id
    @SequenceGenerator(name = "USR_GENERATOR", sequenceName = "public.seq_users", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USR_GENERATOR")
    @Column(name = "id", nullable = false)
    private Long id;

    @JoinColumn(name = "entity_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private EntityEntity entityEntity;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "status", nullable = false)
    private String status;

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

    @PrePersist
    public void prePersist() {
        logCreationDate = DateUtil.getDate();
        logUpdateDate = DateUtil.getDate();
        logState = StateEnum.ACTIVE.getValue();
    }
}