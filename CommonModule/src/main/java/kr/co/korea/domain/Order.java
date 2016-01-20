package kr.co.korea.domain;

import java.io.Serializable;

/**
 * Created by kjs on 2016-01-19.
 */
public class Order implements Serializable {
    public final static String PROCESS_EXIT = "exit";

    private String processOrder;

    public String getProcessOrder() {
        return processOrder;
    }

    public void setProcessOrder(String processOrder) {
        this.processOrder = processOrder;
    }
}
