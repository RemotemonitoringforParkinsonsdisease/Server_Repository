package ui;

import manageData.ReceiveDataViaNetwork;
import manageData.SendDataViaNetwork;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Manages the low-level network connection with a client, wrapping the socket,
 * input and output streams, and helper classes used to send and receive data
 * over the network.
 */
public class Connection {

    private final Socket socket;
    private final DataInputStream in;
    private final DataOutputStream out;

    private final SendDataViaNetwork send;
    private final ReceiveDataViaNetwork receive;

    /**
     * Creates a new connection wrapper for the given socket, initializing the
     * input and output data streams and the helper classes for sending and
     * receiving data over the network.
     *
     * @param socket the connected socket associated with the client
     * @throws RuntimeException if an I/O error occurs while creating the streams
     */
    public Connection(Socket socket) {
        try {
            this.socket = socket;

            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());

            this.send = new SendDataViaNetwork(out);
            this.receive = new ReceiveDataViaNetwork(in);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the helper used to send data to the client over the network.
     *
     * @return the send helper for network communication
     */
    public SendDataViaNetwork getSendViaNetwork() {
        return send;
    }

    /**
     * Returns the helper used to receive data from the client over the network.
     *
     * @return the receive helper for network communication
     */
    public ReceiveDataViaNetwork getReceiveViaNetwork() {
        return receive;
    }

    /**
     * Returns the underlying socket associated with this connection.
     *
     * @return the socket used for the client connection
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * Releases all network-related resources by closing the input and output
     * streams and the underlying socket. Any I/O exception thrown while closing
     * the resources is ignored.
     */
    public void releaseResources() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException ignored) {}
    }
}
