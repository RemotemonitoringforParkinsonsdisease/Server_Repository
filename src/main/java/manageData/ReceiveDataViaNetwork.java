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

/**
 * Handles the reception of data from the client over a network connection,
 * wrapping a DataInputStream and providing higher level methods to read
 * primitive values, domain objects, CSV files and symptom lists.
 */
public class ReceiveDataViaNetwork {
    private DataInputStream dataInputStream;

    /**
     * Creates a new receiver bound to the given data input stream, which will
     * be used to read all incoming data from the client over the network.
     *
     * @param dis the data input stream associated with the client connection
     */
    public ReceiveDataViaNetwork(DataInputStream dis) {
        this.dataInputStream = dis;
    }

    /**
     * Receives a UTF string from the client using the underlying data input
     * stream. If an I/O error occurs while reading, the error is logged and
     * the method returns null.
     *
     * @return the string received from the client, or null if an error occurs
     */
    public String receiveString() {
        try {
            return dataInputStream.readUTF();
        } catch (IOException ex) {
            Logger.getLogger(ReceiveDataViaNetwork.class.getName()).log(Level.SEVERE, "Error receiving String", ex);
            return null;
        }
    }

    /**
     * Receives an integer from the client using the underlying data input
     * stream. If an I/O error occurs while reading, the error is logged and
     * the method returns −1 as a default error value.
     *
     * @return the integer received from the client, or −1 if an error occurs
     */
    public int receiveInt() {
        try {
            return dataInputStream.readInt();
        } catch (IOException ex) {
            Logger.getLogger(ReceiveDataViaNetwork.class.getName()).log(Level.SEVERE, "Error receiving int", ex);
            return -1;
        }
    }

    /**
     * Receives the data of a newly registered doctor from the client and
     * builds a Doctor object with the full name, password and date of birth
     * sent over the network. If any error occurs during reception or parsing,
     * the error is logged and the method returns null.
     *
     * @return the Doctor object created from the received data, or null on error
     */
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

    /**
     * Receives the data of a newly registered patient from the client and
     * builds a Patient object with the password, full name and date of birth
     * sent over the network. If any error occurs during reception or parsing,
     * the error is logged and the method returns null.
     *
     * @return the Patient object created from the received data, or null on error
     */
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

    /**
     * Receives a report from the client by reading the patient identifier,
     * report date, list of symptoms, patient and doctor observations and the
     * associated CSV signal file. With this information it builds and returns
     * a Report object. If an I/O error occurs while reading from the stream,
     * the error is printed and the method returns null.
     *
     * @return the Report object created from the received data, or null on error
     * @throws IOException if a low-level I/O error occurs outside the handled block
     */
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

    /**
     * Receives a CSV file from the client, storing it on the server file system
     * under the server_files folder. The method reads the file name and size,
     * then streams the file content to disk and finally returns the path on
     * the server where the file has been saved.
     *
     * @return the absolute or relative path of the stored CSV file on the server
     * @throws IOException if an error occurs while creating directories or reading the file data
     */
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
        return filePath.toString();
    }

    /**
     * Receives a list of symptoms from the client as a single UTF string,
     * where individual symptoms are separated by commas, and converts it
     * into a list of Symptoms enum values. If the string is empty or an
     * error occurs, the method returns an empty list.
     *
     * @return a list of Symptoms parsed from the received string, possibly empty
     * @throws IOException if a low-level I/O error occurs outside the handled block
     */
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
