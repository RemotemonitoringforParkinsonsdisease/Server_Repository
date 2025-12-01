package ui;

import POJOs.*;
import jdbcs.ManagerJDBC;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Server-side controller that manages the interaction with a single client
 * (patient or doctor application) through a network connection and the database.
 */
public class UI {
    private Connection connection;
    private ManagerJDBC manager;
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    /**
     * Creates a new UI instance for handling a client connection on the server.
     *
     * @param socket  the socket associated with the connected client
     * @param manager the JDBC manager used to access the database
     */
    public UI(Socket socket, ManagerJDBC manager) {
        this.manager = manager;
        this.connection = new Connection(socket);
    }

    /**
     * Entry point for handling a client session on the server side.
     * This method receives an initial integer from the client to identify the type of
     * application. If the value is 1, it initializes the patient application and
     * opens the patient pre-login menu; if the value is 2, it initializes the doctor
     * application and opens the doctor pre-login menu; for any other value it sends
     * the string "INVALID" to the client. When the processing finishes or an
     * exception is thrown, the connection resources are released in the finally block.
     */
    public void run() {
        try{
            logger.getLogger(UI.class.getName()).log(Level.INFO, "Socket acceptected");
            int message = connection.getReceiveViaNetwork().receiveInt();
            if(message == 1){
                connection.getSendViaNetwork().sendString("PATIENT");
                logger.getLogger(UI.class.getName()).log(Level.INFO, "Patient app initialized");
                patientPreLoggedMenu();
            } else if(message == 2){
                connection.getSendViaNetwork().sendString("DOCTOR");
                logger.getLogger(UI.class.getName()).log(Level.INFO, "Doctor app initialized");

                doctorPreLoggedMenu();
            }else {
                connection.getSendViaNetwork().sendString("INVALID");
            }
        }catch(Exception e){
            logger.getLogger(UI.class.getName()).log(Level.WARNING, "" + e.getMessage());
        }finally {
            connection.releaseResources();
        }

    }

    /**
     * Handles the pre-login menu for the patient application, receiving an option
     * from the client and redirecting the flow to patient registration, patient
     * login or application exit.
     *
     * @throws IOException if an error occurs while receiving data from the client
     */
    private void patientPreLoggedMenu() throws IOException {
        logger.getLogger(UI.class.getName()).log(Level.WARNING, "Patient pre logged menu");
        int option = connection.getReceiveViaNetwork().receiveInt();
        switch (option){
            case 1: logger.getLogger(UI.class.getName()).log(Level.INFO, "Registering patient"); registerPatient(); break;
            case 2: logger.getLogger(UI.class.getName()).log(Level.INFO, "Logging in patient"); logInPatient(); break;
            case 3: logger.getLogger(UI.class.getName()).log(Level.INFO, "Exiting patient"); exitMenu(); break;
        }
    }
//
    /**
     * Handles the pre-login menu for the doctor application in a loop, receiving
     * options from the client and redirecting the flow to doctor registration,
     * doctor login or application exit.
     */
    private void doctorPreLoggedMenu() {
        do{
            logger.getLogger(UI.class.getName()).log(Level.WARNING, "Doctor pre logged menu");
            int option = connection.getReceiveViaNetwork().receiveInt();
            switch (option){
                case 1: logger.getLogger(UI.class.getName()).log(Level.INFO, "Registering doctor");registerDoctor(); break;
                case 2: logger.getLogger(UI.class.getName()).log(Level.INFO, "Loggin in doctor");loginDoctor(); break;
                case 3: logger.getLogger(UI.class.getName()).log(Level.INFO, "Exiting doctor");exitMenu(); break;
            }

        } while (true);

    }

