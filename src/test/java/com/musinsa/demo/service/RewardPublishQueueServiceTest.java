package com.musinsa.demo.service;

import com.musinsa.demo.domain.RewardPublish;
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
class RewardPublishQueueServiceTest extends AbstractRewardReceiveServiceTest {
    @Autowired
    RewardQueueService rewardQueueService;

    @Autowired
    RedisTemplate redisTemplate;

    @Test
    @DisplayName("큐에 대기 하는 유저의 순서 확인")
    public void addItemTest() {

        //given
        User user1 = 유저_생성();
        User user2 = 유저_생성();
        User user3 = 유저_생성();
        RewardPublish rewardPublish = 보상_생성();

        // when
        rewardQueueService.addQueue(rewardPublish, user1, System.currentTimeMillis());
        rewardQueueService.addQueue(rewardPublish, user2, System.currentTimeMillis());
        rewardQueueService.addQueue(rewardPublish, user3, System.currentTimeMillis());

        long orderOfUser1 = rewardQueueService.getOrder(rewardPublish, user1);
        long orderOfUser2 = rewardQueueService.getOrder(rewardPublish, user2);
        long orderOfUser3 = rewardQueueService.getOrder(rewardPublish, user3);

        // then
        assertThat(orderOfUser1).isEqualTo(0);
        assertThat(orderOfUser2).isEqualTo(1);
        assertThat(orderOfUser3).isEqualTo(2);
    }


}