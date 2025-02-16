package com.example.the_java_bank.entity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import com.example.the_java_bank.entity.BaseEntity.AbstractEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_users")
public class User extends AbstractEntity<Long> {

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "other_name")
    private String otherName;

    @Column(name = "gender")
    private String gender;

    @Column(name = "address")
    private String address;

    @Column(name = "state_of_origin")
    private String stateOfOrigin;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "account_balance")
    private BigDecimal accountBalance;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "alter_native_phone_number")
    private String alternativePhoneNumber;

    @Column(name = "status")
    private String status;

    @OneToMany(mappedBy = "user")
    private Set<GroupHasUser> userHasGroups = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<UserHasRole> userHasRoles = new HashSet<>();

}
