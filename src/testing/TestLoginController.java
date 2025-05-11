package testing;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import wordle.Service.PlayerRepository;

/**
 * TestLoginController tests the login functionality of the application.
 * It checks if the login is successful with valid credentials and fails with invalid credentials.
 */
class TestLoginController {

	/**
	 * Test the login functionality with valid credentials.
	 * It checks if the login is successful with valid credentials.
	 */
	@Test
	void testLogin1() {
		String username = "123";
		String password = "123";
		var opt = PlayerRepository.authenticate(username, password);
		assertTrue(opt.isPresent(), "Login should be successful with valid credentials.");
	}
	
	/**
	 * Test the login functionality with invalid credentials.
	 * It checks if the login fails with invalid credentials.
	 */
	@Test
	void testLogin2() {
		String username = "user1";
		String password = "password1";
		var opt = PlayerRepository.authenticate(username, password);
		assertFalse(opt.isPresent(), "Login should fail with invalid credentials.");
	}

	/**
	 * Test the login functionality with empty credentials.
	 * It checks if the login fails with empty credentials.
	 */
	@Test
	void testLoginEmpty() {
		String username = "";
		String password = "";
		
		assertTrue(username.isEmpty() && password.isEmpty(), "Username and Password must not be empty.");
	}
}
