package com.musinsa.demo.controller;

import com.musinsa.demo.dto.CommonResponse;
import com.musinsa.demo.dto.response.RewardResponseDto;
import com.musinsa.demo.service.RewardSearchService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@ApiOperation(value = "보상 내역 조회 Controller")
@RequestMapping("/reward")
@RequiredArgsConstructor
public class RewardSearchController {
    private final RewardSearchService rewardSearchService;

    @GetMapping("/{rewardNo}")
    @ApiOperation(value = "당일 보상 발급 리스트 조회 API")
    @ApiResponses({
            @ApiResponse(code = 0, message = "조회 성공"),
            @ApiResponse(code = 404, message = "요청 값에 대한 데이터가 존재 하지 않을 경우")
    })
    @ResponseStatus(HttpStatus.OK)
    public CommonResponse<RewardResponseDto> getPublishedRewardTodayList(@ApiParam(value = "rewardNo", example = "1", required = true)
                                                                         @RequestParam("rewardNo") Long rewardNo) {
        List<RewardResponseDto> responses = rewardSearchService.getDetailsBy(rewardNo, LocalDate.now());
        return new CommonResponse(responses);
    }

    @GetMapping("/{rewardNo}/{userId}")
    @ApiOperation(value = "사용자의 보상 발급 내역 조회 API")
    @ApiResponses({
            @ApiResponse(code = 0, message = "요청 조회 성공"),
            @ApiResponse(code = 404, message = "요청 값에 대한 데이터가 존재 하지 않을 경우")
    })
    @ResponseStatus(HttpStatus.OK)
    public CommonResponse<RewardResponseDto> getPublishedRewardDetail(
            @ApiParam(value = "userId", example = "random_1", required = true)
            @PathVariable("userId") String userId,
            @ApiParam(value = "rewardNo", example = "1", required = true)
            @PathVariable("rewardNo") Long rewardNo) {
        RewardResponseDto response = rewardSearchService.getDetail(userId, rewardNo);
        return new CommonResponse(response);
    }

}
