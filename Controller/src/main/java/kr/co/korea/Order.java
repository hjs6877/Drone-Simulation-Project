package kr.co.korea;

import java.io.Serializable;

/**
 * Created by kjs on 2016-01-19.
 */
public class Order implements Serializable {
    private final static String PROCESS_EXIT = "exit";

    private String processOrder;

    public static String getProcessExit() {
        return PROCESS_EXIT;
    }

    public String getProcessOrder() {
        return processOrder;
    }

    public void setProcessOrder(String processOrder) {
        this.processOrder = processOrder;
    }
}
