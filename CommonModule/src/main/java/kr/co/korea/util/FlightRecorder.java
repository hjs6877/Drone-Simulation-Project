package kr.co.korea.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by kjs on 2016-02-17.
 */
public class FlightRecorder {
    public static final String COMMA = ",";
    public static final String DASH = "-";
//    public static final String path = "F:\\kr.ac.korea";
    public static final String path = "D:\\kr.ac.korea";
    private String fileName;
    private File file;
    private FileWriter fileWriter;
    private BufferedWriter bufferedWriter;
    private boolean existTodaysFile = false;

    public FlightRecorder(String fileName, boolean append) throws IOException {

        this.fileName = fileName;

        File dir = new File(path);


        if(!dir.exists()){
            dir.mkdirs();
        }

        String writePath = path + File.separator + fileName;

        this.file = new File(writePath);

        /**
         * 오늘 일자의 파일이 존재하는지 확인.
         * - 파일이 존재하면 파일을 쓸때 헤더를 쓰지 않고,
         * - 파일이 존재하지 않으면 파일을 쓸 때 헤더를 쓴다.
         */
        if(file.exists()){
            existTodaysFile = true;
        }



        fileWriter = new FileWriter(file, append);
        bufferedWriter = new BufferedWriter(fileWriter);
    }

    public void writeToFile(String flightInfo, boolean newLine){
        try {
            this.bufferedWriter.write(flightInfo);
            if(newLine){
                this.bufferedWriter.newLine();
            }
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

    public void writeNewLine(){
        try {
            this.bufferedWriter.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isExistTodaysFile() {
        return existTodaysFile;
    }

    public void writeFlyingInfoFileHeader() {
        String flightInfoHeader = "startDate"     + FlightRecorder.COMMA +
                "droneName"                       + FlightRecorder.COMMA +
                "leaderOrFollower"         + FlightRecorder.COMMA +
                "leaderOrFollowerAtSeconds"         + FlightRecorder.COMMA +
                "speed"                           + FlightRecorder.COMMA +
                "atSeconds"                       + FlightRecorder.COMMA +
                "longitude"              + FlightRecorder.COMMA +
                "latitude"               + FlightRecorder.COMMA +
                "isHappenedErrorEvent"                       + FlightRecorder.COMMA +
                "errorEventType"                          + FlightRecorder.COMMA +
                "errorPoint"                          + FlightRecorder.COMMA +
                "errorWeight"                          + FlightRecorder.COMMA +
                "totalDistance"                         + FlightRecorder.COMMA +
                "isArrivedDestination"                         + FlightRecorder.COMMA +
                "remainDistance";

        /**
         * 오늘 일자의 파일이 존재하는지 확인.
         * - 파일이 존재하면 파일을 쓸때 헤더를 쓰지 않고,
         * - 파일이 존재하지 않으면 파일을 쓸 때 헤더를 쓴다.
         */
        if(!this.isExistTodaysFile()){
            this.writeToFile(flightInfoHeader, true);
        }

    }

    public void writeDroneSettingInfoFileHeader() {
        String droneSettingInfoHeader = "startDate"     + FlightRecorder.COMMA +
                "aNumberOfDrone"     + FlightRecorder.COMMA +
                "droneName"                       + FlightRecorder.COMMA +
                "formationType"         + FlightRecorder.COMMA +
                "departure"         + FlightRecorder.COMMA +
                "destination"                           + FlightRecorder.COMMA +
                "departure longitude"                       + FlightRecorder.COMMA +
                "departure latitude"              + FlightRecorder.COMMA +
                "destination longitude"               + FlightRecorder.COMMA +
                "destination latitude"                       + FlightRecorder.COMMA +
                "distance"                          + FlightRecorder.COMMA +
                "speed"                         + FlightRecorder.COMMA +
                "angle"                         + FlightRecorder.COMMA +
                "flightTime";

        /**
         * 오늘 일자의 파일이 존재하는지 확인.
         * - 파일이 존재하면 파일을 쓸때 헤더를 쓰지 않고,
         * - 파일이 존재하지 않으면 파일을 쓸 때 헤더를 쓴다.
         */
        if(!this.isExistTodaysFile()){
            this.writeToFile(droneSettingInfoHeader, true);
        }

    }
}
