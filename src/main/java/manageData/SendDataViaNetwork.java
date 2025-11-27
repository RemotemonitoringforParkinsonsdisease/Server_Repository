package manageData;

import POJOs.*;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SendDataViaNetwork {
    private DataOutputStream dataOutputStream;

    public SendDataViaNetwork(DataOutputStream dos) {
        this.dataOutputStream = dos;
    }

    public void sendString(String s) {
        try {
            dataOutputStream.writeUTF(s);
        } catch (IOException e) {
            Logger.getLogger(SendDataViaNetwork.class.getName()).log(Level.SEVERE, "Error sending String", e);
        }
    }

    public void sendInt(int i) {
        try {
            dataOutputStream.writeInt(i);
        } catch (IOException e) {
            Logger.getLogger(SendDataViaNetwork.class.getName()).log(Level.SEVERE, "Error sending int", e);
        }
    }

    public void sendLoggedPatient(Patient patient) {
        try {
            sendInt(patient.getPatientId());
            sendInt(patient.getUserId());
            sendInt(patient.getDoctorId());
            sendString(patient.getPatientPassword());
            sendString(patient.getFullName());
            sendString(patient.getDob().toString());
            sendReports(patient.getReports());
            //Añadir pasar reports
            //Pasar User en seeMyInfo
        } catch (Exception e) {
            Logger.getLogger(SendDataViaNetwork.class.getName()).log(Level.SEVERE, "Error sending patient", e);
        }
    }

    public void sendLoggedDoctor(Doctor doctor) {
        try {
            sendInt(doctor.getUserId());
            sendInt(doctor.getDoctorId());
            sendString(doctor.getFullName());
            sendString(doctor.getDoctorPassword());
            sendString(doctor.getDob().toString());
            sendPatients(doctor.getPatients());
        } catch (Exception e) {
            Logger.getLogger(SendDataViaNetwork.class.getName()).log(Level.SEVERE, "Error sending doctor", e);
        }
    }
    public void sendPatients(List<Patient> patients) {
        if(patients == null){
            sendInt(0);
            return;
        }
        sendInt(patients.size());
        for(Patient p : patients){
            sendPatientToDoctor(p);
        }
    }
    public void sendPatientToDoctor(Patient patient) {
        try {
            sendInt(patient.getPatientId());
            sendString(patient.getDob().toString());
            sendString(patient.getFullName());
        } catch (Exception e) {
            Logger.getLogger(SendDataViaNetwork.class.getName()).log(Level.SEVERE, "Error sending patient to doctor", e);
        }
    }

    public void sendLoggedInDoctor(Doctor doctor) {
        try {
            sendInt(doctor.getUserId());
            sendInt(doctor.getDoctorId());
            sendString(doctor.getFullName());
            sendString(doctor.getDoctorPassword());
            sendString(doctor.getDob().toString());
            sendPatients(doctor.getPatients());
        } catch (Exception e) {
            Logger.getLogger(SendDataViaNetwork.class.getName()).log(Level.SEVERE, "Error sending doctor", e);
        }
    }
    public void sendReports(List<Report> reports){
        try{
            if(reports == null){
                sendInt(0);
                return;
            }
            sendInt(reports.size());
            for (Report r : reports) {
                dataOutputStream.writeInt(r.getReportId());
                dataOutputStream.writeInt(r.getPatientId());
                dataOutputStream.writeUTF(r.getReportDate().toString());
                sendCSVFile(r.getSignalsFilePath());
                sendSymptoms(r.getSymptoms());
                dataOutputStream.writeUTF(r.getPatientObservation());
                dataOutputStream.writeUTF(r.getDoctorObservation());
                dataOutputStream.flush();
            }
        } catch (IOException e) {
            Logger.getLogger(SendDataViaNetwork.class.getName()).log(Level.SEVERE, "Error sending reports", e);
        }
    }
    public void sendSymptoms(List<Symptoms> symptoms) throws IOException{
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < symptoms.size(); i++) {
            sb.append(symptoms.get(i).name());
            if (i < symptoms.size() - 1) {
                sb.append(",");  // Añadir coma excepto en el último
            }
        }
        dataOutputStream.writeUTF(sb.toString());
    }

    public void releaseResources() {
        try {
            dataOutputStream.close();
        } catch (IOException ex) {
            Logger.getLogger(SendDataViaNetwork.class.getName()).log(Level.SEVERE, null, ex);
            }
    }

    public void sendCSVFile (String filePath) throws IOException {
        File file = new File(filePath);
        FileInputStream fis = new FileInputStream(file);

        // 1. Enviar el nombre del archivo
        dataOutputStream.writeUTF(file.getName());

        // 2. Enviar tamaño del archivo
        dataOutputStream.writeLong(file.length());

        // 3. Enviar contenido
        byte[] buffer = new byte[4096];
        int bytesRead;

        while ((bytesRead = fis.read(buffer)) != -1) {
            dataOutputStream.write(buffer, 0, bytesRead);
        }

        dataOutputStream.flush();
        fis.close();
    }

    public void sendUser(User user) {
        try {
            dataOutputStream.writeInt(user.getId());
            dataOutputStream.writeUTF(user.getEmail());
        } catch (IOException e) {
            System.err.println("Error al leer el flujo de entrada: " + e.getMessage());
        }
    }
}
