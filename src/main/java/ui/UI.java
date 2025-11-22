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
                patientPreLoggedMenu();
            } else if(message == 2){
                connection.getSendViaNetwork().sendString("DOCTOR");
                doctorPreLoggedMenu();
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
        //TODO DOOOOOOOOOO
    }

    private void patientPreLoggedMenu() {
       int option = connection.getReceiveViaNetwork().receiveInt();
       switch (option){
           case 1: registerPatient(); break;
           case 2: logInPatient(); break;
           case 3: exitMenu(); break;
       }
    }
    private void doctorPreLoggedMenu() {
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
            //TODO userId = cogerElIDDeUsuario existente

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
        int doctorId = manager.getDoctorJDBC().getRandomDoctorId(); //TODO
        Patient patient = connection.getReceiveViaNetwork().receiveRegisteredPatient();
        patient.setUserId(userId);
        //TODO:Insertamos patient en DB
        patientLoggedInMenu(patient);


    }
    public void patientLoggedInMenu(Patient patient) {
        connection.getSendViaNetwork().sendLoggedPatient(patient);
        int option = connection.getReceiveViaNetwork().receiveInt();
        switch (option){
            case 1: seePatientInfo(patient); break;
            case 2: createReport(patient); break;
            case 3: exitMenu(); break;
        }
    }
    private void seePatientInfo(Patient patient) {
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
    private void createReport(Patient patient) {
        Report report = connection.getReceiveViaNetwork().receiveReport();
        //TODO: Guardad en DB
        patient.addReport(report);
        //TODO: Actualizar patient en DB
        connection.getSendViaNetwork().sendString("REPORT ADDED");
    }
    private void exitMenu() {
        //TODO
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

    private void loginDoctor() {
        String doctorEmail = connection.getReceiveViaNetwork().receiveString();

        if (manager.getUserJDBC().getUserByEmail(doctorEmail) != null) { //Si existe el usuario

            Integer userId = manager.getUserJDBC().getUserIdByEmail(doctorEmail);

            if(manager.getDoctorJDBC().getDoctorIdByUserId() != null){ //Si existe el doctor

                Integer doctorId = manager.getDoctorJDBC().getDoctorIdByUserId(userId);
                connection.getSendViaNetwork().sendString("EMAIL OK");

                String password = connection.getReceiveViaNetwork().receiveString();
                if(manager.getDoctorJDBC().getPasswordByDoctorId(doctorId).equals(password)){
                    connection.getSendViaNetwork().sendString("PASSWORD OK");

                    //TODO: GetDoctor From User //userId, doctorId, fullname, password, dob, patients
                    Doctor doctor = manager.getDoctorJDBC().getDoctorByDoctorId(doctorId);
                    System.out.println(doctor.toString()); //TODO
                    doctorLoggedInMenu(doctor);

                } else{
                    connection.getSendViaNetwork().sendString("PASSWORD ERROR");
                    return null;
                }
            } else{
                connection.getSendViaNetwork().sendString("NO DOCTOR FOUND");
                return null;
            }
        } else{
            connection.getSendViaNetwork().sendString("NO USER FOUND");
            return null;
        }

    }

    private void doctorLoggedInMenu(Doctor doctor) {
        connection.getSendViaNetwork().sendLoggedDoctor(doctor);
        int option = connection.getReceiveViaNetwork().receiveInt();
    }
    private void logInPatient(){
        String patientEmail = connection.getReceiveViaNetwork().receiveString();

        if (manager.getUserJDBC().getUserByEmail(patientEmail) != null) { //Si existe el usuario

            Integer userId = manager.getUserJDBC().getUserIdByEmail(patientEmail);

            if(manager.getPatientJDBC().getPatientIdByUserId() != null){ //Si existe el paciente

                Integer patirntId = manager.getDoctorJDBC().getPatientIdByUserId(userId);
                connection.getSendViaNetwork().sendString("EMAIL OK");

                String password = connection.getReceiveViaNetwork().receiveString();
                if(manager.getPatientDoctorJDBC().getPasswordByPatientId(patientId).equals(password)){
                    connection.getSendViaNetwork().sendString("PASSWORD OK");

                    //TODO: Get Patient From User //userId, doctorId, fullname, password, dob, patients
                    Patient patient = manager.getPatientJDBC().getPatientByPatientId(patientId);
                    System.out.println(patient.toString()); //TODO
                    patientLoggedInMenu(patient);

                } else{
                    connection.getSendViaNetwork().sendString("PASSWORD ERROR");
                    return null;
                }
            } else{
                connection.getSendViaNetwork().sendString("NO PATIENT FOUND");
                return null;
            }
        } else{
            connection.getSendViaNetwork().sendString("NO USER FOUND");
            return null;
        }

    }
}
