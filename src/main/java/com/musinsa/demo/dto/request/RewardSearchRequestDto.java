package com.musinsa.demo.dto.request;

import io.swagger.annotations.Example;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RewardSearchRequestDto {
    @Schema(description = "요청 일자", example = "2022-12-12")
    private LocalDateTime userId;
    @Schema(description = "진행 중인 보상 이벤트 ID", example = "1")
    private Long rewardPublishNo;
}
