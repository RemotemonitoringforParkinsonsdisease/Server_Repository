package ui;

import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {

        ServerSocket serverSocket = new ServerSocket(8000); // escuchando puerto 8000

        while(running) { //para poder tener varios clientes a la vez sin que se bloqueen entre ellos
            Socket socket = serverSocket.accept(); // acepta un cliente
            activeClients++;
            new Thread(() -> handleClient(socket, manager)).start(); // crea hilo para cliente
        }

    }
}
