package com.example.bank_system_demo.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "transactions")
public class Transaction {
	//列挙型を登録
	public enum TransactionType {
		DEPOSIT,
		WITHDRAW,
		OPEN,
		TRANSFER,
		RECEIVE
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "account_id", nullable = false)
	private Integer accountId;
	@Column(name = "target_account_id")
	private Integer targetAccountId;
	// 列挙型を文字列として保存
	@Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
	private TransactionType transactionType;
	@Column(name = "amount", nullable = false)
	private BigDecimal amount;
	@Column(name = "timestamp", nullable = false, updatable = false)
	private Timestamp timestamp;
	
	//コンストラクタ
	public Transaction(int accountId, TransactionType transactionType, BigDecimal amount) {
		this.accountId = accountId;
		this.targetAccountId = null;
		this.transactionType = transactionType;
		this.amount = amount;
	}
	public Transaction(int accountId,Integer targetAccountId, TransactionType transactionType, BigDecimal amount) {
		this.accountId = accountId;
		this.targetAccountId = targetAccountId;
		this.transactionType = transactionType;
		this.amount = amount;
	}
	public Transaction(int id, int accountId,Integer targetAccountId, TransactionType transactionType, BigDecimal amount, Timestamp timestamp) {
		this.id = id;
		this.accountId = accountId;
		this.targetAccountId = targetAccountId;
		this.transactionType = transactionType;
		this.amount = amount;
		this.timestamp = timestamp;
	}
	// Getter & Setter
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public Integer getAccountId() {
		return accountId;
	}
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	
	public Integer getTargetAccountId() {
		return targetAccountId;
	}
	public void setTargetAccountId(Integer targetAccountId) {
		this.targetAccountId = targetAccountId;
	}
	
	public TransactionType getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}

	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	public Timestamp getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
}