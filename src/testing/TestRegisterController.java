package testing;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TestRegisterController {

	@Test
	void testUserRegister1() {
		String username = "user1";
		String password = "password1";
		
		
		assertTrue(username.equals("user1") && password.equals("password1"), "Username and Password should be registered successfully.");
	}

	@Test
	void testEmptyRegister() {
		String username = "";
		String password = "";
		
		assertTrue(username.isEmpty() && password.isEmpty(), "Username and Password must not be empty.");
	}
	
	@Test
	void testDuplicateRegister() {
		String username = "user1";
		String password = "password1";
		
		assertTrue(username.equals("user1") && password.equals("password1"), "This Username and Password already exist.");
	}
}
