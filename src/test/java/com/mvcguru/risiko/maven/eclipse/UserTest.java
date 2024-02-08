package com.mvcguru.risiko.maven.eclipse;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {
	private User user;
	
	@BeforeEach
    void setUp(){
		String username = "bobby";
	    String password = "bobbybobby00";
        user = new User(username, password);
    }
    
	@Test
	void testGet() {
	   String username = "bobby";
       String password = "bobbybobby00";
       
       assertEquals(username, user.getUsername(), "Username should be equal to the one set");
       assertEquals(password, user.getPassword(), "Password should be equal to the one set");
   }
	
	@Test
	void testSet() {
       String newUsername = "newTestUser";
       String newPassword = "newTestPassword";

       user.setUsername(newUsername);
       user.setPassword(newPassword);

       assertEquals(newUsername, user.getUsername(), "Username should be updated to the new one");
       assertEquals(newPassword, user.getPassword(), "Password should be updated to the new one");
       user.setUsername(newUsername);
       user.setPassword(newPassword);

       assertEquals(newUsername, user.getUsername(), "Username should be updated to the new one");
       assertEquals(newPassword, user.getPassword(), "Password should be updated to the new one");
   }
}
