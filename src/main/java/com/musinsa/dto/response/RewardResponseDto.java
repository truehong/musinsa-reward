package com.musinsa.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RewardResponseDto {
    private String userId;
    private Integer point;
    private Long rewardNo;
    private Long rank;
}
