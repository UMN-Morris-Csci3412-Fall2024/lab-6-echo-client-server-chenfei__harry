package echoserver;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.Socket;

public class EchoClient {
    private static final String HOST = "localhost";
    private static final int PORT = 12345;  // Same port as server

    public static void main(String[] args) {
        try (Socket socket = new Socket(HOST, PORT)) {
            InputStream input = System.in;
            OutputStream output = socket.getOutputStream();
            InputStream socketInput = socket.getInputStream();
            OutputStream socketOutput = System.out;

            int byteData;
            // Send data to server
            while ((byteData = input.read()) != -1) {
                output.write(byteData);
            }
            output.flush();  // Ensure all data is sent before closing

            // Echo data received from server to standard output
            while ((byteData = socketInput.read()) != -1) {
                socketOutput.write(byteData);
            }
            socketOutput.flush();
        } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
        }
    }
}