package ui;

import POJOs.*;
import jdbcs.ManagerJDBC;
import manageData.ReceiveDataViaNetwork;
import manageData.SendDataViaNetwork;
import managers.PatientManager;
import managers.DoctorManager;
import managers.ReportManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class UI {
    private Connection connection;
    private JDBCConnection jdbcConnection;
    private ManagerJDBC manager;

    public UI(Socket socket, ManagerJDBC manager) {
        this.manager = manager;
        this.connection = new Connection(socket);
        this.jdbcConnection = new JDBCConnection(manager);
    }


    public void run(){
        try{
            System.out.println("Socket acceptected");
            int message = connection.getReceiveViaNetwork().receiveInt();
            if(message == 1){
                connection.getSendViaNetwork().sendString("PATIENT");
                patientMainMenu();
            } else if(message == 2){
                connection.getSendViaNetwork().sendString("DOCTOR");
                doctorMainMenu();
            }else {
                connection.getSendViaNetwork().sendString("INVALID");
            }
        }catch(IOException e){
            System.out.println("Error during communication: " + e.getMessage());
        }finally {
            connection.releaseResources();
        }

    }
    public void startConnection(){
    }

    // ---------------------- MENÚ PRINCIPAL ----------------------
    public void mainMenu() {
        boolean running = true;
        while (running) {
            System.out.println("1. Patient");
            System.out.println("2. Doctor");
            System.out.println("3. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine(); // limpiar buffer

            switch (choice) {
                case 1 -> patientMainMenu();
                case 2 -> doctorMainMenu();
                case 3 -> running = false;
                default -> System.out.println("Option invalid, try again.");
            }
        }
    }

    private void patientMainMenu() {
       int option = connection.getReceiveViaNetwork().receiveInt();
       switch (option){
           case 1: registerPatient(); break;
           case 2: loginPatient(); break;
           case 3: exitMenu(); break;
       }
    }
    private void doctorMainMenu() {
        int option = connection.getReceiveViaNetwork().receiveInt();
        switch (option){
            case 1: registerDoctor(); break;
            case 2: loginDoctor(); break;
            case 3: exitMenu(); break;
        }
    }

/**/
    public void registerPatient() {
        String email = connection.getReceiveViaNetwork().receiveString(); // email
        if (manager.getUserJDBC().getUserByEmail(email) != null) {

            System.out.println("User already exists");
            //userId = cogerElIDDeUsuario existente

            if(manager.getPatientJDBC().getPatientByUserId(userID) != null){
                System.out.println("Patient already exists");
                connection.getSendViaNetwork().sendString("EMAIL ERROR");
                return;
            }
            connection.getSendViaNetwork().sendString("EMAIL OK");
        } else{
            manager.getUserJDBC().addUser(new User(email));
        }
        Integer userId = manager.getUserJDBC().getUserIdByEmail(email);
        Patient patient = connection.getReceiveViaNetwork().receiveRegisteredPatient();
        patient.setUserId(userId);
        //TODO:Insertamos patient en DB
        connection.getReceiveViaNetwork().receiveString().equals("PATIENT REGISTERED");


    }
    public void patientLoggedInMenu(Patient patient) {

        Integer patientId = manager.getPatientJDBC().getPatientIdByUserId(patient.getUserId());
        patient.setPatientId(patientId);
        System.out.println("Sending patient to app");
        if(connection.getReceiveViaNetwork().receiveString().equals("PATIENT LOGGED")){
            connection.getSendViaNetwork().sendNewPatient(patient);
            System.out.println("Patient logged successfully.");

        }
    }
/**/


    private Patient loginPatient() {
        System.out.println("Enter email:");
        String email = receiveDataViaNetwork.receiveString();  // leer desde socket
        Patient patient = manager.getPatientJDBC().getPatientByEmail(email);
        if (patient == null) {
            System.out.println("Email not found.");
            sendDataViaNetwork.sendString("Email not found"); // responder al cliente
            return null;
        }

        System.out.println("Enter password:");
        String password = receiveDataViaNetwork.receiveString(); // leer desde socket
        if (!patient.getPassword().equals(password)) {
            System.out.println("Incorrect password.");
            sendDataViaNetwork.sendString("Incorrect password"); // responder al cliente
            return null;
        }

        System.out.println("Login successful.");
        sendDataViaNetwork.sendString("Login successful"); // responder al cliente
        return patient;
    }


    private void patientLoggedInMenu(Patient patient) {
        boolean running = true;
        while (running) {
            System.out.println("Patient Logged In Menu:");
            System.out.println("1. My Information");
            System.out.println("2. See Reports");
            System.out.println("3. Initiate Report");
            System.out.println("4. Logout");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> seeMyInformation(patient);
                case 2 -> seeReports(patient);
                case 3 -> initiateReport(patient);
                case 4 -> running = false;
                default -> System.out.println("Option invalid.");
            }
        }
    }

    private void seeMyInformation(Patient patient) {
        System.out.println("Full Name: " + patient.getFullName());
        System.out.println("DOB: " + patient.getDob());
        System.out.println("Email: " + patient.getEmail());
        System.out.println("Password: " + patient.getPassword());
    }

    private void seeReports(Patient patient) {
        List<Report> reports = manager.getReportJDBC().getReportsByPatient(patient.getId());
        if (reports.isEmpty()) {
            System.out.println("No reports found.");
        } else {
            for (Report r : reports) {
                System.out.println("Report ID: " + r.getReportId());
                System.out.println("Patient Observation: " + r.getPatientObservation());
                System.out.println("Doctor Observation: " + r.getDoctorObservation());
                System.out.println("-----------");
            }
        }
    }

    private void initiateReport(Patient patient) {
        Report report = new Report(patient, LocalDate.now(), "", "");
        // Aquí llamar a métodos para: recordSignals(report), chooseSymptoms(report), addObservations(report)
        // Una vez completado:
        manager.getReportJDBC().addReport(report);
        System.out.println("Report sent successfully.");
    }

    // ---------------------- MENÚ DE DOCTOR ----------------------
    private void doctorMainMenu() {
        boolean doctorMenu = true;
        while (doctorMenu) {
            System.out.println("Doctor Menu:");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Back");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> registerDoctor();
                case 2 -> {
                    Doctor doctor = loginDoctor();
                    if (doctor != null) doctorLoggedInMenu(doctor);
                }
                case 3 -> doctorMenu = false;
                default -> System.out.println("Option invalid.");
            }
        }
    }


