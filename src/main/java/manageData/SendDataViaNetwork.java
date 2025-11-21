package manageData;

import POJOs.Patient;
import POJOs.Doctor;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SendDataViaNetwork {
    private final DataOutputStream dataOutputStream;

    public SendDataViaNetwork(Socket socket) {
        DataOutputStream dos = null;
        try {
            dos = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            Logger.getLogger(SendDataViaNetwork.class.getName()).log(Level.SEVERE, null, e);
        }
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

    public void sendPatient(Patient patient) {
        try {
            sendString(patient.getEmail());
            sendString(patient.getFullName());
            sendString(patient.getDob().toString());
            sendString(patient.getPassword());
        } catch (Exception e) {
            Logger.getLogger(SendDataViaNetwork.class.getName()).log(Level.SEVERE, "Error sending patient", e);
        }
    }

    public void sendDoctor(Doctor doctor) {
        try {
            sendString(doctor.getEmail());
            sendString(doctor.getFullName());
            sendString(doctor.getDob().toString());
            sendString(doctor.getPassword());
        } catch (Exception e) {
            Logger.getLogger(SendDataViaNetwork.class.getName()).log(Level.SEVERE, "Error sending doctor", e);
        }
    }
    public void releaseResources() {
        try {
            dataOutputStream.close();
        } catch (IOException ex) {
            Logger.getLogger(SendDataViaNetwork.class.getName()).log(Level.SEVERE, null, ex);
            }
    }

}
