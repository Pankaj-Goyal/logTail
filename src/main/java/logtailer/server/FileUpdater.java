package logtailer.server;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class FileUpdater {

    public String periodicFileWriter(String filePath) throws Exception {
        if(filePath == null || filePath.isEmpty())
            throw new Exception("No file path found to periodically update log file");

        while(!LogTailer.stopTailing) {
            Thread.sleep(2000);
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
                String content = "New line added in log file at : " + System.currentTimeMillis() + "\n";
                bw.write(content);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "Done";
    }

    public void writeToFileAsync(String filePath) {
        CompletableFuture.supplyAsync(() -> {
            try {
                return periodicFileWriter(filePath);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        });
    }
}
