package testing;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import wordle.Service.PlayerRepository;

class TestLoginController {

	@Test
	void testLogin1() {
		String username = "123";
		String password = "123";
		var opt = PlayerRepository.authenticate(username, password);
		assertTrue(opt.isPresent(), "Login should be successful with valid credentials.");
	}
	
	@Test
	void testLogin2() {
		String username = "user1";
		String password = "password1";
		var opt = PlayerRepository.authenticate(username, password);
		assertFalse(opt.isPresent(), "Login should fail with invalid credentials.");
	}

	@Test
	void testLoginEmpty() {
		String username = "";
		String password = "";
		
		assertTrue(username.isEmpty() && password.isEmpty(), "Username and Password must not be empty.");
	}
}
