package manageData;

import POJOs.*;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReceiveDataViaNetwork {
    private DataInputStream dataInputStream;

    public ReceiveDataViaNetwork(DataInputStream dis) {
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
            Integer patientId = dataInputStream.readInt();
            String date = dataInputStream.readUTF();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate reportDate = LocalDate.parse(date, formatter);
            List<Symptoms> symptoms = receiveSymptoms();
            String patientObservation = dataInputStream.readUTF();
            String doctorObservation = dataInputStream.readUTF();
            String signalsFilePath = receiveCSVFile();
            report = new Report(patientId, reportDate, patientObservation, doctorObservation, symptoms, signalsFilePath);
        } catch (IOException e) {
            System.err.println("Error al leer el flujo de entrada: " + e.getMessage());
        }
        return report;
    }


    public String receiveCSVFile() throws IOException {

        String fileName = dataInputStream.readUTF();
        long fileSize = dataInputStream.readLong();

        String folder = "server_files/";
        Files.createDirectories(Paths.get(folder));

        Path filePath = Paths.get(folder + fileName);
        FileOutputStream fos = new FileOutputStream(filePath.toFile());

        byte[] buffer = new byte[4096];
        long remaining = fileSize;
        int bytesRead;

        while (remaining > 0 && (bytesRead = dataInputStream.read(buffer, 0, (int)Math.min(buffer.length, remaining))) != -1) {
            fos.write(buffer, 0, bytesRead);
            remaining -= bytesRead;
        }

        fos.close();
        return filePath.toString(); // ruta en el servidor
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
}