/**/
    private void registerDoctor() {
        String email = connection.getReceiveViaNetwork().receiveString(); // email

        if (manager.getUserJDBC().getUserByEmail(email) != null) {

            System.out.println("User already exists");
            //userId = cogerElIDDeUsuario existente

            if(manager.getDoctorJDBC().getDoctorByUserId(userID) != null){
                System.out.println("Doctor already exists");
                connection.getSendViaNetwork().sendString("EMAIL ERROR");
                return;
            }
            connection.getSendViaNetwork().sendString("EMAIL OK");
        } else{
            manager.getUserJDBC().addUser(new User(email));
        }
        Integer userId = manager.getUserJDBC().getUserIdByEmail(email);
        Doctor doctor = connection.getReceiveViaNetwork().receiveRegisteredDoctor();
        doctor.setUserId(userId);
        //TODO: Insertamos doctor en DB
        Integer doctorId = manager.getDoctorJDBC().getDoctorIdByUserId(userId);
        doctor.setDoctorId(doctorId);
        System.out.println("Sending doctor to app");
        doctor.setPatients(new ArrayList<>());
        connection.getSendViaNetwork().sendLoggedDoctor(doctor);
    }

    private Doctor loginDoctor() {
        System.out.println("Enter email:");
        String email = scanner.nextLine();
        Doctor doctor = manager.getDoctorJDBC().getDoctorByEmail(email);
        if (doctor == null) {
            System.out.println("Email not found.");
            return null;
        }

        System.out.println("Enter password:");
        String password = scanner.nextLine();
        if (!doctor.getPassword().equals(password)) {
            System.out.println("Incorrect password.");
            return null;
        }

        System.out.println("Login successful.");
        return doctor;
    }

    private void doctorLoggedInMenu(Doctor doctor) {
        boolean running = true;
        while (running) {
            System.out.println("Doctor Logged In Menu:");
            System.out.println("1. See Patient List");
            System.out.println("2. Logout");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> seePatientList(doctor);
                case 2 -> running = false;
                default -> System.out.println("Option invalid.");
            }
        }
    }

    /**/

    private void seePatientList(Doctor doctor) {
        List<Patient> patients = manager.getPatientJDBC().getPatientsByDoctor(doctor.getId());
        for (Patient p : patients) {
            System.out.println("ID: " + p.getId() + " Name: " + p.getFullName() + " Email: " + p.getEmail());
            List<Report> reports = manager.getReportJDBC().getReportsByPatient(p.getId());
            for (Report r : reports) {
                System.out.println("   Report ID: " + r.getReportId());
                System.out.println("   Patient Obs: " + r.getPatientObservation());
                System.out.println("   Doctor Obs: " + r.getDoctorObservation());
            }
        }
    }

    // Inicia la grabación de señales
    private void recordSignals(Report report) {
        // Aquí se simula la grabación de señales; si tuvieras integración con Bitalino, pondrías la lógica real
        System.out.println("Recording signals...");
        // Por ejemplo, puedes inicializar señales vacías
        report.setSignals(new HashSet<>()); // Vacío por ahora, luego puedes agregar Signal reales
        System.out.println("Signals recorded successfully.");
    }

    private void addObservations(Report report) {
        System.out.println("Enter your observations for this report:");
        String obs = scanner.nextLine();
        report.setPatientObservation(obs);
    }

    private void chooseSymptoms(Report report) {
        List<Symptoms> chosenSymptoms = new ArrayList<>();
        boolean done = false;

        Symptoms[] allSymptoms = Symptoms.values();

        while (!done) {
            System.out.println("Select a symptom (enter 0 to finish):");
            for (int i = 0; i < allSymptoms.length; i++) {
                System.out.println((i + 1) + ". " + allSymptoms[i].name());
            }

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, try again.");
                continue;
            }

            if (choice == 0) {
                done = true;
            } else if (choice >= 1 && choice <= allSymptoms.length) {
                Symptoms selected = allSymptoms[choice - 1];
                if (!chosenSymptoms.contains(selected)) {
                    chosenSymptoms.add(selected);
                    System.out.println(selected.name() + " added.");
                } else {
                    System.out.println(selected.name() + " already selected.");
                }
            } else {
                System.out.println("Invalid choice, try again.");
            }
        }

        report.setSymptoms(chosenSymptoms);
    }


}
