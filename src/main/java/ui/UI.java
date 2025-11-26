package ui;

import POJOs.*;
import jdbcs.ManagerJDBC;


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
    private ManagerJDBC manager;

    public UI(Socket socket, ManagerJDBC manager) {
        this.manager = manager;
        this.connection = new Connection(socket);
    }


    public void run(){
        try{
            System.out.println("Socket acceptected");
            int message = connection.getReceiveViaNetwork().receiveInt();
            if(message == 1){
                connection.getSendViaNetwork().sendString("PATIENT");
                patientPreLoggedMenu();
            } else if(message == 2){
                connection.getSendViaNetwork().sendString("DOCTOR");
                doctorPreLoggedMenu();
            }else {
                connection.getSendViaNetwork().sendString("INVALID");
            }
        }catch(Exception e){
            System.out.println("Error during communication: " + e.getMessage());
        }finally {
            connection.releaseResources();
        }

    }
    public void startConnection(){
        //TODO DOOOOOOOOOO
    }

    private void patientPreLoggedMenu() throws IOException {
       int option = connection.getReceiveViaNetwork().receiveInt();
       switch (option){
           case 1: System.out.println("Registering patient"); registerPatient(); break;
           case 2: System.out.println("Logging in patient"); logInPatient(); break;
           case 3: System.out.println("Exiting patient"); exitMenu(); break;
       }
    }
    private void doctorPreLoggedMenu() {
        do{
            System.out.println("Idle");
            int option = connection.getReceiveViaNetwork().receiveInt();
            switch (option){
                case 1: System.out.println("Registering doctor");registerDoctor(); break;
                case 2: System.out.println("Logging in doctor");loginDoctor(); break;
                case 3: System.out.println("Exiting doctor");exitMenu(); break;
            }

        } while (true);

    }

