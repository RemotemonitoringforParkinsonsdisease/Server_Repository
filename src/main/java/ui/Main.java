package ui;

import jdbcs.ManagerJDBC;
import POJOs.Patient;
import POJOs.Doctor;
import manageData.ReceiveDataViaNetwork;
import manageData.SendDataViaNetwork;
import ui.UI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {

    private static final int PORT = 9000;
    private static boolean running = true;

    public static void main(String[] args) {
        ManagerJDBC jdbcManager = new ManagerJDBC();
        try{
            ServerSocket serverSocket = new ServerSocket(PORT);
            Thread thread = logIn(jdbcManager, serverSocket);
        } catch (IOException e) {
            System.out.println(e); //TODO:Excepcion

        }




    }
    private static void logIn(ManagerJDBC manager, ServerSocket serverSocket) {
        //JDBCRole roleManager = new JDBCRole(manager);
        //JDBCUser userManager = new JDBCUser(manager, roleManager);
        //Role role = new Role("administrator");
        try {
            while (running) {
                System.out.println("\n\n      LOG IN\n");
                String email;
                do {
                    email = Utilities.readString("Email: ");
                } while (!Utilities.checkEmail(email));

                String psw = Utilities.readString("Enter your password: ");
                byte[] password = EncryptPassword.encryptPassword(psw);

                if (password != null) {
                    User u = userManager.checkPassword(email, new String(password));
                    if (u != null && u.getRole().getName().equals(role.getName())) {
                        menuAdmin(serverSocket);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Displays the server's admin menu, where the admin can shut down the server
     * or see the number of active clients connected.
     */
    private static void menuAdmin(ServerSocket serverSocket) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            while (running) {
                System.out.println("=== ADMINISTRATOR MENU ===");
                System.out.println("1. Turn off the server");
                System.out.println("2. View connected clients");
                System.out.print("Select and option: \n");

                String input = reader.readLine();
                int opcion;
                try {
                    opcion = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    System.out.println("Please, enter a valid number.");
                    continue;
                }

                if (opcion == 1) {
                    System.out.println("Closing server...");
                    while (activeClients > 0) {
                        System.out.println("Waiting for active clients desconnection: " + activeClients);
                        Thread.sleep(2000);
                    }
                    running = false;
                    releaseResources(serverSocket);
                } else if (opcion == 2) {
                    System.out.println("Active clients now: " + activeClients);
                } else {
                    System.out.println("Not valid option. Try again.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

