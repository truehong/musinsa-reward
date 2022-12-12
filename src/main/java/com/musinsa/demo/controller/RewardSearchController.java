package com.musinsa.demo.controller;

import com.musinsa.demo.dto.CommonResponse;
import com.musinsa.demo.dto.request.RewardSearchRequestDto;
import com.musinsa.demo.dto.response.RewardResponseDto;
import com.musinsa.demo.service.RewardSearchService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@ApiOperation(value = "보상 내역 조회 Controller")
@RequestMapping("/search/rewards")
@RequiredArgsConstructor
public class RewardSearchController {
    private final RewardSearchService rewardSearchService;

    @PostMapping("/users")
    @ApiOperation(value = "당일 보상 발급 리스트 조회 API")
    @ApiResponses({
            @ApiResponse(code = 0, message = "조회 성공"),
            @ApiResponse(code = 404, message = "요청 값에 대한 데이터가 존재 하지 않을 경우")
    })
    @ResponseStatus(HttpStatus.OK)
    public CommonResponse<RewardResponseDto> getPublishedRewardHistory(@RequestBody RewardSearchRequestDto dto) {
        List<RewardResponseDto> responses = rewardSearchService.getSearchRewardList(dto.getRewardPublishNo(), dto.getRequestDate(), dto.getOrder());
        return new CommonResponse(responses);
    }

    @PostMapping("/users/{userId}")
    @ApiOperation(value = "사용자의 보상 발급 내역 조회 API")
    @ApiResponses({
            @ApiResponse(code = 0, message = "요청 조회 성공"),
            @ApiResponse(code = 404, message = "요청 값에 대한 데이터가 존재 하지 않을 경우")
    })
    @ResponseStatus(HttpStatus.OK)
    public CommonResponse<RewardResponseDto> getPublishedUserRewardDetail(
            @ApiParam(value = "userId", example = "userId", required = true)
            @PathVariable("userId") String userId,
            @ApiParam(value = "rewardPublishNo", example = "1", required = true)
            @RequestParam("rewardPublishNo") Long rewardPublishNo) {
        RewardResponseDto response = rewardSearchService.getDetail(userId, rewardPublishNo);
        return new CommonResponse(response);
    }

}
