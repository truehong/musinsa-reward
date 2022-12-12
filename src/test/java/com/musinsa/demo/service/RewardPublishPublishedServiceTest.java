package com.musinsa.demo.service;

import com.musinsa.demo.domain.RewardHistory;
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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ActiveProfiles("local")
public class RewardPublishPublishedServiceTest extends AbstractRewardReceiveServiceTest {
    @Autowired
    RewardPublishService rewardPublishService;

    @Autowired
    RewardSearchService rewardSearchService;

    @Autowired
    RedisTemplate redisTemplate;

    @Test
    @DisplayName("10명이 넘으면 더이상 지급되지 않는다.")
    void userRewardPublishedTest() throws InterruptedException {
        final int NUMBER_OF_THREAD = 5;
        final int SCHEDULER_EXECUTION_TIME = 5000;
        final int USER_COUNT = 30;
        final int LIMIT_REWARDS_COUNT = 10;
        // given
        RewardPublish 테스트_보상 = 보상_생성();

        ExecutorService service = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(NUMBER_OF_THREAD);
        for (int i = 0; i < NUMBER_OF_THREAD; i++) {
            service.submit(() -> {
                List<User> users = 다중_유저_생성(USER_COUNT);
                users.stream()
                        .forEach(user -> {
                            rewardPublishService.register(user.getId(), 테스트_보상.getRewardPublishNo());
                        });
                latch.countDown();
            });
        }
        latch.await();
        Thread.sleep(SCHEDULER_EXECUTION_TIME);

        List<RewardHistory> publishes = rewardHistoryRepository.findAllByRewardPublish(테스트_보상);
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
        RewardPublish 테스트_보상 = 보상_생성();

        ExecutorService service = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        for (int i = 0; i < numberOfThreads; i++) {
            service.submit(() -> {
                rewardPublishService.register(유저_1.getId(), 테스트_보상.getRewardPublishNo());
                latch.countDown();
            });
        }
        latch.await();
        Thread.sleep(SCHEDULER_EXECUTION_TIME);

        List<RewardHistory> publishes = rewardHistoryRepository.findAllByUserAndRewardPublishAndRegisterDate(유저_1, 테스트_보상, LocalDate.now());
        assertThat(publishes.size()).isEqualTo(1);
    }
}
