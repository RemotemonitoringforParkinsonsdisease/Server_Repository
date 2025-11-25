package ui;

import POJOs.User;
import jdbcs.ManagerJDBC;
import POJOs.Patient;
import POJOs.Doctor;
import manageData.ReceiveDataViaNetwork;
import manageData.SendDataViaNetwork;
import ui.UI;
import utilities.Utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static final int PORT = 9000;
    private static boolean running = true;
    private static ServerSocket serverSocket;
    private static Thread serverThread;

    public static void main(String[] args) {
        ManagerJDBC jdbcManager = new ManagerJDBC();
        adminLoginMenu();
            do{
                System.out.println("SERVER MENU (PORT: " + PORT + "):");
                System.out.println("1) Start Server");
                System.out.println("2) Stop Server");
                System.out.println("3) Exit");
                int option = Utilities.readInteger("Select an option: ");
                switch (option){
                    case 1: startServer(jdbcManager); break;
                    case 2: stopServer(); break;
                    case 3: exitServer(); break;
                    default: System.out.println("Please introduce a valid option.");
                        continue;

                }
            } while(true);
    }
    private static void adminLoginMenu() {
        //TODO: Implement admin login menu
    }
    private static void clientHandler(Socket socket, ManagerJDBC jdbcManager) {
        try{
            UI ui = new UI(socket, jdbcManager);
            ui.run();
        } catch(Exception e){
            System.out.println("Error during client handling: " + e.getMessage());
        } finally{
            try{
                socket.close();
            } catch (IOException e) {
                System.out.println("Error closing socket: " + e.getMessage());
            }
        }
    }
    private static void startServer(ManagerJDBC jdbcManager) {
        if(running){
            System.out.println("Server is already running.");
            return;
        }
        running = true;
        System.out.println("Server started");
        Thread serverThread = new Thread(() -> {
            try {
                serverSocket = new ServerSocket(PORT);
                System.out.println("Server started on port " + PORT);

                while (running) {
                    try {
                        Socket socket = serverSocket.accept();
                        System.out.println("Client connected.");

                        new Thread(() -> {
                            clientHandler(socket, jdbcManager);
                        }).start();

                    } catch (IOException e) {
                        if (running) {
                            System.out.println("Error accepting client: " + e.getMessage());
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Unable to start server: " + e.getMessage());
            }
        });
        serverThread.start();
    }
    private static void stopServer() {
        if(!running){
            System.out.println("Server is already stopped.");
            return;
        }
        try{
            running = false;
            serverSocket.close();
            System.out.println("Server stopped");
        } catch (IOException e) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    private static void exitServer() {
        //TODO: Implement exit server
    }
}

