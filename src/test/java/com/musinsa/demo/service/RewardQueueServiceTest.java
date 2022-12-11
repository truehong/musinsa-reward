package com.musinsa.demo.service;

import com.musinsa.demo.domain.Reward;
import com.musinsa.demo.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("local")
class RewardQueueServiceTest extends AbstractRewardReceiveServiceTest {
    @Autowired
    RewardQueueService rewardQueueService;

    @Autowired
    RedisTemplate redisTemplate;

    @Test
    @DisplayName("큐에 대기 하는 유저의 순서 확인")
    public void addItemTest() {

        //given
        User user1 = 유저_생성("user1");
        User user2 = 유저_생성("user2");
        User user3 = 유저_생성("user3");
        Reward reward = 보상_생성();

        // when
        rewardQueueService.addQueue(reward, user1, System.currentTimeMillis());
        rewardQueueService.addQueue(reward, user2, System.currentTimeMillis());
        rewardQueueService.addQueue(reward, user3, System.currentTimeMillis());

        long orderOfUser1 = rewardQueueService.getOrder(reward, user1);
        long orderOfUser2 = rewardQueueService.getOrder(reward, user2);
        long orderOfUser3 = rewardQueueService.getOrder(reward, user3);

        // then
        assertThat(orderOfUser1).isEqualTo(0);
        assertThat(orderOfUser2).isEqualTo(1);
        assertThat(orderOfUser3).isEqualTo(2);
    }


}