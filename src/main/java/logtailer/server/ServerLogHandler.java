package logtailer.server;

import java.net.ServerSocket;
import java.net.Socket;

public class ServerLogHandler {

    public static void main(String args[]) throws Exception {
        ServerSocket ss = null;
        Socket clientSocket = null;
        try {
            ss = new ServerSocket(80);
            // connect it to client socket
            clientSocket = ss.accept();
            Socket clientSocketF = clientSocket;
            System.out.println("Connection established");

            String filePath = args[0];
            if(filePath == null || filePath.isEmpty())
                throw  new Exception("No file Path found to locate log file");

            LogTailer logTailer = new LogTailer(filePath);

            boolean shouldAutoUpdateFile = Boolean.parseBoolean(args[1]);

            if(shouldAutoUpdateFile) {
                FileUpdater updater = new FileUpdater();
                updater.writeToFileAsync(filePath);
            }

            logTailer.streamDataToClient(clientSocketF);

        } finally {
            if(ss != null)
                ss.close();
            
            if(clientSocket != null)
                clientSocket.close();
        }
    }
}