    /**
     * Registers a new patient in the system. It checks whether the email provided
     * by the client already exists, creates the user record if necessary, assigns
     * a random available doctor, receives the patient data, stores it in the
     * database and finally opens the logged-in menu for that patient.
     *
     * @throws IOException if an error occurs while sending or receiving data
     */
    public void registerPatient() throws IOException {
        String email = connection.getReceiveViaNetwork().receiveString();
        if (manager.getUserJDBC().getUserByEmail(email) != null) {

            Integer userID = manager.getUserJDBC().getUserIdByEmail(email);

            if(manager.getPatientJDBC().getPatientIdByUserId(userID) != null){
                connection.getSendViaNetwork().sendString("EMAIL ERROR");
                return;
            }else {
                logger.getLogger(UI.class.getName()).log(Level.INFO, "Email: " + email + " is OK");
                connection.getSendViaNetwork().sendString("EMAIL OK");
            }
        } else{
            logger.getLogger(UI.class.getName()).log(Level.INFO, "Email: " + email + " is OK");
            manager.getUserJDBC().addUser(email);
            connection.getSendViaNetwork().sendString("EMAIL OK");
        }
        Integer userId = manager.getUserJDBC().getUserIdByEmail(email);
        Integer doctorId = manager.getDoctorJDBC().getRandomDoctorId();
        if(doctorId == null){
            String message = "NO DOCTOR AVAILABLE";
            connection.getSendViaNetwork().sendString(message);
            logger.getLogger(UI.class.getName()).log(Level.WARNING, "NO DOCTOR AVAILABLE");
            return;
        }
        else{
            String message = "DOCTOR ASSIGNED";
            connection.getSendViaNetwork().sendString(message);
            logger.getLogger(UI.class.getName()).log(Level.INFO, "DOCTOR ASSIGNED");
        }
        Patient patient = connection.getReceiveViaNetwork().receiveRegisteredPatient();
        patient.setUserId(userId);
        patient.setDoctorId(doctorId);
        manager.getPatientJDBC().addPatient(patient);
        Integer patientId = manager.getPatientJDBC().getPatientIdByUserId(userId);
        patient.setPatientId(patientId);
        logger.getLogger(UI.class.getName()).log(Level.INFO, "Patient registered " +patient.getFullName());
        patientLoggedInMenu(patient);
    }

    /**
     * Handles the main menu for a logged-in patient, sending the patient data
     * to the client and processing the selected options to view patient information,
     * create a new report or log out and return to the patient pre-login menu.
     *
     * @param patient the patient who has just registered or logged in
     * @throws IOException if an error occurs while sending or receiving data
     */
    public void patientLoggedInMenu(Patient patient) throws IOException {
        connection.getSendViaNetwork().sendLoggedPatient(patient);
        int option;
        do {
            switch (option = connection.getReceiveViaNetwork().receiveInt()){
                case 1: seePatientInfo(patient); break;
                case 2: createReport(patient); break;
                case 3:
                    logger.getLogger(UI.class.getName()).log(Level.INFO, "Patient logging out: " + patient.getFullName());
                    this.patientPreLoggedMenu();
            }
        } while (option != 3);
    }

    /**
     * Sends detailed information about a patient to the client, including the
     * associated user data and, if available, the full name of the assigned doctor.
     *
     * @param patient the patient whose information is requested
     * @throws IOException if an error occurs while sending data to the client
     */
    private void seePatientInfo(Patient patient) throws IOException {
        User user = manager.getUserJDBC().getUserById(patient.getUserId());
        connection.getSendViaNetwork().sendUser(user);
        if(patient.getDoctorId() != null){
            Doctor doctor = manager.getDoctorJDBC().getDoctorByDoctorId(patient.getDoctorId());
            connection.getSendViaNetwork().sendString(doctor.getFullName());
        } else{
            connection.getSendViaNetwork().sendString("NO DOCTOR");
        }
    }

    /**
     * Creates a new report for the given patient. It receives the report data
     * from the client, stores it in the database, retrieves the generated report
     * identifier, associates the report with the patient and notifies the client
     * that the report has been added.
     *
     * @param patient the patient for whom the report is created
     * @throws IOException if an error occurs while sending or receiving data
     */
    private void createReport(Patient patient) throws IOException {
        logger.getLogger(UI.class.getName()).log(Level.INFO, "Creating report for patient: " + patient.getFullName());
        Report report = connection.getReceiveViaNetwork().receiveReport();
        manager.getReportJDBC().addReport(report);
        report.setReportId(manager.getReportJDBC().getReportIdBySignalFilePath(report.getSignalsFilePath()));
        patient.addReport(report);
        logger.getLogger(UI.class.getName()).log(Level.INFO, "Report added for patient: " + patient.getFullName());
        connection.getSendViaNetwork().sendString("REPORT ADDED");
    }

