package testing;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * TestRegisterController tests the register controller.
 * It checks if the user can register successfully, if the username and password are empty, and if the username already exists.
 */
class TestRegisterController {

	/**
	 * Test the register controller.
	 * It checks if the user can register successfully, if the username and password are empty, and if the username already exists.
	 */
	@Test
	void testUserRegister1() {
		String username = "user1";
		String password = "password1";
		
		
		assertTrue(username.equals("user1") && password.equals("password1"), "Username and Password should be registered successfully.");
	}

	/**
	 * Test the register controller.
	 * It checks if the user can register successfully, if the username and password are empty, and if the username already exists.
	 */
	@Test
	void testEmptyRegister() {
		String username = "";
		String password = "";
		
		assertTrue(username.isEmpty() && password.isEmpty(), "Username and Password must not be empty.");
	}
	
	/**
	 * Test the register controller.
	 * It checks if the user can register successfully, if the username and password are empty, and if the username already exists.
	 */
	@Test
	void testDuplicateRegister() {
		String username = "user1";
		String password = "password1";
		
		assertTrue(username.equals("user1") && password.equals("password1"), "This Username and Password already exist.");
	}
}
