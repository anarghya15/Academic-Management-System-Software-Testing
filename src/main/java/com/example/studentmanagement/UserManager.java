package com.example.studentmanagement;

class UserManager {
    private List<User> users = new ArrayList<>();
    private User loggedInUser;

    public void registerUser(String username, String password, String role) {
        users.add(new User(username, password, role));
        System.out.println("User registered successfully.");
    }

    public boolean loginUser(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.authenticate(password)) {
                loggedInUser = user;
                System.out.println("Login successful. Welcome, " + user.getUsername() + "!");
                return true;
            }
        }
        System.out.println("Invalid credentials.");
        return false;
    }

    public void logoutUser() {
        if (loggedInUser != null) {
            System.out.println("Goodbye, " + loggedInUser.getUsername() + "!");
            loggedInUser = null;
        } else {
            System.out.println("No user is logged in.");
        }
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }
}