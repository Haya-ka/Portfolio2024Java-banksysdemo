package com.example.bank_system_demo.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bank_system_demo.model.Account;
import com.example.bank_system_demo.model.User;
import com.example.bank_system_demo.repository.AccountRepository;
import com.example.bank_system_demo.repository.UserRepository;

@Service
public class TransactionService {
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private UserRepository userRepository;
	
	@Transactional
	public boolean transfer(int senderId, String recipientEmail, BigDecimal amount) {
		//送金元の口座を取得
		List<Account> senderAccountOpt = accountRepository.findByUserId(senderId);
		if(senderAccountOpt.isEmpty()) {
			System.out.println("送金元の口座が見つかりません。");
			return false;
		}
		Account senderAccount = senderAccountOpt.get(0);
		
		//残高チェック
		if(senderAccount.getBalance().compareTo(amount) < 0) {
			System.out.println("残高が不足しています。");
			return false;
		}
		
		//送金先のユーザーをメールアドレスで検索
		Optional<User> recipientUserOpt = userRepository.findByEmail(recipientEmail);
		if(recipientUserOpt.isEmpty()) {
			System.out.println("送金先のユーザーが見つかりません。");
			return false;
		}
		User recipientUser = recipientUserOpt.get();
		
		//送金先の口座を取得
		List<Account> recipientAccountOpt = accountRepository.findByUserId(recipientUser.getId());
		if(recipientAccountOpt.isEmpty()) {
			System.out.println("送金先の口座が見つかりません。");
			return false;
		}
		Account recipientAccount = recipientAccountOpt.get(0);
		
		//残高の更新
		senderAccount.setBalance(senderAccount.getBalance().subtract(amount));
		recipientAccount.setBalance(recipientAccount.getBalance().add(amount));
		
		//口座情報を保存
		accountRepository.save(senderAccount);
		accountRepository.save(recipientAccount);
		
		System.out.println("送金が成功しました。");
		return true;
	}
}
