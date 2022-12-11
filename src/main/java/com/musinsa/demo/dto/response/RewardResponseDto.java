package com.musinsa.demo.dto.response;

import com.musinsa.demo.domain.RewardPublish;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
public class RewardResponseDto {
    private String userId;
    private Integer point;
    private Long rewardNo;
    private Long rank;

    public static List<RewardResponseDto> from(List<RewardPublish> publishes) {
        return publishes.stream().map(publish -> RewardResponseDto.builder()
                .userId(publish.getUser().getId())
                .rewardNo(publish.getReward().getNo())
                .point(publish.getPoint().getAmount())
                .build())
                .collect(Collectors.toList());
    }
}
