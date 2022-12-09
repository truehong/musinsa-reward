package com.musinsa.demo.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RewardResponseDto {
    String userId;
    Integer point;
    Long rewardNo;
}
