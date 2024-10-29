package echoserver;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.Socket;

public class EchoClient {
    public static final int PORT = 6013;  // Required port number
    public static void main(String[] args) throws IOException {
        String host;
        // Default to "localhost" if no host is provided, otherwise use the first argument as the host
        if (args.length == 0) {
            host = "127.0.0.1";
        } else {
            host = args[0];
        }
        try (Socket clientSocket = new Socket(host, PORT)) {
            InputStream serverInput = clientSocket.getInputStream();
            OutputStream serverOutput = clientSocket.getOutputStream();

            int byteData;
            while ((byteData = System.in.read()) != -1) {
                serverOutput.write(byteData);
                serverOutput.flush();
            }

            clientSocket.shutdownOutput(); // Indicate that no more data will be sent to the server

            // Receive echoed data from the server and print it to standard output
            while ((byteData = serverInput.read()) != -1) {
                System.out.write(byteData);
            }
            System.out.flush();
        } catch (IOException ex) {
            System.err.println("Connection error: " + ex.getMessage());
        }
    }
}
