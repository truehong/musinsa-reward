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
@RequestMapping("/reward")
@RequiredArgsConstructor
public class RewardController {
    private final RewardPublishService rewardPublishService;

    @PostMapping
    @ApiOperation(value = "보상 요청 등록")
    @ApiResponses({
            @ApiResponse(code = 0, message = "요청 등록 성공"),
            @ApiResponse(code = 10002, message = "등록 되지 않은 보상 이벤트 ID 로 요청 시 실패")
    })
    @ResponseStatus(HttpStatus.OK)
    public CommonResponse<Void> registerForReward(@RequestBody RewardRequestDto requestDto) {
        rewardPublishService.register(requestDto.getUserId(), requestDto.getRewardNo());
        return CommonResponse.ok();
    }
}
