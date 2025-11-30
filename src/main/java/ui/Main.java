package ui;

import POJOs.Admin;
import jdbcs.ManagerJDBC;
import utilities.Utilities;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main entry point for the telemedicine server application. This class manages
 * the administrator login, shows a simple console menu to start and stop the
 * server, and listens for incoming client connections, delegating each client
 * to a UI instance running in its own thread.
 */
public class Main {

    private static final int PORT = 9000;
    private static boolean running = false;
    private static ServerSocket serverSocket;
    private static ManagerJDBC jdbcManager;
    private static int clientsCount = 0;

    /**
     * Entry point of the server application. It initializes the database manager,
     * forces an administrator login through the console, and then repeatedly
     * shows a menu that allows the admin to start the server, stop it or exit
     * the application completely.
     *
     * @param args program arguments (not used)
     */
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
                case 2:
                    confirmExit();
                case 3:
                    if(confirmExit().equals("1")){
                        exitServer();
                    }

                default: System.out.println("Please introduce a valid option.");
            }
        } while(true);
    }

    /**
     * Manages the administrator login process before the server menu is shown.
     * This method runs in a loop asking for an email and password, checks that
     * the email belongs to an existing user with admin privileges and validates
     * the password in the database, only returning when valid admin credentials
     * have been provided.
     */
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

    /**
     * Handles a single client connection once the server has accepted it.
     * It creates a UI instance for the connected socket, runs the interaction
     * with the client, and finally ensures that the socket is closed even if
     * an error occurs during client handling.
     *
     * @param socket      the socket connected to the client
     * @param jdbcManager the database manager shared with the server
     */
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
            decrementClients();
        }
    }

    /**
     * Starts the server if it is not already running. This method launches a
     * background thread that opens a server socket on the configured port and
     * enters a loop accepting client connections while the running flag is true.
     * Each accepted client is handled in a separate thread using the clientHandler
     * method. If the server is already running, it simply prints a message and
     * returns.
     *
     * @param jdbcManager the database manager used by the server and its clients
     */
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
                        incrementClients();
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

    /**
     * Stops the server if it is currently running. It sets the running flag to
     * false, closes the server socket so that the accept loop can finish, and
     * prints a message indicating that the server has been stopped. If the
     * server is already stopped, it prints a message and returns.
     */
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

    /**
     * Exits the server application immediately, terminating the Java virtual
     * machine with a normal status code after any required cleanup has been
     * performed by the caller.
     */
    private static void exitServer() {
        System.exit(0);
    }

    /**
     * Increments the count of connected clients
     */
    public static synchronized void incrementClients() {
        clientsCount++;
    }

    /**
     * Decrements the count of connected clients
     */
    public static synchronized void decrementClients() {
        clientsCount--;
    }

    /**
     * Returns the count of connected clients.
     *
     * @return the count of connected clients
     */
    public static synchronized int getConnectedClients() {
        return clientsCount;
    }

    /**
     * Confirms with the administrator whether to stop the server, showing the
     * number of currently connected clients.
     *
     * @return "0" to return to the menu, "1" to stop the server
     */
    public static synchronized String confirmExit() {
        System.out.println("There are " + getConnectedClients() + " connected clients. ");
        do{
            String confirm = Utilities.readString("Are you sure you want to stop the server? (0: Return) (1: Close Server): ");
            switch (confirm){
                case "0": return confirm;
                case "1": stopServer(); return confirm;
                default: System.out.println("Please introduce a valid option."); break;
            }
        } while (true);
    }
}
