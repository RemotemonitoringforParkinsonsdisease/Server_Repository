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
    private DataInputStream dataInputStream;

    public ReceiveDataViaNetwork (Socket socket) {
        try {
            this.dataInputStream = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            Logger.getLogger(ReceiveDataViaNetwork.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public String receiveString() {
        try {
            return dataInputStream.readUTF();
        } catch (IOException ex) {
            System.err.println("Error recibing String");
            Logger.getLogger(ReceiveDataViaNetwork.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Patient receivePatient() {
        Patient patient = null;

        try {
            String email = dataInputStream.readUTF();
            String fullName = dataInputStream.readUTF();
            String date = dataInputStream.readUTF();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dob = LocalDate.parse(date, formatter);
            String password = dataInputStream.readUTF();

            patient = new Patient(email, fullName, dob, password, "P");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return patient;
    }

/*    public Doctor receiveDoctor(ReceiveDataViaNetwork recieveDataViaNetwork, SendDataViaNetwork sendDataViaNetwork, UserManager userManager, DoctorManager doctorManager, PatientManager patientManager, SymptomsManager symptomsManager, InterpretationManager interpretationManager, Socket socket){
        Doctor doctor = null;
        try {
            int id = dataInputStream.readInt();
            String fullName = dataInputStream.readUTF();
            String date = dataInputStream.readUTF();
            String email = dataInputStream.readUTF();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dob = LocalDate.parse(date, formatter);
            doctor = new Doctor(id, name, surname, dob, email);
        } catch (EOFException ex) {
            System.out.println("All data have been correctly read.");
        } catch (IOException ex) {
            System.out.println("Unable to read from the client.");
            Logger.getLogger(ReceiveDataViaNetwork.class.getName()).log(Level.SEVERE, null, ex);
        }

        return doctor;
    }*/

    public int receiveInt() throws IOException{
        return dataInputStream.readInt();
    }
}
