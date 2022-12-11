package com.musinsa.demo.service;

import com.musinsa.demo.common.exception.ServiceErrorType;
import com.musinsa.demo.common.exception.RewardServiceException;
import com.musinsa.demo.domain.Reward;
import com.musinsa.demo.domain.User;
import com.musinsa.demo.dto.response.RewardResponseDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
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
class RewardReceiveServiceTest extends AbstractRewardReceiveServiceTest {

    @Autowired
    RewardPublishService rewardPublishService;

    @Autowired
    RewardSearchService rewardSearchService;


    @Test
    @DisplayName("유저의 보상 등록 테스트")
    void userRewardRegisterTest() {
        // given
        User 테스트_유저 = 유저_생성();
        Reward 테스트_보상 = 보상_생성();
        // when
        rewardPublishService.register(테스트_유저.getId(), 테스트_보상.getNo());
        RewardResponseDto 보상_지급 = rewardSearchService.getDetails(테스트_유저.getId(), 테스트_보상.getNo());
        //then
        Assertions.assertEquals(보상_지급.getPoint(), 100);
        Assertions.assertEquals(보상_지급.getUserId(), 테스트_유저.getId());
    }

    @Test
    @DisplayName("같은 유저가 두번 등록시 에러 발생")
    void userRewardRegisterExceptionTest() {
        // given
        User 테스트_유저 = 유저_생성();
        Reward 테스트_보상 = 보상_생성();
        // when
        rewardPublishService.register(테스트_유저.getId(), 테스트_보상.getNo());

        Throwable 보상_지급_한번더 = catchThrowable(() -> rewardPublishService.register(테스트_유저.getId(), 테스트_보상.getNo()));
        assertThat(보상_지급_한번더).isInstanceOf(RewardServiceException.class);
        assertThat(보상_지급_한번더).withFailMessage(ServiceErrorType.USER_DUPLICATE_REGISTER.getMessage());
    }

    @Test
    @DisplayName("하루에 10건 이상 보상 신청 시 에러 발생")
    void rewardOutOfStockExceptionTest() {
        // given
        Reward 테스트_보상 = 보상_생성();
        List<User> 열명의_유저들 = 다중_유저_생성(10);
        User 열한번째_유저 = 유저_생성();

        // when
        assertThat(열명의_유저들.size()).isEqualTo(10);
        열명의_유저들.forEach(user -> rewardPublishService.register(user.getId(), 테스트_보상.getNo()));

        // then
        Throwable 열한번째_보상 = catchThrowable(() -> rewardPublishService.register(열한번째_유저.getId(), 테스트_보상.getNo()));
        assertThat(열한번째_보상).isInstanceOf(RewardServiceException.class);
        assertThat(열한번째_보상).withFailMessage(ServiceErrorType.OUT_OF_REWARD_STOCK.getMessage());
    }
}