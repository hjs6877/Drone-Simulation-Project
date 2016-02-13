package kr.co.korea.domain;

import java.io.Serializable;

/**
 * Created by ideapad on 2016-02-10.
 */
public enum FlyingMessage implements Serializable{
    STATUS_FLYING_READY,        /** 비행 대기 상태 **/
    STATUS_NEED_REPLACE_LEADER, /** 리더 교체 필요 상태 **/
    STATUS_FLYING_WAITED,       /** 비행 대기 상태 **/
    STATUS_ELECTED_NEW_LEADER,  /** 새로운 리더가 선출 된 상태 **/
    STATUS_FLYING_ARRIVED,      /** 목적지에 도착한 상태 **/
    DO_FLYING_START,            /** 비행을 시작하라 **/
    DO_FLYING_WAIT,             /** 비행을 일시 중지 대기하라 **/
    DO_FLYING_RESUME,           /** 중지 된 비행을 재개하라 **/
    DO_FLYING_STOP,             /** 비행을 완전 중지하라 **/
    DO_ELECT_NEW_LEADER         /** 새로운 리더를 선출하라 **/
}
