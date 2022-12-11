package com.musinsa.demo.service;

import com.musinsa.demo.common.exception.ServiceErrorType;
import com.musinsa.demo.common.exception.RewardServiceException;
import com.musinsa.demo.domain.Reward;
import com.musinsa.demo.domain.RewardPublish;
import com.musinsa.demo.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;


@SpringBootTest
@ActiveProfiles("local")
public class RewardPublishedServiceTest extends AbstractRewardReceiveServiceTest {
    @Autowired
    RewardPublishService rewardPublishService;

    @Autowired
    RewardSearchService rewardSearchService;

    @Autowired
    RedisTemplate redisTemplate;

    @Test
    @DisplayName("유저의 보상 발행 테스트(30개요청)-하루에 10개만 발행 가능")
    void userRewardPublishedTest() throws InterruptedException {
        final int SCHEDULER_EXECUTION_TIME = 5000;
        final int USER_COUNT = 30;
        final int LIMIT_REWARDS_COUNT = 10;
        // given
        List<User> users = 다중_유저_생성(USER_COUNT, "user_");
        Reward 테스트_보상 = 보상_생성();

        // when
        users.stream()
                .parallel()
                .forEach(v -> {
                    rewardPublishService.register(v.getId(), 테스트_보상.getNo());
                });

        Thread.sleep(SCHEDULER_EXECUTION_TIME);

        List<RewardPublish> publishes = rewardHistoryRepository.findAllByRewardAndRegisterDateOrderByRegisterDateDesc(테스트_보상, LocalDate.now());
        // then
        assertThat(publishes.size()).isEqualTo(LIMIT_REWARDS_COUNT);
    }

    @Test
    @DisplayName("유저의 보상 발행 테스트(동일 유저 요청)-하루에 1건만 발행 가능")
    void sameUserRewardPublishedTest() throws InterruptedException {
        final int REWARD_LIMIT = 10;
        final int SCHEDULER_EXECUTION_TIME = 5000;
        final int REQUEST_USER_COUNT = 2;
        // given
        User 유저_1 = 유저_생성("user_1");
        User 유저_2 = 유저_생성("user_2");
        Reward 테스트_보상 = 보상_생성();

        // when - 반복 요청 : redis set 자료형 으로 반복 요청 시에도 하나만 전달됨
        rewardPublishService.register(유저_1.getId(), 테스트_보상.getNo());
        rewardPublishService.register(유저_1.getId(), 테스트_보상.getNo());
        rewardPublishService.register(유저_2.getId(), 테스트_보상.getNo());
        rewardPublishService.register(유저_2.getId(), 테스트_보상.getNo());
        rewardPublishService.register(유저_1.getId(), 테스트_보상.getNo());

        Thread.sleep(SCHEDULER_EXECUTION_TIME);
        // when - 중복 요청 : 중복 에러 발생
        Throwable 중복_요청_유저_1 = catchThrowable(() -> rewardPublishService.register(유저_1.getId(), 테스트_보상.getNo()));
        Throwable 중복_요청_유저_2 = catchThrowable(() -> rewardPublishService.register(유저_2.getId(), 테스트_보상.getNo()));

        Thread.sleep(SCHEDULER_EXECUTION_TIME);


        // then
        assertThat(중복_요청_유저_1).isInstanceOf(RewardServiceException.class);
        assertThat(중복_요청_유저_2).isInstanceOf(RewardServiceException.class);
        assertThat(중복_요청_유저_1).withFailMessage(ServiceErrorType.USER_DUPLICATE_REGISTER.getMessage());
        assertThat(중복_요청_유저_2).withFailMessage(ServiceErrorType.USER_DUPLICATE_REGISTER.getMessage());

        List<RewardPublish> publishes = rewardHistoryRepository.findAllByRewardAndRegisterDate(테스트_보상, LocalDate.now());
        assertThat(publishes.size()).isEqualTo(2);
        Reward reward = rewardRepository.findById(테스트_보상.getNo()).get();
        assertThat(reward.getStock().getRemains()).isEqualTo(REWARD_LIMIT - REQUEST_USER_COUNT);
    }
}
