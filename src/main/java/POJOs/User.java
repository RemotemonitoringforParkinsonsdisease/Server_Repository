package POJOs;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class User {
    private Integer id;   // protegido para que subclases puedan acceder
    private String email;

    public User(String email, String fullName) {
        this.email = email;
        this.fullName = fullName;
    }
    public User(Integer id, String email, String fullName) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
    }

    // Getters
    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
    public String getFullName() {
        return fullName;
    }

    // Setters
    public void setId(Integer id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }




}
