package com.musinsa.demo.service;

import com.musinsa.demo.domain.Reward;
import com.musinsa.demo.domain.User;
import com.musinsa.demo.dto.RewardResponseDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("local")
class RewardReceiveServiceTest extends AbstractRewardReceiveServiceTest{

    @Autowired
    RewardServiceImpl rewardReceiveService;

    @Test
    @DisplayName("유저의 보상 등록 테스트")
    void receive() {
        // given
        User 테스트_유저 = 유저_생성("user");
        Reward 테스트_보상 = 보상_생성();
        // when
        RewardResponseDto 보상_지급 = rewardReceiveService.receive(테스트_유저.getId(), 테스트_보상.getNo());
        //then
        Assertions.assertEquals(보상_지급.getPoint(), 100);
        Assertions.assertEquals(보상_지급.getUserId(), 테스트_유저.getId());
    }
}