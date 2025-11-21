package manageData;

import POJOs.Patient;
import POJOs.Doctor;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    public Patient receivePatient() {
        try {
            String email = receiveString();
            String fullName = receiveString();
            String date = receiveString();
            LocalDate dob = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String password = receiveString();
            return new Patient(email, fullName, dob, password, "P");
        } catch (Exception e) {
            Logger.getLogger(ReceiveDataViaNetwork.class.getName()).log(Level.SEVERE, "Error receiving patient", e);
            return null;
        }
    }

    public Doctor receiveDoctor() {
        try {
            String email = receiveString();
            String fullName = receiveString();
            String date = receiveString();
            LocalDate dob = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String password = receiveString();
            return new Doctor(email, password, fullName, dob, null);
        } catch (Exception e) {
            Logger.getLogger(ReceiveDataViaNetwork.class.getName()).log(Level.SEVERE, "Error receiving doctor", e);
            return null;
        }
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
