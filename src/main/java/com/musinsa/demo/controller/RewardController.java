package com.musinsa.demo.controller;

import com.musinsa.demo.dto.CommonResponse;
import com.musinsa.demo.dto.request.RewardRequestDto;
import com.musinsa.demo.service.RewardPublishService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@ApiOperation(value = "보상 요청 Controller")
@RequestMapping("/reward")
@RequiredArgsConstructor
public class RewardController {
    private final RewardPublishService rewardPublishService;

    @PostMapping
    @ApiOperation(value = "유저의 보상 요청 API", notes = "요청 결과만 확인 가능 합니다. 발급 내역은 보상 발급 내역 조회 API를 통해 확인 가능합니다. \n " +
            " 1. rewardPublishNo - 1 프로젝트 생성시 자동 생성 \n " +
            " 2. userId - 요청 API 에서 계정 생성, 이후 API 에서 중복 체크\n")
    @ApiResponses({
            @ApiResponse(code = 0, message = "요청 성공"),
            @ApiResponse(code = 404, message = "등록 되지 않은 보상 이벤트 ID 로 요청 시 실패")
    })
    @ResponseStatus(HttpStatus.OK)
    public CommonResponse<Void> createRewardRequest(@RequestBody RewardRequestDto requestDto) {
        rewardPublishService.register(requestDto.getUserId(), requestDto.getRewardPublishNo());
        return CommonResponse.ok();
    }
}
