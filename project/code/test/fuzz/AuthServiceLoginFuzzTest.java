package fuzz;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;
import com.code_intelligence.jazzer.junit.FuzzTest;
import repository.Database;
import service.AuthService;

import java.lang.reflect.Field;

/**
 * Fuzz AuthService.login(username, password) for robustness.
 */
class AuthServiceLoginFuzz {

	private static void resetDatabaseSingleton() throws Exception {
		Field instance = Database.class.getDeclaredField("instance");
		instance.setAccessible(true);
		instance.set(null, null);
	}

	@FuzzTest(maxDuration = "10s")
	void loginShouldNotCrash(FuzzedDataProvider data) throws Exception {
		// Keep each fuzz iteration isolated.
		resetDatabaseSingleton();

		AuthService auth = new AuthService();
		auth.registerUser("seeduser", "seed@example.com", "13812345678", "123456");

		boolean useSeedUser = data.consumeBoolean();
		boolean nullPassword = data.consumeBoolean();

		String username = useSeedUser ? "seeduser" : data.consumeString(20);
		String password = nullPassword ? null : data.consumeString(20);

		// If the implementation is not robust against null/edge inputs, Jazzer will find a crash.
		auth.login(username, password);
	}
}
