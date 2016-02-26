package kr.co.korea.domain;

import kr.co.korea.error.ErrorType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 1. errorEventList에 발생한 장애를 추가한다.
 * 2. errorEventPointList에 발생한 장애의 점수를 추가한다.
 */
public class FlightStatus implements Serializable {
    private List<ErrorType> errorEventList = new ArrayList<ErrorType>();
    private List<Double> errorEventPointList = new ArrayList<Double>();
    private Map<ErrorType, Integer> happenedErrorEventCountMap = new HashMap<ErrorType, Integer>();
    private double totalErrorPoint = 0.0;

    public double getTotalErrorPoint() {
        return totalErrorPoint;
    }

    /**
     * 1. 해당 장애 이벤트가 맵에 없으면 즉, 처음 발생한 장애 이벤트라면,
     *      - 해당 장애 이벤트의 출현 횟수를 1로 저장한다.
     *      - 해당 장애 이벤트의 기본 point를 totalErrorPoint에 더해서 누적시킨다.
     * 2. 해당 장애 이벤트가 맵에 있으면 즉, 이전에 발생한적이 있는 장애 이벤트라면,
     *      - 해당 장애 이벤트의 출현 횟수를 1증가 시켜서 저장한다.
     *      - 해당 장애 이벤트의 가중치 point를 계산한다.
     *          가중치 point = (현재시점까지의 해당 장애 이벤트 발생 횟수 / 해당 시점까지 발생한 전체 장애 이벤트의 개수) * 해당 장애 이벤트의 가본 point
     *      - 해당 장애 이벤트의 기본 point에 가중치 point를 더한다.
     *      - totalErrorPoint 에 (기본 point + 가중치 point)를 더한다.
     * @param errorType
     */
    public void setTotalErrorPoint(ErrorType errorType) {
        double defaultPoint = errorType.getPoint();
        int errorEventCount = happenedErrorEventCountMap.get(errorType);

        if(errorEventCount == 1){
            totalErrorPoint = totalErrorPoint + defaultPoint;
        } else {
            happenedErrorEventCountMap.put(errorType, ++errorEventCount);

            double weightPoint = this.getWeightPoint(errorType);
            double finalPoint = defaultPoint + weightPoint;

            totalErrorPoint = totalErrorPoint + finalPoint;

        }
    }

    public double getWeightPoint(ErrorType errorType){
        double defaultPoint = errorType.getPoint();
        int happenedCount = happenedErrorEventCountMap.get(errorType);

        double weightPoint = (happenedCount / errorEventList.size()) * defaultPoint;

        return weightPoint;
    }

    public void addErrorEvent(ErrorType errorType){
        errorEventList.add(errorType);
        errorEventPointList.add(errorType.getPoint());

        if(happenedErrorEventCountMap.get(errorType) == null){
            int initCount = 1;
            happenedErrorEventCountMap.put(errorType, initCount);
        } else {
            int count = happenedErrorEventCountMap.get(errorType);
            happenedErrorEventCountMap.put(errorType, ++count);
        }
    }


    public boolean hasThreshholdErrorEvent(){
        return this.getTotalErrorPoint() >= 10.0;
    }

    public Map<ErrorType, Integer> getHappenedErrorEventCountMap() {
        return happenedErrorEventCountMap;
    }

    public List<ErrorType> getErrorEventList() {
        return errorEventList;
    }

    public void clearErrorEventList(){
        errorEventList.clear();
        errorEventPointList.clear();
    }

    public List<Double> getErrorEventPointList() {
        return errorEventPointList;
    }
}
