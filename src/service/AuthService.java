package service;

import model.User;
import repository.Database;
import util.PasswordUtil;
import util.ValidationUtil;

public class AuthService {
	private Database db;
	private User currentUser; // 当前登录用户

	public AuthService() {
		this.db = Database.getInstance();
		this.currentUser = null;
	}

	/**
	 * 用户注册
	 */
	public boolean registerUser(String username, String email, String phone, String password) {
		// 验证输入
		if (!ValidationUtil.isValidUsername(username)) {
			System.out.println("用户名不合法！(3-20位字母数字下划线)");
			return false;
		}

		if (!ValidationUtil.isValidEmail(email)) {
			System.out.println("邮箱格式不正确！");
			return false;
		}

		if (!ValidationUtil.isValidPhone(phone)) {
			System.out.println("手机号格式不正确！");
			return false;
		}

		if (!PasswordUtil.isValidPassword(password)) {
			System.out.println("密码长度至少6位！");
			return false;
		}

		// 检查用户名是否已存在
		if (db.getUserByUsername(username) != null) {
			System.out.println("用户名已存在！");
			return false;
		}

		// 检查邮箱是否已注册
		if (db.getUserByEmail(email) != null) {
			System.out.println("邮箱已被注册！");
			return false;
		}

		// 创建新用户
		Long userId = db.generateUserId();
		String passwordHash = PasswordUtil.hashPassword(password);
		User newUser = new User(userId, username, email, phone, passwordHash);
		db.addUser(newUser);

		System.out.println("注册成功！用户ID: " + userId);
		return true;
	}

	/**
	 * 用户登录
	 */
	public boolean login(String username, String password) {
		User user = db.getUserByUsername(username);

		if (user == null) {
			System.out.println("用户不存在！");
			return false;
		}

		if (!PasswordUtil.verifyPassword(password, user.getPasswordHash())) {
			System.out.println("密码错误！");
			return false;
		}

		currentUser = user;
		System.out.println("登录成功！欢迎，" + user.getUsername());
		return true;
	}

	/**
	 * 用户登出
	 */
	public void logout() {
		if (currentUser != null) {
			System.out.println("再见，" + currentUser.getUsername());
			currentUser = null;
		}
	}

	/**
	 * 检查是否已登录
	 */
	public boolean isLoggedIn() {
		return currentUser != null;
	}

	/**
	 * 获取当前登录用户
	 */
	public User getCurrentUser() {
		return currentUser;
	}

	/**
	 * 重置密码
	 */
	public boolean resetPassword(String email, String newPassword) {
		User user = db.getUserByEmail(email);

		if (user == null) {
			System.out.println("该邮箱未注册！");
			return false;
		}

		if (!PasswordUtil.isValidPassword(newPassword)) {
			System.out.println("密码长度至少6位！");
			return false;
		}

		user.setPasswordHash(PasswordUtil.hashPassword(newPassword));
		db.updateUser(user);

		System.out.println("密码重置成功！");
		return true;
	}

	/**
	 * 编辑个人资料
	 */
	public boolean editProfile(String email, String phone) {
		if (currentUser == null) {
			System.out.println("请先登录！");
			return false;
		}

		if (email != null && !email.isEmpty()) {
			if (!ValidationUtil.isValidEmail(email)) {
				System.out.println("邮箱格式不正确！");
				return false;
			}
			// 检查邮箱是否被其他用户使用
			User existingUser = db.getUserByEmail(email);
			if (existingUser != null && !existingUser.getId().equals(currentUser.getId())) {
				System.out.println("该邮箱已被其他用户使用！");
				return false;
			}
			currentUser.setEmail(email);
		}

		if (phone != null && !phone.isEmpty()) {
			if (!ValidationUtil.isValidPhone(phone)) {
				System.out.println("手机号格式不正确！");
				return false;
			}
			currentUser.setPhone(phone);
		}

		db.updateUser(currentUser);
		System.out.println("个人资料更新成功！");
		return true;
	}
}
