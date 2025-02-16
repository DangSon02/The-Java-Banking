package com.example.the_java_bank.entity;

import java.util.HashSet;
import java.util.Set;

import com.example.the_java_bank.entity.BaseEntity.AbstractEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
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
@Table(name = "tbl_role")
public class Roles extends AbstractEntity<Integer> {

    private String name;

    private String description;

    @OneToMany(mappedBy = "roles")
    private Set<RoleHasPermission> roleHasPermissions = new HashSet<>();

    @OneToMany(mappedBy = "roles")
    private Set<UserHasRole> userHasRoles = new HashSet<>();

}
