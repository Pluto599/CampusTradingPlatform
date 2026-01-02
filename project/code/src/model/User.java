package model;

import java.util.Date;

public class User {
	private Long id;
	private String username;
	private String email;
	private String phone;
	private String passwordHash;
	private int creditScore;
	private Date registeredAt;
	private String avatarUrl;

	public User() {
		this.creditScore = 100; // 默认信用分
		this.registeredAt = new Date();
	}

	public User(Long id, String username, String email, String phone, String passwordHash) {
		this();
		this.id = id;
		this.username = username;
		this.email = email;
		this.phone = phone;
		this.passwordHash = passwordHash;
	}

	// Getters and Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public int getCreditScore() {
		return creditScore;
	}

	public void setCreditScore(int creditScore) {
		this.creditScore = creditScore;
	}

	public Date getRegisteredAt() {
		return registeredAt;
	}

	public void setRegisteredAt(Date registeredAt) {
		this.registeredAt = registeredAt;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	@Override
	public String toString() {
		return "用户[" + username + "] (ID:" + id + ", 信用分:" + creditScore + ")";
	}
}
