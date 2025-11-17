package jdbcs;

import POJOs.Symptoms;
import managers.SymptomManager;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SymptomJDBC implements SymptomManager {

    private ManagerJDBC manager;
    public SymptomManagerJDBC(ManagerJDBC manager) {
        this.manager = manager;
    }

    public void addSymptom(Symptoms symptom) {
        String sql = "INSERT INTO Symptoms (name) VALUES (?)";

        try {
            PreparedStatement pstmt = manager.getConnection().prepareStatement(sql);
            pstmt.setString(1, symptom.getName());

            pstmt.executeUpdate();

            System.out.println("Symptom added successfully: " + symptom.getName());

            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
