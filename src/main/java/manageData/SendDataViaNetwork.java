package manageData;

import POJOs.Patient;
import POJOs.Doctor;
import POJOs.Report;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
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

    public void sendNewPatient(Patient patient) {
        try {
            sendInt(patient.getPatientId());
            sendInt(patient.getUserId());
            sendInt(patient.getDoctorId());
            sendString(patient.getPatientPassword());
            sendString(patient.getFullName());
            sendString(patient.getDob().toString());
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
    public void sendReports(List<Report> reports) throws IOException{
        sendInt(reports.size());
        for (Report r : reports) {
            dataOutputStream.writeInt(r.getReportId());
            dataOutputStream.writeInt(r.getPatientId());
            dataOutputStream.writeUTF(r.getReportDate().toString());
            sendSymptoms(r.getSymptoms());
            sendSignals(r.getSignals());
            dataOutputStream.writeUTF(r.getPatientObservation());
            dataOutputStream.writeUTF(r.getDoctorObservation());
            dataOutputStream.flush();
        }
    }
    public void sendSignals(List<Signal> signals) throws IOException{
        dataOutputStream.writeInt(signals.size());

        for (Signal signal : signals) {
            dataOutputStream.writeInt(signal.getSignalId());
            dataOutputStream.writeUTF(signal.getSignalType().name());
            sendListOfIntegerValues(signal.getValues());
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
