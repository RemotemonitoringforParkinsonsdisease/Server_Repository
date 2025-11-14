package POJOs;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class User {
    private static List<String> idList = new ArrayList<String>();
    private String id;
    private String email;
    private String fullName;

    //User para REGISTRARSE
    public User(String email, String fullName, String letter) {
        this.email = email;
        this.fullName = fullName;
        this.id = createId(letter);
        idList.add(id);
    }

    private String createId(String letter) {
        final int idLength = 9;
        for (int i = 0; i < idLength; i++) {
            Random rand = new Random();
            letter += rand.nextInt(10);
        }
        if(idList.contains(letter)){
            return createId("" + letter.charAt(0));
        }
        return letter;
    }
}
