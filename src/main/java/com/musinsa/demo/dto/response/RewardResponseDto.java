package com.musinsa.demo.dto.response;

import com.musinsa.demo.domain.RewardHistory;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
public class RewardResponseDto {
    private String userId;
    private Integer point;
    private Long rewardPublishNo;
    private Long rank;
    private LocalDate registerDate;

    public static List<RewardResponseDto> from(List<RewardHistory> publishes) {
        return publishes.stream().map(publish -> RewardResponseDto.builder()
                        .userId(publish.getUser().getId())
                        .rewardPublishNo(publish.getRewardPublish().getRewardPublishNo())
                        .point(publish.getPoint().getAmount())
                        .registerDate(publish.getRegisterDate())
                .build())
                .collect(Collectors.toList());
    }
}
