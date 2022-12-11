package com.musinsa.demo.service;

public interface RewardPublishService {
    void register(String userId, Long rewardNo);
    void publish(String userId, Long rewardNo);
}
