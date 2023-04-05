package com.freetech.sample.securitycommandservice.infraestructure.adapters.out.entities;

import enums.StateEnum;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utils.DateUtil;
import utils.StringUtil;

import java.util.Date;

@Getter
@NoArgsConstructor
@Setter
@Entity
@Table(schema="public", name="entities")
public class EntityEntity {
    @Id
    @SequenceGenerator(name = "ENT_GENERATOR", sequenceName = "public.seq_entities", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ENT_GENERATOR")
    @Column(name = "id", nullable = false)
    private Long id;

    @JoinColumn(name = "parent_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private EntityEntity entityEntity;

    @JoinColumn(name = "entity_type_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private EntityTypeEntity entityTypeEntity;

    @Column(name = "number_document", nullable = false)
    private String numberDocument;

    @Column(name = "bussiness_name", nullable = false)
    private String bussinessName;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "lastname", nullable = false)
    private String lastname;

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
        bussinessName = bussinessName != null ? bussinessName : StringUtil.EMPTY;
        name = name != null ? name : StringUtil.EMPTY;
        lastname = lastname != null ? lastname : StringUtil.EMPTY;
        logCreationDate = DateUtil.getDate();
        logUpdateDate = DateUtil.getDate();
        logState = StateEnum.ACTIVE.getValue();
    }
}
