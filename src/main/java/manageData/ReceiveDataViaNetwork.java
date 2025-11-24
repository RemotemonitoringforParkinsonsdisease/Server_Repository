package manageData;

import POJOs.*;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReceiveDataViaNetwork {
    private final DataInputStream dataInputStream;

    public ReceiveDataViaNetwork(Socket socket) {
        DataInputStream dis = null;
        try {
            dis = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            Logger.getLogger(ReceiveDataViaNetwork.class.getName()).log(Level.SEVERE, null, e);
        }
        this.dataInputStream = dis;
    }

    public String receiveString() {
        try {
            return dataInputStream.readUTF();
        } catch (IOException ex) {
            Logger.getLogger(ReceiveDataViaNetwork.class.getName()).log(Level.SEVERE, "Error receiving String", ex);
            return null;
        }
    }

    public int receiveInt() {
        try {
            return dataInputStream.readInt();
        } catch (IOException ex) {
            Logger.getLogger(ReceiveDataViaNetwork.class.getName()).log(Level.SEVERE, "Error receiving int", ex);
            return -1; // Valor por defecto en error
        }
    }

    public Doctor receiveRegisteredDoctor() {
        try {
            String fullName = receiveString();
            String password = receiveString();
            String date = receiveString();
            LocalDate dob = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            return new Doctor(fullName, password, dob);
        } catch (Exception e) {
            Logger.getLogger(ReceiveDataViaNetwork.class.getName()).log(Level.SEVERE, "Error receiving doctor", e);
            return null;
        }
    }
    public Patient receiveRegisteredPatient(){
        Patient patient = null;
        try{
            String patientPassword = receiveString();
            String fullName = receiveString();
            String date = receiveString();
            LocalDate dob = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            patient = new Patient(patientPassword,fullName,dob);
        } catch(Exception e){
            Logger.getLogger(ReceiveDataViaNetwork.class.getName()).log(Level.SEVERE, "Error receiving registered patient", e);
        }
        return patient;
    }

    public Report receiveReport() throws IOException{
        Report report = null;

        try {
            Integer reportId = dataInputStream.readInt();
            Integer patientId = dataInputStream.readInt();
            String date = dataInputStream.readUTF();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate reportDate = LocalDate.parse(date, formatter);
            List<Signal> signals = receiveSignals();
            List<Symptoms> symptoms = receiveSymptoms();
            String patientObservation = dataInputStream.readUTF();
            String doctorObservation = dataInputStream.readUTF();

            report = new Report(reportId, patientId, reportDate, signals, symptoms, patientObservation, doctorObservation);
        } catch (IOException e) {
            System.err.println("Error al leer el flujo de entrada: " + e.getMessage());
        }
        return report;
    }

    public List<Signal> receiveSignals() throws IOException{
        List<Signal> signals = new ArrayList<>();
        try {
            int numSignals = dataInputStream.readInt();
            if (numSignals == 0) {
                return signals;
            }

            for (int i = 0; i < numSignals; i++) {
                Integer signalId = dataInputStream.readInt();
                String typeSignal = dataInputStream.readUTF();
                SignalType signalType = SignalType.valueOf(typeSignal);
                Integer samplingRate = dataInputStream.readInt();
                List<Integer> values = receiveListOfIntegerValues();
                Signal signal = new Signal(signalId, signalType, samplingRate, values);
                signals.add(signal);
            }
            return signals;
        } catch (IOException e) {
            System.out.println("Error al leer el flujo de entrada " + e.getMessage());
        }
        return signals;
    }

    public List<Integer> receiveListOfIntegerValues() throws IOException {
        List<Integer> values = new ArrayList<>();
        int numValues = dataInputStream.readInt();
        if (numValues == 0) {
            return values;
        }
        for (int i = 0; i < numValues; i++) {
            Integer value = dataInputStream.readInt();
            values.add(value);
        }
        return values;
    }


    public List<Symptoms> receiveSymptoms() throws IOException{
        List<Symptoms> symptoms = new ArrayList<>();
        try {
            String symptomsLine = dataInputStream.readUTF();
            if (symptomsLine.isEmpty()) {
                return symptoms;
            }
            for (String s : symptomsLine.split(",")) {
                symptoms.add(Symptoms.valueOf(s.trim()));
            }
            return symptoms;
        } catch (IOException e) {
            System.err.println("Error al leer el flujo de entrada: " + e.getMessage());
        }
        return symptoms;
    }

    public void releaseResources() {
        try {
            if (dataInputStream != null) {
                dataInputStream.close();
            }
        } catch (IOException e) {
            System.err.println("Error al liberar los recursos: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
