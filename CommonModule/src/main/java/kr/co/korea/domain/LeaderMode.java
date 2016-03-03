package kr.co.korea.domain;

/**
 * Created by ideapad on 2016-03-03.
 */
public enum LeaderMode {
    STATIC_LEADER_MODE(1),
    DYNAMIC_LEADER_REPLACE_MODE(2);

    private int value;

    LeaderMode(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
