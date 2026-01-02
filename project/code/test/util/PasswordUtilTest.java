package util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordUtilTest {

	@Test
	void jacocoAgentShouldBePresentWhenCoverageEnabled() {
		assertDoesNotThrow(() -> Class.forName("org.jacoco.agent.rt.RT"));
	}

	@AfterAll
	static void forceJacocoDump() throws Exception {
		// 在部分高版本 JDK/启动方式下，JaCoCo 的 dumponexit 可能不落盘；这里强制 dump 一次
		Class<?> rt = Class.forName("org.jacoco.agent.rt.RT");
		Object agent = rt.getMethod("getAgent").invoke(null);
		agent.getClass().getMethod("dump", boolean.class).invoke(agent, true);
	}

	@Test
	void hashPassword_shouldBeDeterministic() {
		String p = "abc123";
		assertEquals(PasswordUtil.hashPassword(p), PasswordUtil.hashPassword(p));
	}

	@Test
	void hashPassword_shouldReturn64LowercaseHex() {
		String hash = PasswordUtil.hashPassword("password");
		assertNotNull(hash);
		assertEquals(64, hash.length());
		assertTrue(hash.matches("^[0-9a-f]{64}$"));
	}

	@Test
	void hashPassword_knownSha256ForAbc() {
		assertEquals(
				"ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad",
				PasswordUtil.hashPassword("abc"));
	}

	@Test
	void hashPassword_differentPasswordsShouldProduceDifferentHash() {
		String h1 = PasswordUtil.hashPassword("password1");
		String h2 = PasswordUtil.hashPassword("password2");
		assertNotEquals(h1, h2);
	}

	@Test
	void hashPassword_emptyStringShouldWork() {
		String hash = PasswordUtil.hashPassword("");
		assertNotNull(hash);
		assertEquals(64, hash.length());
	}

	@Test
	void hashPassword_nullShouldThrow() {
		assertThrows(NullPointerException.class, () -> PasswordUtil.hashPassword(null));
	}

	@Test
	void verifyPassword_correctPasswordShouldReturnTrue() {
		String p = "nju-se";
		String stored = PasswordUtil.hashPassword(p);
		assertTrue(PasswordUtil.verifyPassword(p, stored));
	}

	@Test
	void verifyPassword_wrongPasswordShouldReturnFalse() {
		String stored = PasswordUtil.hashPassword("right-password");
		assertFalse(PasswordUtil.verifyPassword("wrong-password", stored));
	}

	@Test
	void verifyPassword_invalidStoredHashShouldReturnFalse() {
		assertFalse(PasswordUtil.verifyPassword("any", "not-a-valid-hash"));
	}

	@Test
	void verifyPassword_nullStoredHashShouldReturnFalse() {
		assertFalse(PasswordUtil.verifyPassword("any", null));
	}

	@Test
	void verifyPassword_nullInputShouldThrow() {
		String stored = PasswordUtil.hashPassword("p@ssw0rd");
		assertThrows(NullPointerException.class, () -> PasswordUtil.verifyPassword(null, stored));
	}

	@Test
	void isValidPassword_nullShouldBeFalse() {
		assertFalse(PasswordUtil.isValidPassword(null));
	}

	@Test
	void isValidPassword_emptyShouldBeFalse() {
		assertFalse(PasswordUtil.isValidPassword(""));
	}

	@Test
	void isValidPassword_length5ShouldBeFalse() {
		assertFalse(PasswordUtil.isValidPassword("12345"));
	}

	@Test
	void isValidPassword_length6ShouldBeTrue() {
		assertTrue(PasswordUtil.isValidPassword("123456"));
	}

	@Test
	void isValidPassword_longerShouldBeTrue() {
		assertTrue(PasswordUtil.isValidPassword("longer-password"));
	}
}
