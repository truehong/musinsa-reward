package com.musinsa.demo.service;

import com.musinsa.demo.dto.RewardResponseDto;

public interface ReceiveService {
    RewardResponseDto receive(String userId, Long rewardNo);
}
