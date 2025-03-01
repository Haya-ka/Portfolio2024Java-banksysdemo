package com.example.bank_system_demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bank_system_demo.model.Admin;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Admin findByEmail(String email);
}