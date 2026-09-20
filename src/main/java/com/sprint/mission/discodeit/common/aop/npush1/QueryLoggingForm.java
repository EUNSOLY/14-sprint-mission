package com.sprint.mission.discodeit.common.aop.npush1;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class QueryLoggingForm {
    public String apiUrl;
    public String apiMethod;
    private long queryCount = 0;
    private long queryTime = 0;


    public void queryCountUp() {
        this.queryCount++;
    }

    public void addQueryTime(Long queryTime) {
        this.queryTime += queryTime;
    }
}
