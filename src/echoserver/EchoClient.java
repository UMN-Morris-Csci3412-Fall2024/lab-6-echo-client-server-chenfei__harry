package echoserver;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.Socket;

public class EchoClient {
    private static final String HOST = "localhost";
    private static final int PORT = 6013;

    public static void main(String[] args) {
        try (Socket socket = new Socket(HOST, PORT)) {
            InputStream userInput = System.in;
            OutputStream output = socket.getOutputStream();
            InputStream socketInput = socket.getInputStream();
            OutputStream consoleOutput = System.out;

            Thread senderThread = new Thread(() -> {
                try {
                    int byteData;
                    while ((byteData = userInput.read()) != -1) {
                        output.write(byteData);
                    }
                    output.flush();
                } catch (IOException e) {
                    System.err.println("Error sending data: " + e.getMessage());
                }
            });

            Thread receiverThread = new Thread(() -> {
                try {
                    int byteData;
                    while ((byteData = socketInput.read()) != -1) {
                        consoleOutput.write(byteData);
                    }
                    consoleOutput.flush();
                } catch (IOException e) {
                    System.err.println("Error receiving data: " + e.getMessage());
                }
            });

            senderThread.start();
            receiverThread.start();

            senderThread.join(); 
            receiverThread.join(); 

        } catch (IOException | InterruptedException e) {
            System.err.println("Client error: " + e.getMessage());
        }
    }
}
