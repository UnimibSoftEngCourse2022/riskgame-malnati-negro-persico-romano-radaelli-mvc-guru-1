package com.mvcguru.risiko.maven.eclipse;

public interface UserDao {
    User getUserByUsernameAndPassword(String username, String password);
    void registerUser(User user);
    void updateUser(User user);
    void deleteUser(User user);
    void createUsersTable();
}
