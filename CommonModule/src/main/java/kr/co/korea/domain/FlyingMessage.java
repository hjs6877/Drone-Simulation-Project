package kr.co.korea.domain;

import java.io.Serializable;

/**
 * Created by ideapad on 2016-02-10.
 */
public enum FlyingMessage implements Serializable{
    STATUS_FLYING_READY,        /** 비행 준비 상태 **/
    STATUS_NEED_REPLACE_LEADER, /** 리더 교체 필요 상태 **/
    STATUS_FLYING_WAITED_FOR_REPLACE_LEADER,       /** 리더 교체를 위한 비행 대기 상태 **/
    STATUS_FLYING_WAITED_FOR_STOP_FLYING,       /** 비행 중지를 위한 비행 대기 상태 **/
    STATUS_NEED_STOP_FLYING,    /** 비행 중지 필요 상태 **/
    STATUS_ELECTED_NEW_LEADER,  /** 새로운 리더가 선출 된 상태 **/
    STATUS_FLYING_ARRIVED,      /** 목적지에 도착한 상태 **/
    DO_FLYING_START,            /** 비행을 시작하라 **/
    DO_FLYING_WAIT_FOR_REPLACE_LEADER,             /** 리더 교체를 위해 비행을 일시중지 대기하라 **/
    DO_FLYING_WAIT_FOR_STOP_FLYING,             /** 비행 중지를 위해 비행을 일시중지 대기하라 **/
    DO_FLYING_RESUME,           /** 중지 된 비행을 재개하라 **/
    DO_FLYING_STOP,             /** 비행을 완전 중지하라 **/
    DO_FLYING_FINISH            /** 착륙 후, 비행을 종료하라 **/
}
