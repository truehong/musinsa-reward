package com.musinsa.demo.service;

import com.musinsa.demo.domain.Reward;
import com.musinsa.demo.domain.RewardPublish;
import com.musinsa.demo.domain.User;
import com.musinsa.demo.repository.RewardHistoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ActiveProfiles("local")
public class RewardPublishedServiceTest extends AbstractRewardReceiveServiceTest {
    @Autowired
    RewardPublishService rewardPublishService;

    @Autowired
    RewardSearchService rewardSearchService;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    RewardHistoryRepository rewardHistoryRepository;

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
                .forEach(v -> {
                    rewardPublishService.register(v.getId(), 테스트_보상.getNo());
                });

        Thread.sleep(SCHEDULER_EXECUTION_TIME);

        List<RewardPublish> publishes = rewardHistoryRepository.findAllByRewardAndRegisterDateOrderByRegisterDateDesc(테스트_보상, LocalDate.now());
        // then
        assertThat(publishes.size()).isEqualTo(LIMIT_REWARDS_COUNT);
    }
}
