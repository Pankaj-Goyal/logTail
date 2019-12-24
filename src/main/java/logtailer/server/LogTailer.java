package logtailer.server;

import java.io.File;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class LogTailer {

    private final String filePath;
    private File fileToTail;
    private final long tailInterval;
    public static boolean stopTailing;
    private long lastReadPos;

    public LogTailer(String filePath) {
        this.filePath = filePath;
        this.fileToTail = new File(filePath);
        this.tailInterval = 1000l;
        this.stopTailing = false;
        this.lastReadPos = 0;
    }

    public String streamDataToClient(Socket socket) throws Exception {
        if(fileToTail == null || !fileToTail.exists())
            throw new IllegalArgumentException("File not found for filepath: " + filePath);

        System.out.println("About to send");

        PrintStream clientOutputStream = new PrintStream(socket.getOutputStream());;
        try {
            lastReadPos = fileToTail.length();
            while (!stopTailing) {
                Thread.sleep(tailInterval);

                List<String> tailedOutput = getTailedData();
                if(tailedOutput.size() > 0)
                    tailedOutput.forEach(clientOutputStream::println);
            }
        } catch (Exception e) {
            e.printStackTrace();
            stopTailing();
            throw e;
        } finally {
            clientOutputStream.close();
        }

        return "Done";
    }

    public List<String> getTailedData() throws Exception {
        List<String> tailedOutput = new ArrayList<String>();

        long fileLength = fileToTail.length();

        if(lastReadPos > fileLength) {
            lastReadPos = fileLength;
            return new ArrayList<>();
        }

        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(fileToTail, "r");
            randomAccessFile.seek(lastReadPos);
            String line;
            while ((line = randomAccessFile.readLine()) != null) {
//                System.out.println(line);
                tailedOutput.add(line);
            }

            lastReadPos = randomAccessFile.getFilePointer();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception while reading file");
            throw e;
        } finally {
            if(randomAccessFile != null)
            randomAccessFile.close();
        }
        if(tailedOutput.size() > 1 && tailedOutput.get(0).equals(""))
            tailedOutput.remove(0); //removing extra line because of enter- while manually entry to log file

        return tailedOutput;
    }

    public void stopTailing() {
        stopTailing = true;
    }
}
