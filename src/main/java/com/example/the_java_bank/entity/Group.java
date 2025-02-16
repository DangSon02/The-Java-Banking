package com.example.the_java_bank.entity;

import java.util.HashSet;
import java.util.Set;

import com.example.the_java_bank.entity.BaseEntity.AbstractEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tbl_group")
public class Group extends AbstractEntity<Integer> {

    private String name;

    private String description;

    @OneToOne
    @JoinColumn(name = "role_id")
    private Roles roles;

    @OneToMany(mappedBy = "group")
    private Set<GroupHasUser> userHasGroups = new HashSet<>();
}
