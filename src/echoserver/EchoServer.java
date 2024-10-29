package echoserver;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {
    
    private static final int PORT = 6013;  

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("EchoServer is listening on port " + PORT);

            while (true) {
                // Accept a client connection and start a new thread to handle it
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connected to client: " + clientSocket.getInetAddress());

                // Create a new thread to handle the client
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }
}

class ClientHandler implements Runnable {
    private final Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (InputStream input = clientSocket.getInputStream();
             OutputStream output = clientSocket.getOutputStream()) {

            int byteData;
            while ((byteData = input.read()) != -1) {
                output.write(byteData);
            }
            output.flush();
            System.out.println("Echoed data back to client: " + clientSocket.getInetAddress());

        } catch (IOException e) {
            System.err.println("Error handling client: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
                System.out.println("Client disconnected: " + clientSocket.getInetAddress());
            } catch (IOException e) {
                System.err.println("Error closing client socket: " + e.getMessage());
            }
        }
    }
}
