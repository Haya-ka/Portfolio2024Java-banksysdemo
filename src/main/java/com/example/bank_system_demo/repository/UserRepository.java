package com.example.bank_system_demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.bank_system_demo.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	Optional<User> findByEmail(String email);
	//ユーザーの新規登録時にemailの重複を確認
	boolean existsByEmail(String email);
}