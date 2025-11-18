package jdbcs;

import POJOs.Signal;
import POJOs.SignalType;
import managers.SignalManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SignalJDBC implements SignalManager {

    private ManagerJDBC manager;

    public SignalJDBC(ManagerJDBC manager) {
        this.manager = manager;
    }

    @Override
    public void addSignal(Signal signal) {
        String sql = "INSERT INTO Signal (signal_id, type, values) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = manager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, signal.getSignalId());
            stmt.setString(2, signal.getSignalType().name());
            stmt.setString(3, signal.valuesToString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Signal getSignalById(String signalId) {
        String sql = "SELECT * FROM Signal WHERE signal_id=?";
        try (PreparedStatement stmt = manager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, signalId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                SignalType type = SignalType.valueOf(rs.getString("type"));
                String[] valuesArray = rs.getString("values").split(",");
                List<Float> values = new ArrayList<>();
                for (String s : valuesArray) values.add(Float.parseFloat(s));
                Signal s = new Signal(type, signalId);
                s.setValues(values);
                return s;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
