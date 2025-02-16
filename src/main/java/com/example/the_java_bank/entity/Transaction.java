package com.example.the_java_bank.entity;

import java.math.BigDecimal;

import com.example.the_java_bank.entity.BaseEntity.AbstractEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tbl_transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction extends AbstractEntity<Long> {

    @Column(name = "transaction_type")
    private String transactionType;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "status")
    private String status;

}