    /**
     * Closes the application for the current client, releasing the network
     * resources associated with the connection and signalling that the client
     * has exited normally.
     */
    private void exitMenu() {
        logger.getLogger(UI.class.getName()).log(Level.INFO, "Exiting application: " + connection.getSocket().getInetAddress().toString());
        connection.releaseResources();
        throw new RuntimeException("Client exited normally");
    }

    /**
     * Registers a new doctor in the system. It checks whether the email provided
     * by the client already exists, creates the user record if necessary, receives
     * the doctor data, stores it in the database, assigns the generated doctor
     * identifier, initializes the list of patients and confirms the registration.
     */
    private void registerDoctor() {
        do{
            String email = connection.getReceiveViaNetwork().receiveString();
            logger.getLogger(UI.class.getName()).log(Level.INFO, "" +email);
            if (manager.getUserJDBC().getUserByEmail(email) != null) {

                Integer userID = manager.getUserJDBC().getUserIdByEmail(email);

                if(manager.getDoctorJDBC().getDoctorIdByUserId(userID) != null){
                    connection.getSendViaNetwork().sendString("EMAIL ERROR");
                    return;
                } else{
                    connection.getSendViaNetwork().sendString("EMAIL OK");
                }
            } else{
                logger.getLogger(UI.class.getName()).log(Level.INFO, "Email: " + email + " is OK");
                manager.getUserJDBC().addUser(email);
                String message = "EMAIL OK";
                connection.getSendViaNetwork().sendString(message);
            }
            Integer userId = manager.getUserJDBC().getUserIdByEmail(email);
            Doctor doctor = connection.getReceiveViaNetwork().receiveRegisteredDoctor();
            doctor.setUserId(userId);
            manager.getDoctorJDBC().addDoctor(doctor);
            Integer doctorId = manager.getDoctorJDBC().getDoctorIdByUserId(userId);
            doctor.setDoctorId(doctorId);
            doctor.setPatients(new ArrayList<>());
            logger.getLogger(UI.class.getName()).log(Level.INFO, "Doctor registered " + doctor.getFullName());
            return;
        } while(true);

    }

    /**
     * Manages the login process for a doctor. It receives the email and password
     * from the client, verifies that the corresponding user and doctor exist,
     * validates the password and, if correct, loads the doctor and their patients
     * and opens the logged-in doctor menu. In case of any error, it sends the
     * appropriate status message to the client and returns.
     */
    private void loginDoctor() {
        do{
            String doctorEmail = connection.getReceiveViaNetwork().receiveString();
            Integer userId = manager.getUserJDBC().getUserIdByEmail(doctorEmail);
            if (userId != null) {
                Integer doctorId = manager.getDoctorJDBC().getDoctorIdByUserId(userId);
                if(doctorId != null){
                    connection.getSendViaNetwork().sendString("EMAIL OK");
                    String password = connection.getReceiveViaNetwork().receiveString();
                    if(manager.getDoctorJDBC().getPasswordByDoctorId(doctorId).equals(password)){
                        connection.getSendViaNetwork().sendString("PASSWORD OK");
                        Doctor doctor = manager.getDoctorJDBC().getDoctorByDoctorId(doctorId);
                        doctor.setPatients(manager.getPatientJDBC().getPatientsByDoctorId(doctor.getDoctorId()));
                        doctorLoggedInMenu(doctor);

                    } else{
                        connection.getSendViaNetwork().sendString("PASSWORD ERROR");
                        return;
                    }
                } else{
                    connection.getSendViaNetwork().sendString("NO DOCTOR FOUND");
                    return;
                }
            } else{
                connection.getSendViaNetwork().sendString("NO USER FOUND");
                return;
            }

        } while (true);
    }

