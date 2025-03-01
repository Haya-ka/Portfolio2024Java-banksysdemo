package com.example.bank_system_demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.bank_system_demo.model.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
	List<Account> findByUserId(int userId);
}