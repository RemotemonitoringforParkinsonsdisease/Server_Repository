package ui;

import JDBCs.ManagerJDBC;
import POJOs.Patient;
import manageData.ReceiveDataViaNetwork;

import java.net.Socket;

public class UI {
    private Socket socket;
    private ManagerJDBC manager;

    public UI(Socket socket, ManagerJDBC manager) {
        this.socket = socket;
        this.manager = manager;
    }

    private static void patientRegister (ReceiveDataViaNetwork receiveDataViaNetwork , Patient patient,) {
        try {
            String message = receiveDataViaNetwork.receiveString();
            if (message.contains("OK")) {

            }
        }

    }
}
