package com.musinsa.demo.dto.request;

import lombok.Getter;

@Getter
public class PublishedItem {
    private String userId;
    private Long rewardNo;

    public PublishedItem(String userId, Long reward) {
        this.userId = userId;
        this.rewardNo = reward;
    }
}
