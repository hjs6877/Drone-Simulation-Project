package kr.co.korea.domain;

import kr.co.korea.error.ErrorType;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

/**
 * 1. TRIVIAL 장애 발생 시, TRIVIAL list에 추가하고 TRIVIAL list size가 2이면, MINOR에 1개추가 후, list 비우기
 * 2. MINOR 장애 발생 시, MINOR list에 추가하고, MINOR list size가 2이면, MAJOR list에 1개추가 후, list 비우기
 * 3. MAJOR 장애 발생 시, MAJOR list에 추가하고, MAJOR list size가 3이면, CRITICAL list에 1개 추가 후, list 비우기
 * 4. CRITICAL 장애 발생 시, CRITICAL list에 추가하고, CRITICAL list size가 2이면, 리더 교체
 * 5. BLOCK 장애 발생 시, BLOCK list에 추가하고, BLOCK list size가 1이면, 리더 교체
 */
public class FlightStatus implements Serializable {
    List<ErrorType> trivialList = new ArrayList<ErrorType>();
    List<ErrorType> minorList = new ArrayList<ErrorType>();;
    List<ErrorType> majorList = new ArrayList<ErrorType>();;
    List<ErrorType> criticalList = new ArrayList<ErrorType>();;
    List<ErrorType> blockList = new ArrayList<ErrorType>();;


    public List<ErrorType> getTrivialList() {
        return trivialList;
    }

    public List<ErrorType> getMinorList() {
        return minorList;
    }

    public List<ErrorType> getMajorList() {
        return majorList;
    }


    public List<ErrorType> getCriticalList() {
        return criticalList;
    }


    public List<ErrorType> getBlockList() {
        return blockList;
    }


    // TODO 메서드를 하나로 줄이기
    public void addTrivial(ErrorType errorType) {
        trivialList.add(errorType);
    }

    public void addMinor(ErrorType errorType) {
        minorList.add(errorType);
    }

    public void addMajor(ErrorType errorType) {
        majorList.add(errorType);
    }

    public void addCritical(ErrorType errorType) {
        criticalList.add(errorType);
    }

    public void addBlock(ErrorType errorType) {
        blockList.add(errorType);
    }


}
