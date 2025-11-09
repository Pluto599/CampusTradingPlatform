package util;

import java.util.regex.Pattern;

public class ValidationUtil {

	private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

	private static final Pattern PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");

	/**
	 * 验证用户名(3-20位字母数字下划线)
	 */
	public static boolean isValidUsername(String username) {
		if (username == null || username.length() < 3 || username.length() > 20) {
			return false;
		}
		return username.matches("^[a-zA-Z0-9_]+$");
	}

	/**
	 * 验证邮箱格式
	 */
	public static boolean isValidEmail(String email) {
		if (email == null) {
			return false;
		}
		return EMAIL_PATTERN.matcher(email).matches();
	}

	/**
	 * 验证手机号格式(中国大陆)
	 */
	public static boolean isValidPhone(String phone) {
		if (phone == null) {
			return false;
		}
		return PHONE_PATTERN.matcher(phone).matches();
	}

	/**
	 * 验证商品标题(1-100字符)
	 */
	public static boolean isValidProductTitle(String title) {
		return title != null && !title.trim().isEmpty() && title.length() <= 100;
	}

	/**
	 * 验证商品描述(不超过1000字符)
	 */
	public static boolean isValidDescription(String description) {
		return description != null && description.length() <= 1000;
	}
}
