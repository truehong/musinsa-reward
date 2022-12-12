package com.musinsa.demo.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class RewardRequestDto {
    @Schema(example = "userId")
    private String userId;
    @Schema(example = "1")
    private Long rewardPublishNo;
}
