package com.example.bank_system_demo.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "accounts")
public class Account {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "user_id", nullable = false)
	private int userId;
	@Column(name = "balance", nullable = false)
	private BigDecimal balance;
	@Column(name = "created_at", nullable = false, updatable = false)
	private Timestamp timestamp;
	
	// コンストラクタ
	public Account(int id, int userId, BigDecimal balance, Timestamp timestamp) {
		this.id = id;
		this.userId = userId;
		this.balance = balance;
		this.timestamp = timestamp;
	}
	public Account() {}
	
	// 口座作成時にtimestampを設定する
    @PrePersist
    public void setTimestamp() {
        this.timestamp = Timestamp.valueOf(LocalDateTime.now()); // 現在日時を設定
    }
	
	// Getter & Setter
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	
	public Timestamp getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
}
