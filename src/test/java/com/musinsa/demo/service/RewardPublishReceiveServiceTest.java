package com.musinsa.demo.service;

import com.musinsa.demo.common.exception.RewardServiceException;
import com.musinsa.demo.common.exception.ServiceErrorType;
import com.musinsa.demo.domain.RewardPublish;
import com.musinsa.demo.domain.User;
import com.musinsa.demo.dto.response.RewardResponseDto;
import jdk.jfr.Description;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

@Transactional
@SpringBootTest
@ActiveProfiles("local")
class RewardPublishReceiveServiceTest extends AbstractRewardReceiveServiceTest {

    @Autowired
    RewardPublishService rewardPublishService;

    @Autowired
    RewardSearchService rewardSearchService;

    @Description("유저 포인트 생성 확인")
    void userRewardRegisterTest() throws InterruptedException {
        final int SCHEDULER_EXECUTION_TIME = 5000;
        // given
        User 테스트_유저 = 유저_생성();
        RewardPublish 테스트_보상 = 보상_생성();
        // when
        rewardPublishService.register(테스트_유저.getId(), 테스트_보상.getRewardPublishNo());
        Thread.sleep(SCHEDULER_EXECUTION_TIME);
        RewardResponseDto 보상_지급 = rewardSearchService.getDetail(테스트_유저.getId(), 테스트_보상.getRewardPublishNo());
        //then
        Assertions.assertEquals(보상_지급.getPoint(), 100);
        Assertions.assertEquals(보상_지급.getUserId(), 테스트_유저.getId());
    }

    void userRewardRegisterExceptionTest() {
        // given
        User 테스트_유저 = 유저_생성();
        RewardPublish 테스트_보상 = 보상_생성();
        // when
        rewardPublishService.register(테스트_유저.getId(), 테스트_보상.getRewardPublishNo());

        Throwable 보상_지급_한번더 = catchThrowable(() -> rewardPublishService.register(테스트_유저.getId(), 테스트_보상.getRewardPublishNo()));
        assertThat(보상_지급_한번더).isInstanceOf(RewardServiceException.class);
        assertThat(보상_지급_한번더).withFailMessage(ServiceErrorType.USER_DUPLICATE_REGISTER.getMessage());
    }

    void rewardOutOfStockExceptionTest() {
        // given
        RewardPublish 테스트_보상 = 보상_생성();
        List<User> 열명의_유저들 = 다중_유저_생성(10);
        User 열한번째_유저 = 유저_생성();

        // when
        assertThat(열명의_유저들.size()).isEqualTo(10);
        열명의_유저들.forEach(user -> rewardPublishService.register(user.getId(), 테스트_보상.getRewardPublishNo()));

        // then
        Throwable 열한번째_보상 = catchThrowable(() -> rewardPublishService.register(열한번째_유저.getId(), 테스트_보상.getRewardPublishNo()));
        assertThat(열한번째_보상).isInstanceOf(RewardServiceException.class);
        assertThat(열한번째_보상).withFailMessage(ServiceErrorType.OUT_OF_REWARD_STOCK.getMessage());
    }
}