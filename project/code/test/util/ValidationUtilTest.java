package util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ValidationUtilTest {

	@Test
	void isValidUsername_nullShouldBeFalse() {
		assertFalse(ValidationUtil.isValidUsername(null));
	}

	@Test
	void isValidUsername_tooShortShouldBeFalse() {
		assertFalse(ValidationUtil.isValidUsername("ab"));
	}

	@Test
	void isValidUsername_minLengthShouldBeTrue() {
		assertTrue(ValidationUtil.isValidUsername("abc"));
	}

	@Test
	void isValidUsername_maxLengthShouldBeTrue() {
		assertTrue(ValidationUtil.isValidUsername("a".repeat(20)));
	}

	@Test
	void isValidUsername_tooLongShouldBeFalse() {
		assertFalse(ValidationUtil.isValidUsername("a".repeat(21)));
	}

	@Test
	void isValidUsername_shouldAllowUnderscoreAndDigits() {
		assertTrue(ValidationUtil.isValidUsername("user_001"));
	}

	@Test
	void isValidUsername_shouldRejectDash() {
		assertFalse(ValidationUtil.isValidUsername("user-name"));
	}

	@Test
	void isValidUsername_shouldRejectSpace() {
		assertFalse(ValidationUtil.isValidUsername("user name"));
	}

	@Test
	void isValidUsername_shouldRejectChinese() {
		assertFalse(ValidationUtil.isValidUsername("刘佳璇"));
	}

	@Test
	void isValidEmail_nullShouldBeFalse() {
		assertFalse(ValidationUtil.isValidEmail(null));
	}

	@Test
	void isValidEmail_basicValidShouldBeTrue() {
		assertTrue(ValidationUtil.isValidEmail("test@example.com"));
	}

	@Test
	void isValidEmail_shouldAllowPlusAndDot() {
		assertTrue(ValidationUtil.isValidEmail("a.b+tag@sub.example.com"));
	}

	@Test
	void isValidEmail_missingAtShouldBeFalse() {
		assertFalse(ValidationUtil.isValidEmail("test.example.com"));
	}

	@Test
	void isValidEmail_missingTldShouldBeFalse() {
		assertFalse(ValidationUtil.isValidEmail("test@example"));
	}

	@Test
	void isValidEmail_tldTooShortShouldBeFalse() {
		assertFalse(ValidationUtil.isValidEmail("test@example.c"));
	}

	@Test
	void isValidPhone_nullShouldBeFalse() {
		assertFalse(ValidationUtil.isValidPhone(null));
	}

	@Test
	void isValidPhone_valid138ShouldBeTrue() {
		assertTrue(ValidationUtil.isValidPhone("13812345678"));
	}

	@Test
	void isValidPhone_valid199ShouldBeTrue() {
		assertTrue(ValidationUtil.isValidPhone("19912345678"));
	}

	@Test
	void isValidPhone_invalidPrefixShouldBeFalse() {
		assertFalse(ValidationUtil.isValidPhone("12812345678"));
	}

	@Test
	void isValidPhone_invalidLengthShouldBeFalse() {
		assertFalse(ValidationUtil.isValidPhone("1381234567"));
	}

	@Test
	void isValidProductTitle_nullShouldBeFalse() {
		assertFalse(ValidationUtil.isValidProductTitle(null));
	}

	@Test
	void isValidProductTitle_blankShouldBeFalse() {
		assertFalse(ValidationUtil.isValidProductTitle(""));
	}

	@Test
	void isValidProductTitle_whitespaceShouldBeFalse() {
		assertFalse(ValidationUtil.isValidProductTitle("   "));
	}

	@Test
	void isValidProductTitle_length1ShouldBeTrue() {
		assertTrue(ValidationUtil.isValidProductTitle("x"));
	}

	@Test
	void isValidProductTitle_length100ShouldBeTrue() {
		assertTrue(ValidationUtil.isValidProductTitle("a".repeat(100)));
	}

	@Test
	void isValidProductTitle_length101ShouldBeFalse() {
		assertFalse(ValidationUtil.isValidProductTitle("a".repeat(101)));
	}

	@Test
	void isValidDescription_nullShouldBeFalse() {
		assertFalse(ValidationUtil.isValidDescription(null));
	}

	@Test
	void isValidDescription_emptyShouldBeTrue() {
		assertTrue(ValidationUtil.isValidDescription(""));
	}

	@Test
	void isValidDescription_length1000ShouldBeTrue() {
		assertTrue(ValidationUtil.isValidDescription("a".repeat(1000)));
	}

	@Test
	void isValidDescription_length1001ShouldBeFalse() {
		assertFalse(ValidationUtil.isValidDescription("a".repeat(1001)));
	}
}
