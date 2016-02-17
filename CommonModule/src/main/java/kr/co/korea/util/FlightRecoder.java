package kr.co.korea.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by kjs on 2016-02-17.
 */
public class FlightRecoder {
    public static final String COMMA = ",";
//    private final String path = "E://kr/ac/korea/drone-simulation/flight-info";
    private final String path = "E:\\kr.ac.korea";
    private String fileName;
    private File file;
    private FileWriter fileWriter;
    private BufferedWriter bufferedWriter;

    public FlightRecoder(String fileName, boolean append) throws IOException {

        this.fileName = fileName;

        File dir = new File(path);

        if(!dir.exists()){
            dir.mkdirs();
        }

        String writePath = path + File.separator + fileName;
        this.file = new File(writePath);
        fileWriter = new FileWriter(file, append);
        bufferedWriter = new BufferedWriter(fileWriter);
    }

    public void writeToFile(String flightInfo){
        try {
            this.bufferedWriter.write(flightInfo);
            this.bufferedWriter.newLine();
            this.bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close(){
        try {
            this.bufferedWriter.close();
            this.fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
