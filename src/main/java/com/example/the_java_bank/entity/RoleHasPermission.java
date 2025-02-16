package com.example.the_java_bank.entity;

import com.example.the_java_bank.entity.BaseEntity.AbstractEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tbl_role_has_permission")
public class RoleHasPermission extends AbstractEntity<Integer> {

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Roles roles;

    @ManyToOne
    @JoinColumn(name = "permission_id")
    private Permission permission;

}
