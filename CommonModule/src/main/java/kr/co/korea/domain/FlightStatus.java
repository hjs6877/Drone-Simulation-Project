package kr.co.korea.domain;

import kr.co.korea.error.ErrorType;
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
    private List<ErrorType> trivialList = new ArrayList<ErrorType>();
    private List<ErrorType> minorList = new ArrayList<ErrorType>();
    private List<ErrorType> majorList = new ArrayList<ErrorType>();
    private List<ErrorType> criticalList = new ArrayList<ErrorType>();
    private List<ErrorType> blockList = new ArrayList<ErrorType>();

    private double totalErrorPoint = 0.0;

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

    public double getTotalErrorPoint() {
        return totalErrorPoint;
    }

    public void setTotalErrorPoint() {
        double trivialUnitPoint = 1.0;
        double minorUnitPoint = 2.0;
        double majorUnitPoint = 3.0;
        double criticalUnitPoint = 6.0;
        double blockUnitPoint = 9.0;

        double trivialPoint = trivialUnitPoint * trivialList.size();
        double minorPoint = minorUnitPoint * minorList.size();
        double majorPoint = majorUnitPoint * majorList.size();
        double criticalPoint = criticalUnitPoint * criticalList.size();
        double blockPoint = blockUnitPoint * blockList.size();

        totalErrorPoint = trivialPoint + minorPoint + majorPoint + criticalPoint + blockPoint;
    }

    public void addErrorEvent(ErrorType errorType){
//        if(errorType == ErrorType.TRIVIAL){
//            trivialList.add(errorType);
//        }else if(errorType == ErrorType.MINOR){
//            minorList.add(errorType);
//        }else if(errorType == ErrorType.MAJOR){
//            majorList.add(errorType);
//        }else if(errorType == ErrorType.CRITICAL){
//            criticalList.add(errorType);
//        }else if(errorType == ErrorType.BLOCK){
//            blockList.add(errorType);
//        }
    }

    /**
     * NORMAL을 제외하고, TRIVIAL부터 순차적으로 장애 횟수를 판단해서 상위 장애의 횟수를 업데이트 해야 됨.
     */
    public void updateErrorEvent(){
        /**
         * ErrorType의 순서가 바뀌지 않는다는 가정하에 loop로 처리.
         * TODO 순서가 바뀐다면 TRIVIAL부터 순차적으로 장애 횟수를 판단해야 됨.
         */
        ErrorType[] ErrorTypes = ErrorType.values();
        for(ErrorType ErrorType : ErrorTypes){
            if(ErrorType == ErrorType.NORMAL) continue;

            this.updateUpperErrorType(ErrorType);
        }

    }

    public boolean hasThreshholdErrorEvent(){
        return (this.getCriticalList().size() >= 2 || this.getBlockList().size() >= 1);
    }

    private void updateUpperErrorType(ErrorType ErrorType) {
//        if(ErrorType == ErrorType.TRIVIAL){
//            if(trivialList.size() == 2){
//                minorList.add(ErrorType.TRIVIAL);
//                clearErrorEventList(ErrorType.TRIVIAL);
//            }
//        }else if(ErrorType == ErrorType.MINOR){
//            if(minorList.size() == 2){
//                majorList.add(ErrorType.MAJOR);
//                clearErrorEventList(ErrorType.MINOR);
//            }
//        }else if(ErrorType == ErrorType.MAJOR){
//            if(majorList.size() == 3){
//                criticalList.add(ErrorType.CRITICAL);
//                clearErrorEventList(ErrorType.MAJOR);
//            }
//        }else if(ErrorType == ErrorType.CRITICAL){
//            /**
//             * CRITICAL의 경우 리더 교체를 판단할 때 체크만 하면 되므로 별도로 BLOCK으로 업데이트 할 필요 없음.
//             */
//        }else if(ErrorType == ErrorType.BLOCK){
//            /**
//             * BLOCK의 경우 리더 교체를 판단할 때 체크만 하면 됨.
//             */
//        }
    }

    public void clearErrorEventList(ErrorType ErrorType){
//        if(ErrorType == ErrorType.TRIVIAL){
//            trivialList.clear();
//        }else if(ErrorType == ErrorType.MINOR){
//            minorList.clear();
//        }else if(ErrorType == ErrorType.MAJOR){
//            majorList.clear();
//        }else if(ErrorType == ErrorType.CRITICAL){
//            criticalList.clear();
//        }else if(ErrorType == ErrorType.BLOCK){
//            blockList.clear();
//        }
    }

}
