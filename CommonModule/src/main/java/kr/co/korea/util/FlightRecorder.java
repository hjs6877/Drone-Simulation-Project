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
    public static final String path = "F:\\kr.ac.korea";
//    public static final String path = "D:\\kr.ac.korea";
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
        String flightInfoHeader = "비행시작일자"     + FlightRecorder.COMMA +
                "Drone 이름"                       + FlightRecorder.COMMA +
                "초기 리더/팔로워 구분"         + FlightRecorder.COMMA +
                "현재시점별 리더/팔로워 구분"         + FlightRecorder.COMMA +
                "비행속도"                           + FlightRecorder.COMMA +
                "비행 시점(초)"                       + FlightRecorder.COMMA +
                "시점별 비행 좌표(경도)"              + FlightRecorder.COMMA +
                "시점별 비행 좌표(위도)"               + FlightRecorder.COMMA +
                "장애 발생유무"                       + FlightRecorder.COMMA +
                "장애 타입"                          + FlightRecorder.COMMA +
                "장애 점수"                          + FlightRecorder.COMMA +
                "장애 가중치 점수"                          + FlightRecorder.COMMA +
                "비행 총거리"                         + FlightRecorder.COMMA +
                "잔여 거리";

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
        String droneSettingInfoHeader = "비행시작일자"     + FlightRecorder.COMMA +
                "드론 비행대수"     + FlightRecorder.COMMA +
                "Drone 이름"                       + FlightRecorder.COMMA +
                "포메이션 타입"         + FlightRecorder.COMMA +
                "출발지"         + FlightRecorder.COMMA +
                "목적지"                           + FlightRecorder.COMMA +
                "출발지 좌표(경도)"                       + FlightRecorder.COMMA +
                "출발지 좌표(위도)"              + FlightRecorder.COMMA +
                "목적지 좌표(경도)"               + FlightRecorder.COMMA +
                "목적지 좌표(위도)"                       + FlightRecorder.COMMA +
                "비행 거리"                          + FlightRecorder.COMMA +
                "비행 속도"                         + FlightRecorder.COMMA +
                "방위각"                         + FlightRecorder.COMMA +
                "비행시간";

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
