package com.musinsa.demo.event;

import lombok.Getter;

@Getter
public class RewardPublishedEvent {
    private String userId;
    private Long rewardNo;

    public RewardPublishedEvent(String userId, Long rewardNo) {
        this.userId = userId;
        this.rewardNo = rewardNo;
    }
}
