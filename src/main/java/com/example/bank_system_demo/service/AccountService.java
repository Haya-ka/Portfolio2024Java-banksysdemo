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
public class AccountService {
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private UserRepository userRepository;
	
	public void deposit(int userId, BigDecimal amount) {
		List<Account> accounts = accountRepository.findByUserId(userId);
		if(!accounts.isEmpty()) {
			//デフォルトで最初の口座を選択
			Account account = accounts.get(0);
			//残高を更新
			account.setBalance(account.getBalance().add(amount));
			accountRepository.save(account);
			System.out.println("入金が成功しました。");
		}else {
			System.out.println("該当する口座が見つかりませんでした。");
		}
	}
	
	public boolean withdraw(int userId, BigDecimal amount) {
		Account account = accountRepository.findById(userId).orElse(null);
		if(account != null && account.getBalance().compareTo(amount) >= 0) {
			account.setBalance(account.getBalance().subtract(amount));
			accountRepository.save(account);
			return true;
		}
		//残高不足の場合
		return false;
	}
	
	public boolean transfer(int fromAccountId, int toAccountId, BigDecimal amount) {
		Account fromAccount = accountRepository.findById(fromAccountId).orElse(null);
		Account toAccount = accountRepository.findById(toAccountId).orElse(null);
		
		if(fromAccount != null && toAccount != null && fromAccount.getBalance().compareTo(amount) >= 0) {
			fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
			toAccount.setBalance(toAccount.getBalance().add(amount));
			accountRepository.save(fromAccount);
			accountRepository.save(toAccount);
			return true;
		}
		return false;
	}
	
	//新規口座を作成
	public boolean createAccount(int userId, double initialBalance) {
		Optional<User> userOptional = userRepository.findById(userId);
		if(userOptional.isPresent()) {
			Account account = new Account();
			account.setUserId(userId);
			account.setBalance(BigDecimal.valueOf(initialBalance));
			
			accountRepository.save(account);
			return true;
		}
		return false;
	}
}
