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

import java.lang.reflect.Executable;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    @DisplayName("10명이 넘으면 더이상 지급되지 않는다.")
    void userRewardPublishedTest() throws InterruptedException {
        final int SCHEDULER_EXECUTION_TIME = 5000;
        final int USER_COUNT = 30;
        final int LIMIT_REWARDS_COUNT = 10;
        // given
        List<User> users = 다중_유저_생성(USER_COUNT);
        Reward 테스트_보상 = 보상_생성();

        // when - 병렬 스트림 실행
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
    @DisplayName("한 사용자는 같은날 하루만 발급 가능")
    void sameUserRewardPublishedTest() throws InterruptedException {
        final int numberOfThreads = 2;
        final int SCHEDULER_EXECUTION_TIME = 2000;
        // given
        User 유저_1 = 유저_생성();
        Reward 테스트_보상 = 보상_생성();

        ExecutorService service = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        for (int i = 0; i < numberOfThreads; i++) {
            service.submit(() -> {
                rewardPublishService.register(유저_1.getId(), 테스트_보상.getNo());
                latch.countDown();
            });
        }
        latch.await();
        Thread.sleep(SCHEDULER_EXECUTION_TIME);

        List<RewardPublish> publishes = rewardHistoryRepository.findAllByUserAndRewardAndRegisterDate(유저_1,테스트_보상, LocalDate.now());
        assertThat(publishes.size()).isEqualTo(1);
    }
}
