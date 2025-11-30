package manageData;

import POJOs.*;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles the sending of data to the client over a network connection, wrapping
 * a DataOutputStream and providing higher level methods to send primitive values,
 * domain objects, CSV files and symptom lists.
 */
public class SendDataViaNetwork {
    private DataOutputStream dataOutputStream;

    /**
     * Creates a new sender bound to the given data output stream, which will be
     * used to write all outgoing data to the client over the network.
     *
     * @param dos the data output stream associated with the client connection
     */
    public SendDataViaNetwork(DataOutputStream dos) {
        this.dataOutputStream = dos;
    }

    /**
     * Sends a UTF string to the client using the underlying data output stream.
     * If an I/O error occurs while writing, the error is logged and the method
     * finishes without throwing it.
     *
     * @param s the string to be sent to the client
     */
    public void sendString(String s) {
        try {
            dataOutputStream.writeUTF(s);
        } catch (IOException e) {
            Logger.getLogger(SendDataViaNetwork.class.getName()).log(Level.SEVERE, "Error sending String", e);
        }
    }

    /**
     * Sends an integer value to the client using the underlying data output
     * stream. If an I/O error occurs while writing, the error is logged and
     * the method finishes without throwing it.
     *
     * @param i the integer value to be sent to the client
     */
    public void sendInt(int i) {
        try {
            dataOutputStream.writeInt(i);
        } catch (IOException e) {
            Logger.getLogger(SendDataViaNetwork.class.getName()).log(Level.SEVERE, "Error sending int", e);
        }
    }

    /**
     * Sends the information of a logged-in patient to the client, including its
     * identifiers, password, full name, date of birth and the list of associated
     * reports. If an error occurs while sending any of these fields, the error
     * is logged and the method finishes without throwing it.
     *
     * @param patient the patient whose full logged-in data will be sent
     */
    public void sendLoggedPatient(Patient patient) {
        try {
            sendInt(patient.getPatientId());
            sendInt(patient.getUserId());
            sendInt(patient.getDoctorId());
            sendString(patient.getPatientPassword());
            sendString(patient.getFullName());
            sendString(patient.getDob().toString());
            sendReports(patient.getReports());
        } catch (Exception e) {
            Logger.getLogger(SendDataViaNetwork.class.getName()).log(Level.SEVERE, "Error sending patient", e);
        }
    }

    /**
     * Sends the information of a logged-in doctor to the client, including its
     * identifiers, name, password, date of birth and the list of assigned
     * patients. If an error occurs while sending any of these fields, the error
     * is logged and the method finishes without throwing it.
     *
     * @param doctor the doctor whose full logged-in data will be sent
     */
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

    /**
     * Sends a list of patients to the client. First it sends the size of the
     * list, and then for each patient calls the helper method that sends the
     * minimal information required by the doctor. If the list is null, it sends
     * a size of zero and returns.
     *
     * @param patients the list of patients to be sent, or null for no patients
     */
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

    /**
     * Sends the basic information of a patient that is relevant for the doctor
     * view, including the patient id, date of birth and full name. If an error
     * occurs while sending any of these fields, the error is logged and the
     * method finishes without throwing it.
     *
     * @param patient the patient whose basic data will be sent to the doctor
     */
    public void sendPatientToDoctor(Patient patient) {
        try {
            sendInt(patient.getPatientId());
            sendString(patient.getDob().toString());
            sendString(patient.getFullName());
        } catch (Exception e) {
            Logger.getLogger(SendDataViaNetwork.class.getName()).log(Level.SEVERE, "Error sending patient to doctor", e);
        }
    }

    /**
     * Sends a list of reports to the client. It first sends the number of
     * reports, and for each report sends identifiers, date, associated CSV
     * signal file, symptoms, patient observation and doctor observation. If
     * the list is null, it sends a size of zero and returns. Any I/O error
     * during sending is logged and does not propagate.
     *
     * @param reports the list of reports to be sent, or null for no reports
     */
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

    /**
     * Sends a list of symptoms to the client as a single UTF string, where
     * individual symptoms are separated by commas. This representation can
     * later be parsed by the receiver to reconstruct the list.
     *
     * @param symptoms the list of symptoms to be converted and sent
     * @throws IOException if an I/O error occurs while writing to the stream
     */
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

    /**
     * Sends a CSV file to the client over the network. The method first sends
     * the file name and its size in bytes, and then streams the content of the
     * file in blocks until the entire file has been transmitted. The caller
     * must ensure that the path is valid and points to an existing file.
     *
     * @param filePath the path of the CSV file on the server to be sent
     * @throws IOException if an error occurs while reading the file or writing to the stream
     */
    public void sendCSVFile (String filePath) throws IOException {
        File file = new File(filePath);
        FileInputStream fis = new FileInputStream(file);

        dataOutputStream.writeUTF(file.getName());

        dataOutputStream.writeLong(file.length());

        byte[] buffer = new byte[4096];
        int bytesRead;

        while ((bytesRead = fis.read(buffer)) != -1) {
            dataOutputStream.write(buffer, 0, bytesRead);
        }

        dataOutputStream.flush();
        fis.close();
    }

    /**
     * Sends the basic information of a user to the client, including its
     * identifier and email address. If an I/O error occurs while writing,
     * the error is printed to the standard error stream and the method
     * finishes without throwing it.
     *
     * @param user the user whose id and email will be sent to the client
     */
    public void sendUser(User user) {
        try {
            dataOutputStream.writeInt(user.getId());
            dataOutputStream.writeUTF(user.getEmail());
        } catch (IOException e) {
            System.err.println("Error al leer el flujo de entrada: " + e.getMessage());
        }
    }
}
