package logtailer.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class LogTailClient {

    public static void main(String args[]) throws Exception {
        Socket s = null;
        BufferedReader br = null;

        try {
            s = new Socket("localhost", 80);
            br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }

        } finally {
            if(br != null)
                br.close();
            if(s != null)
                s.close();
        }
    }
}
