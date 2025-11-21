package ui;

import manageData.ReceiveDataViaNetwork;
import manageData.SendDataViaNetwork;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Connection {
    private Socket socket;
    private SendDataViaNetwork sendDataViaNetwork;
    private ReceiveDataViaNetwork receiveDataViaNetwork;

    public Connection(Socket socket) {
        try {
            this.socket = socket; //TODO: Revisar
            this. sendDataViaNetwork = new SendDataViaNetwork(socket);
            this. receiveDataViaNetwork = new ReceiveDataViaNetwork(socket);
        } catch (Exception e) {
            System.out.println("Error establishing connection"); //TODO: Revisar excepciones
        }
    }

    public SendDataViaNetwork getSendViaNetwork() {
        return sendDataViaNetwork;
    }

    public ReceiveDataViaNetwork getReceiveViaNetwork() {
        return receiveDataViaNetwork;
    }

    void releaseResources() {
        sendDataViaNetwork.releaseResources();
        receiveDataViaNetwork.releaseResources();
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


}