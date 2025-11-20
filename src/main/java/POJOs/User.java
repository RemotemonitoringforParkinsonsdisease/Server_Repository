package POJOs;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class User {
    private static List<String> idList = new ArrayList<>();
    protected String id;   // protegido para que subclases puedan acceder
    protected String email;
    protected String password;

    //User para REGISTRARSE
    public User(String email, String password) {
        this.email = email;
        this.password = password;
        idList.add(id);
    }

    //User para cargar datos desde DB
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    private Integer createId() {
        final int idLength = 9;
        for (int i = 0; i < idLength; i++) {
            Random rand = new Random();
            id += rand.nextInt(10);
        }
        return id;
    }

    // Getters
    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    // Setters (opcional, si vas a modificar datos)
    public void setEmail(String email) {
        this.email = email;
    }

}
