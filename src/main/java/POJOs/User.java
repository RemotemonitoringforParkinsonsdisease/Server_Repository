package POJOs;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class User {
    private static List<String> idList = new ArrayList<>();
    protected String id;   // protegido para que subclases puedan acceder
    protected String email;
    protected String fullName;

    //User para REGISTRARSE
    public User(String email, String fullName, String letter) {
        this.email = email;
        this.fullName = fullName;
        this.id = createId(letter);
        idList.add(id);
    }

    //User para cargar datos desde DB
    public User(String email, String fullName) {
        this.email = email;
        this.fullName = fullName;
    }

    private String createId(String letter) {
        final int idLength = 9;
        for (int i = 0; i < idLength; i++) {
            Random rand = new Random();
            letter += rand.nextInt(10);
        }
        if (idList.contains(letter)) {
            return createId("" + letter.charAt(0));
        }
        return letter;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    // Setters (opcional, si vas a modificar datos)
    public void setEmail(String email) {
        this.email = email;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
