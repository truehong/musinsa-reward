package com.musinsa.demo.dto.request;

import io.swagger.annotations.Example;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class RewardSearchRequestDto {
    @Schema(description = "요청 일자", example = "2022-12-12")
    private LocalDate requestDate;
    @Schema(description = "정렬 방식", example = "ASC")
    private SortType order;
    @Schema(description = "보상 ID", example = "1")
    private Long rewardPublishNo;

    public Sort.Direction getOrder(){
        return order.getDirection();
    }

    @Getter
    @RequiredArgsConstructor
    public enum SortType {
        ASC(Sort.Direction.ASC),
        DESC(Sort.Direction.DESC);
        private final Sort.Direction direction;
    }

}