/**/
    public void registerPatient() throws IOException {
        String email = connection.getReceiveViaNetwork().receiveString(); // email
        if (manager.getUserJDBC().getUserByEmail(email) != null) {

            System.out.println("User already exists");
            Integer userID = manager.getUserJDBC().getUserIdByEmail(email);

            if(manager.getPatientJDBC().getPatientIdByUserId(userID) != null){
                System.out.println("Patient already exists");
                connection.getSendViaNetwork().sendString("EMAIL ERROR");
                return;
            }else {
                System.out.println("Email: " + email + " is OK");
                connection.getSendViaNetwork().sendString("EMAIL OK");
            }
        } else{
            System.out.println("Email: " + email + " is OK");
            manager.getUserJDBC().addUser(email);
            connection.getSendViaNetwork().sendString("EMAIL OK");
        }
        Integer userId = manager.getUserJDBC().getUserIdByEmail(email);
        Integer doctorId = manager.getDoctorJDBC().getRandomDoctorId();
        Patient patient = connection.getReceiveViaNetwork().receiveRegisteredPatient();
        patient.setUserId(userId);
        patient.setDoctorId(doctorId);
        System.out.println(patient);
        manager.getPatientJDBC().addPatient(patient);
        Integer patientId = manager.getPatientJDBC().getPatientIdByUserId(userId);
        patient.setPatientId(patientId);
        patientLoggedInMenu(patient);
    }
    public void patientLoggedInMenu(Patient patient) throws IOException {
        System.out.println("estamos en patientLoggedInMenu");
        System.out.println(patient.getPatientId());
        connection.getSendViaNetwork().sendLoggedPatient(patient);
        int option;
        do {
            switch (option = connection.getReceiveViaNetwork().receiveInt()){
                case 1: seePatientInfo(patient); break;
                case 2: createReport(patient); break;
                case 3: exitMenu(); break;
            }
        } while (option != 3);
    }

    private void seePatientInfo(Patient patient) throws IOException {
        User user = manager.getUserJDBC().getUserById(patient.getUserId());
        connection.getSendViaNetwork().sendUser(user);
        if(patient.getDoctorId() != null){
            Doctor doctor = manager.getDoctorJDBC().getDoctorByDoctorId(patient.getDoctorId());
            connection.getSendViaNetwork().sendString(doctor.getFullName()); //SOLO SE MANDA NOMBRE DEL DOCTOR
        } else{
            connection.getSendViaNetwork().sendString("NO DOCTOR");
        }
        patientLoggedInMenu(patient);
    }

    private void createReport(Patient patient) throws IOException {
        Report report = connection.getReceiveViaNetwork().receiveReport();
        manager.getReportJDBC().addReport(report);
        patient.addReport(report);
        connection.getSendViaNetwork().sendString("REPORT ADDED");
    }

    private void exitMenu() {
        // Cerrar la conexión de este cliente
        connection.releaseResources();

        // IMPORTANTE: parar el menú de este cliente
        throw new RuntimeException("Client exited normally");
    }


    private void registerDoctor() {
        do{
            String email = connection.getReceiveViaNetwork().receiveString(); //
            System.out.println(email);// email

            if (manager.getUserJDBC().getUserByEmail(email) != null) {

                System.out.println("User already exists");
                Integer userID = manager.getUserJDBC().getUserIdByEmail(email);

                if(manager.getDoctorJDBC().getDoctorIdByUserId(userID) != null){
                    System.out.println("Doctor already exists");
                    connection.getSendViaNetwork().sendString("EMAIL ERROR");
                    return;
                } else{
                    connection.getSendViaNetwork().sendString("EMAIL OK");
                }
            } else{
                System.out.println("Email: " + email + " is OK");
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
            System.out.println("Doctor registered");
            return;
        } while(true);

    }

    private void loginDoctor() {
        do{ String doctorEmail = connection.getReceiveViaNetwork().receiveString();

            if (manager.getUserJDBC().getUserByEmail(doctorEmail) != null) { //Si existe el usuario

                Integer userId = manager.getUserJDBC().getUserIdByEmail(doctorEmail);

                if(manager.getDoctorJDBC().getDoctorIdByUserId(userId) != null){ //Si existe el doctor

                    Integer doctorId = manager.getDoctorJDBC().getDoctorIdByUserId(userId);
                    System.out.println("EMAIL OK");
                    connection.getSendViaNetwork().sendString("EMAIL OK");

                    String password = connection.getReceiveViaNetwork().receiveString();
                    if(manager.getDoctorJDBC().getPasswordByDoctorId(doctorId).equals(password)){
                        System.out.println("PASSWORD OK");
                        connection.getSendViaNetwork().sendString("PASSWORD OK");
                        Doctor doctor = manager.getDoctorJDBC().getDoctorByDoctorId(doctorId);
                        doctor.setPatients(manager.getPatientJDBC().getPatientsByDoctorId(doctor.getDoctorId()));
                        System.out.println(doctor.toString());
                        doctorLoggedInMenu(doctor);

                    } else{
                        System.out.println("PASSWORD ERROR");
                        connection.getSendViaNetwork().sendString("PASSWORD ERROR");
                    }
                } else{
                    System.out.println("NO DOCTOR FOUND");
                    connection.getSendViaNetwork().sendString("NO DOCTOR FOUND");
                }
            } else{
                System.out.println("NO USER FOUND");
                connection.getSendViaNetwork().sendString("NO USER FOUND");
            }

        } while (true);
    }
    private void doctorLoggedInMenu(Doctor doctor) {
        connection.getSendViaNetwork().sendLoggedDoctor(doctor);
        int option;
        do{
            switch(option = connection.getReceiveViaNetwork().receiveInt()){
                case 0: doctorPreLoggedMenu(); break; //Doctor wants to logOut
                case 1: doctorPatientMenu(); break; //Doctor wants to see patients
            }
        } while(option != 0);
    }
    private void doctorPatientMenu() {
        Integer patientId = connection.getReceiveViaNetwork().receiveInt();
        List<Report> reports = manager.getReportJDBC().getReportsByPatientId(patientId);
        connection.getSendViaNetwork().sendReports(reports);
        int option;
        do{
            switch (option = connection.getReceiveViaNetwork().receiveInt()){
                case 0: return; //Doctor wants to go back (select another patient)
                case 1:  //Doctor wants to add observation of a patient
                    Integer reportId = connection.getReceiveViaNetwork().receiveInt();
                    String doctorObservation = connection.getReceiveViaNetwork().receiveString();
                    manager.getReportJDBC().updateDoctorObservation(reportId, doctorObservation);
                    connection.getSendViaNetwork().sendString("ADDED OBSERVATION: " + doctorObservation);
                    break;
            }
        } while (option != 0);
    }

    private void logInPatient() throws IOException {
        String patientEmail = connection.getReceiveViaNetwork().receiveString();

        if (manager.getUserJDBC().getUserByEmail(patientEmail) != null) { //Si existe el usuario

            Integer userId = manager.getUserJDBC().getUserIdByEmail(patientEmail);

            if(manager.getPatientJDBC().getPatientIdByUserId(userId) != null){ //Si existe el paciente

                Integer patientId = manager.getPatientJDBC().getPatientIdByUserId(userId);
                connection.getSendViaNetwork().sendString("EMAIL OK");

                String password = connection.getReceiveViaNetwork().receiveString();
                if(manager.getPatientJDBC().getPasswordByPatientId(patientId).equals(password)){
                    connection.getSendViaNetwork().sendString("PASSWORD OK");
                    Patient patient = manager.getPatientJDBC().getPatientByPatientId(patientId);
                    System.out.println(patient.toString());
                    patientLoggedInMenu(patient);

                } else{
                    connection.getSendViaNetwork().sendString("PASSWORD ERROR");
                }
            } else{
                connection.getSendViaNetwork().sendString("NO PATIENT FOUND");
            }
        } else{
            connection.getSendViaNetwork().sendString("NO USER FOUND");
        }

    }
}
