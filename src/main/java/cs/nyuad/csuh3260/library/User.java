package cs.nyuad.csuh3260.library;

import java.util.UUID;

public class User {

    private String name;
    private String username;
    private String password;
    private String id;

    public User(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.id = UUID.randomUUID().toString();
    }

    public User(String id, String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return username.equals("admin") && password.equals("admin");
    }
}
