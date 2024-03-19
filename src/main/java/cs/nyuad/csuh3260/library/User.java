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

    public String getName() {
        return "";
    }

    public String getUsername() {
        return "";
    }

    public String getPassword() {
       return "";
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
    }

    public void setPassword(String password) {
    }
}
