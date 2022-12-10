package com.musinsa.demo.service;

import com.musinsa.dto.response.RewardResponseDto;

public interface ReceiveService {
    RewardResponseDto receive(String userId, Long rewardNo);
}
