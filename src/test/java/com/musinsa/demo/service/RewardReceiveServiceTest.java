package com.musinsa.demo.service;

import com.musinsa.common.exception.RewardErrorCode;
import com.musinsa.common.exception.RewardServiceException;
import com.musinsa.demo.domain.Reward;
import com.musinsa.demo.domain.User;
import com.musinsa.dto.response.RewardResponseDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

@SpringBootTest
@ActiveProfiles("local")
class RewardReceiveServiceTest extends AbstractRewardReceiveServiceTest{

    @Autowired
    RewardServiceImpl rewardReceiveService;

    @Test
    @DisplayName("유저의 보상 등록 테스트")
    void userRewardRegisterTest() {
        // given
        User 테스트_유저 = 유저_생성("user");
        Reward 테스트_보상 = 보상_생성();
        // when
        RewardResponseDto 보상_지급 = rewardReceiveService.receive(테스트_유저.getId(), 테스트_보상.getNo());
        //then
        Assertions.assertEquals(보상_지급.getPoint(), 100);
        Assertions.assertEquals(보상_지급.getUserId(), 테스트_유저.getId());
    }

    @Test
    @DisplayName("같은 유저가 두번 등록시 EXCEPTION 발생")
    void userRewardRegisterExceptionTest() {
        // given
        User 테스트_유저 = 유저_생성("user");
        Reward 테스트_보상 = 보상_생성();
        // when
        RewardResponseDto 보상_지급 = rewardReceiveService.receive(테스트_유저.getId(), 테스트_보상.getNo());

        Throwable 보상_지급_한번더 =  catchThrowable(() -> rewardReceiveService.receive(테스트_유저.getId(), 테스트_보상.getNo()));
        assertThat(보상_지급_한번더).isInstanceOf(RewardServiceException.class);
        assertThat(보상_지급_한번더).withFailMessage(RewardErrorCode.USER_DUPLICATE_REGISTER.getMessage());
   }



}