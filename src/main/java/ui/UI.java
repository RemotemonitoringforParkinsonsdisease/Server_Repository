package ui;

import JDBCs.ManagerJDBC;
import POJOs.Patient;
import manageData.ReceiveDataViaNetwork;
import manageData.SendDataViaNetwork;
import POJOs.User;

import java.io.IOException;
import java.net.Socket;

public class UI {
    private Socket socket;
    private ManagerJDBC manager;
    User user = new User(null,null,null);


    public UI(Socket socket, ManagerJDBC manager) {
        this.socket = socket;
        this.manager = manager;
    }

    private static void patientRegister (ReceiveDataViaNetwork receiveDataViaNetwork) {
        try {
            String message = receiveDataViaNetwork.receiveString();
            if (message.contains("OK")) {
                Patient patient = receiveDataViaNetwork.receivePatient();
                //int patient_id = user.createId("p")
                //aquí iría el metodo para asignar el doctor de manera aleatoria


            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
//HAY QUE VER QUE SERIA NUESTRO INTERPRETATION MANAGER Y SYMPTOMS MANAGER
    public static void patientMenu(Patient patient, ReceiveDataViaNetwork recieveDataViaNetwork, SendDataViaNetwork sendDataViaNetwork, SymptomsManager symptomsManager, InterpretationManager interpretationManager, Socket socket) {
        int option = 0; //CHECKEAR
        boolean menu = true;
        //ArrayList<Integer> patientSymptomsID = new ArrayList<>();
        while (menu) {
            //option = recieveDataViaNetwork.receiveInt();
            switch (option) {
                case 1: {
                    //seeMyInformation(patient, recieveDataViaNetwork, sendDataViaNetwork, interpretationManager, symptomsManager, socket);                        break;
                }
                case 2: {
                    //seeMyReports(patient, recieveDataViaNetwork, sendDataViaNetwork, interpretationManager, symptomsManager, socket);
                    break;
                }
                case 3: {
                    //initiateReport(patient,recieveDataViaNetwork,sendDataViaNetwork,interpretationManager,symptomsManager,socket);
                }
                case 4: {
                    menu = false;
                    //logOut(parámetros para que el paciente se salga del servidor);
                    break;
                }
                default:
                    System.out.println("That number is not an option, try again");
                    break;
            }

        }
    }
}
