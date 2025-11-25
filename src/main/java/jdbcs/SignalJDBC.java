package jdbcs;

import POJOs.Signal;
import POJOs.SignalType;
import managers.SignalManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SignalJDBC implements SignalManager {

    private ManagerJDBC manager;

    // Constructor
    public SignalJDBC(ManagerJDBC manager) {
        this.manager = manager;
    }

    // Método para agregar una señal
    public void addSignal(Signal signal) {
        String sql = "INSERT INTO signal (report_id, signal_type, values, sampling_rate) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = manager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, signal.getReportId());
            stmt.setString(2, signal.getSignalType().name());  // Guardamos el tipo de señal como String
            stmt.setString(3, signal.intValuesToString());  // Guardamos los valores como String
            stmt.setInt(4, signal.getSamplingRate());  // Guardamos la tasa de muestreo
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Método para obtener una señal por su ID
    public Signal getSignalById(Integer signalId) {
        String sql = "SELECT * FROM signal WHERE signal_id=?";
        try (PreparedStatement stmt = manager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, signalId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                SignalType signalType = SignalType.valueOf(rs.getString("signal_type"));
                String valuesString = rs.getString("values");
                List<Integer> values = new ArrayList<>();
                if (valuesString != null && !valuesString.isEmpty()) {
                    values = null;//signal.stringToIntValues(valuesString);  // Convertimos el string a lista de enteros
                }
                Integer samplingRate = rs.getInt("sampling_rate");

                Signal signal = new Signal(signalId, signalType);
                signal.setValues(values);
                signal.setSamplingRate(samplingRate);

                return signal;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Método para obtener todas las señales
    public List<Signal> readSignals() {
        List<Signal> signals = new ArrayList<>();
        String sql = "SELECT * FROM signal";
        try (Statement stmt = manager.getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Integer signalId = rs.getInt("signal_id");
                SignalType signalType = SignalType.valueOf(rs.getString("signal_type"));
                String valuesString = rs.getString("values");
                List<Integer> values = new ArrayList<>();
                if (valuesString != null && !valuesString.isEmpty()) {
                    values =  null; //signal.stringToIntValues(valuesString);  // Convertimos el string a lista de enteros
                }
                Integer samplingRate = rs.getInt("sampling_rate");

                Signal signal = new Signal(signalId, signalType);
                signal.setValues(values);
                signal.setSamplingRate(samplingRate);

                signals.add(signal);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return signals;
    }

    // Método para obtener señales asociadas a un reporte específico
    public List<Signal> getSignalsByReport(Integer reportId) {
        List<Signal> signals = new ArrayList<>();
        String sql = "SELECT * FROM signal WHERE report_id=?";
        try (PreparedStatement stmt = manager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, reportId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Integer signalId = rs.getInt("signal_id");
                SignalType signalType = SignalType.valueOf(rs.getString("signal_type"));
                String valuesString = rs.getString("values");
                List<Integer> values = new ArrayList<>();
                if (valuesString != null && !valuesString.isEmpty()) {
                    values = null;//signal.stringToIntValues(valuesString);  // Convertimos el string a lista de enteros
                }
                Integer samplingRate = rs.getInt("sampling_rate");

                Signal signal = new Signal(signalId, signalType);
                signal.setValues(values);
                signal.setSamplingRate(samplingRate);

                signals.add(signal);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return signals;
    }


}
