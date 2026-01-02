package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.Database;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {

	@BeforeEach
	void resetDatabaseSingleton() throws Exception {
		Field instance = Database.class.getDeclaredField("instance");
		instance.setAccessible(true);
		instance.set(null, null);
	}

	@Test
	void login_nullPassword_shouldNotThrowAndReturnFalse() {
		AuthService auth = new AuthService();
		assertTrue(auth.registerUser("seeduser", "seed@example.com", "13812345678", "123456"));

		assertDoesNotThrow(() -> {
			boolean ok = auth.login("seeduser", null);
			assertFalse(ok);
		});
	}

	@Test
	void login_emptyPassword_shouldReturnFalse() {
		AuthService auth = new AuthService();
		assertTrue(auth.registerUser("seeduser", "seed@example.com", "13812345678", "123456"));

		assertFalse(auth.login("seeduser", ""));
	}

	@Test
	void login_nullUsername_shouldReturnFalse() {
		AuthService auth = new AuthService();
		assertFalse(auth.login(null, "123456"));
	}
}
