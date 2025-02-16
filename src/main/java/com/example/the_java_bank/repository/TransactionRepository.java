package com.example.the_java_bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.the_java_bank.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
