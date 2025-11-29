package POJOs;


public class User {
    private Integer id;   // protegido para que subclases puedan acceder
    private String email;

    public User() {
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