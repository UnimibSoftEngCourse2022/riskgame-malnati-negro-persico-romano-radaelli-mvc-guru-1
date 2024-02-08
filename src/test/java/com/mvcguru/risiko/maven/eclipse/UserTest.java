package com.mvcguru.risiko.maven.eclipse;

import org.junit.Assert;
import org.junit.Test;

public class UserTest {

    @Test
    public void testUser() {
        String username = "Bobby";
        String password = "bobbybobby";

        User user = new User(username, password);

        Assert.assertEquals("Username should be equal to the one set", username, user.getUsername());
        Assert.assertEquals("Password should be equal to the one set", password, user.getPassword());

        String newUsername = "newTestUser";
        String newPassword = "newTestPassword";

        user.setUsername(newUsername);
        user.setPassword(newPassword);

        Assert.assertEquals("Username should be updated to the new one", newUsername, user.getUsername());
        Assert.assertEquals("Password should be updated to the new one", newPassword, user.getPassword());
    }
}