    /**
     * Handles the main menu for a logged-in doctor, sending the doctor data to
     * the client and processing the selected options to log out and return to
     * the pre-login menu or to open the patient selection and report menu.
     *
     * @param doctor the doctor who has successfully logged in
     */
    private void doctorLoggedInMenu(Doctor doctor) {
        connection.getSendViaNetwork().sendLoggedDoctor(doctor);
        int option;
        do{
            switch(option = connection.getReceiveViaNetwork().receiveInt()){
                case 0: doctorPreLoggedMenu(); break;
                case 1: doctorPatientMenu(); break;
            }
        } while(option != 0);
    }

    /**
     * Manages the doctor view of a specific patient's reports. It receives the
     * patient identifier from the client, sends the list of reports, and then
     * processes options to go back or to add a new observation to one of the
     * reports, updating the database and confirming the change to the client.
     */
    private void doctorPatientMenu() {
        Integer patientId = connection.getReceiveViaNetwork().receiveInt();
        List<Report> reports = manager.getReportJDBC().getReportsByPatientId(patientId);
        connection.getSendViaNetwork().sendReports(reports);
        logger.getLogger(UI.class.getName()).log(Level.INFO, "Reports sent to doctor");
        int option;
        do{
            switch (option = connection.getReceiveViaNetwork().receiveInt()){
                case 0: return;
                case 1:
                    Integer reportId = connection.getReceiveViaNetwork().receiveInt();
                    String doctorObservation = connection.getReceiveViaNetwork().receiveString();
                    logger.getLogger(UI.class.getName()).log(Level.INFO, "Adding observation: " + doctorObservation + " to reportId: " + reportId);
                    manager.getReportJDBC().updateDoctorObservation(reportId, doctorObservation);
                    connection.getSendViaNetwork().sendString("ADDED OBSERVATION: " + doctorObservation);
                    break;
            }
        } while (option != 0);
    }

    /**
     * Manages the login process for a patient. It receives the email and password
     * from the client, verifies that the corresponding user and patient exist,
     * validates the password and, if correct, loads the patient and opens the
     * logged-in patient menu. In case of error, it sends the appropriate status
     * message and returns to the patient pre-login menu.
     *
     * @throws IOException if an error occurs while sending or receiving data
     */
    private void logInPatient() throws IOException {
        String patientEmail = connection.getReceiveViaNetwork().receiveString();
        do{
            Integer userId = manager.getUserJDBC().getUserIdByEmail(patientEmail);
            if (userId != null) {
                Integer patientId = manager.getPatientJDBC().getPatientIdByUserId(userId);
                if (patientId != null) {
                    connection.getSendViaNetwork().sendString("EMAIL OK");
                    logger.getLogger(UI.class.getName()).log(Level.INFO, "email verified sent");
                    String password = connection.getReceiveViaNetwork().receiveString();
                    if (manager.getPatientJDBC().getPasswordByPatientId(patientId).equals(password)) {
                        connection.getSendViaNetwork().sendString("PASSWORD OK");
                        Patient patient = manager.getPatientJDBC().getPatientByPatientId(patientId);
                        logger.getLogger(UI.class.getName()).log(Level.INFO, "Patient logged in ");
                        patientLoggedInMenu(patient);

                    } else {
                        connection.getSendViaNetwork().sendString("PASSWORD ERROR");
                        this.patientPreLoggedMenu();
                    }
                } else {
                    connection.getSendViaNetwork().sendString("NO PATIENT FOUND");
                    this.patientPreLoggedMenu();
                }
            } else {
                connection.getSendViaNetwork().sendString("NO USER FOUND");
                this.patientPreLoggedMenu();
            }
        }  while(true);
    }
//TODO JAVADOC
    /*static {
        try {
            FileHandler fh = new FileHandler("server.log", true);
            fh.setFormatter(new SimpleFormatter());
            logger.addHandler(fh);
            logger.setLevel(Level.INFO);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Could not initialize log file", e);
        }
    }*/
}