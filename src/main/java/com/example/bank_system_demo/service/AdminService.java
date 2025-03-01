package com.example.bank_system_demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bank_system_demo.model.Admin;
import com.example.bank_system_demo.repository.AdminRepository;

@Service
public class AdminService {
	@Autowired
	private AdminRepository adminRepository;
	
	public Admin findByEmail(String email) {
		return adminRepository.findByEmail(email);
	}
}