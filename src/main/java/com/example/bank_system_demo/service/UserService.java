package com.example.bank_system_demo.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bank_system_demo.model.Account;
import com.example.bank_system_demo.model.User;
import com.example.bank_system_demo.repository.AccountRepository;
import com.example.bank_system_demo.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AccountRepository accountRepository;
	
	public boolean createUser(String name, String email, String password) {
		//メールアドレスの重複確認
		Optional<User> userOptional = userRepository.findByEmail(email);
		if (userOptional.orElse(null) != null) {
			return false;
		}
		
		//新しいユーザーを作成
		User user = new User(name, email, PasswordHasher.hashPassword(password));
		userRepository.save(user);
		System.out.println("ユーザー情報を登録しました。");
		return true;
	}
	
	public boolean authenticate(String email, String password) {
		Optional<User> optionalUser = userRepository.findByEmail(email);
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			if (PasswordHasher.checkPassword(password, user.getPassword())) {
				System.out.println("ログインが成功しました。");
				return true;
			}else {
				System.out.println("ユーザ名とパスワードが一致しません。");
				return false;
			}
		}else {
			System.out.println("ユーザ情報が見つかりませんでした。");
			return false;
		}
	}
	
	public Optional<User> getUserById(int id) {
		return userRepository.findById(id);
	}
	
	public Optional<User> getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}
	
	public BigDecimal getUserBalance(int userId) {
		List<Account> accounts = accountRepository.findByUserId(userId);
		BigDecimal totalBalance = BigDecimal.ZERO;
		
		for (Account account : accounts) {
			totalBalance = totalBalance.add(account.getBalance());
		}
		return totalBalance;
	}

	public List<Account> getUserAccounts(int id) {
		return null;
	}
}
