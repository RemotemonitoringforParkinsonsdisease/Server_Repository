package POJOs;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class User {
    private Integer id;   // protegido para que subclases puedan acceder
    private String email;

    public User() {
    }

    public User(String email) {
        this.email = email;
    }

    // Getters
    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }


    // Setters
    public void setId(Integer id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }






}
