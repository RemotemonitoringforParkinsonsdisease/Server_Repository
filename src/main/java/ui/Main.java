package ui;

import POJOs.Admin;
import jdbcs.ManagerJDBC;
import utilities.Utilities;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static final int PORT = 9000;
    private static boolean running = false;
    private static ServerSocket serverSocket;
    private static ManagerJDBC jdbcManager;

    public static void main(String[] args) {
        jdbcManager = new ManagerJDBC();



        /*jdbcManager.getUserJDBC().addUser("mou@gmail.com");
        Integer userId = jdbcManager.getUserJDBC().getUserIdByEmail("mou@gmail.com");
        Admin admin = new Admin(userId,"mou1");
        jdbcManager.getAdminJDBC().addAdmin(admin);
        System.out.printf("admin added");*/
        //CADA VEZ QUE LA BASE DE DATOS SE BORRE HAY Q DESCOMENTAR ESTO Y VOLVERLO A CREAR PARA TENER UN ADMIN

        adminLoginMenu();
            do{
                System.out.println("SERVER MENU (PORT: " + PORT + "):");
                System.out.println("1) Start Server");
                System.out.println("2) Stop Server");
                System.out.println("3) Exit");
                int option = Utilities.readInteger("Select an option: \n");
                switch (option){
                    case 1: startServer(jdbcManager); break;
                    case 2: stopServer(); break;
                    case 3: stopServer();
                        exitServer();
                        break;
                    default: System.out.println("Please introduce a valid option.");
                }
            } while(true);
    }
    private static void adminLoginMenu() {
        do {
            String email;
            boolean valid;
            do {
                email = Utilities.readString("Enter admin email: ");
                valid = Utilities.checkEmail(email);
            } while (!valid);
            if (jdbcManager.getUserJDBC().getUserIdByEmail(email) != null ) {
                Integer userId = jdbcManager.getUserJDBC().getUserIdByEmail(email);
                if(jdbcManager.getAdminJDBC().getAdminIdByUserId(userId) != null){
                    Integer adminId = jdbcManager.getAdminJDBC().getAdminIdByUserId(userId);
                    String password = Utilities.readString("Enter admin password: ");
                    if (jdbcManager.getAdminJDBC().getPasswordByAdminId(adminId).equals(password)) {
                        System.out.println("PASSWORD OK");
                        return;
                    }
                    else {
                        System.out.println("Wrong password");
                    }
                }else {
                    System.out.println("No admin found with that email.");
                }
            } else{
                System.out.println("No user found with that email.");
            }
        } while(true);
    }
    private static void clientHandler(Socket socket, ManagerJDBC jdbcManager) {
        System.out.println("Handling new client " + socket.getInetAddress().toString());
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
        System.exit(0);
    }
}

