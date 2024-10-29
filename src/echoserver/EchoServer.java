package echoserver;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {
    
    private static final int PORT = 6013;  // Required port number

    public static void main(String[] args) {
        // Create a ServerSocket to listen on the specified port
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("EchoServer is listening on port " + PORT);

            while (true) {
                // Wait for a client connection and accept it
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connected to client: " + clientSocket.getInetAddress());

                // Create a new thread to handle the client
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            // Handle any exceptions related to the server socket
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
        // Manage input and output streams for reading data from and writing data to the client
        try (InputStream input = clientSocket.getInputStream();
             OutputStream output = clientSocket.getOutputStream()) {

            int byteData;
            // Read bytes from the client and write each byte back to the client
            while ((byteData = input.read()) != -1) {
                output.write(byteData);
            }
            output.flush();
            System.out.println("Echoed data back to client: " + clientSocket.getInetAddress());

        } catch (IOException e) {
            System.err.println("Error handling client: " + e.getMessage());
        } finally {
            try {
                // Close the connection to the client after communication is complete
                clientSocket.close();
                System.out.println("Client disconnected: " + clientSocket.getInetAddress());
            } catch (IOException e) {
                System.err.println("Error closing client socket: " + e.getMessage());
            }
        }
    }
}
