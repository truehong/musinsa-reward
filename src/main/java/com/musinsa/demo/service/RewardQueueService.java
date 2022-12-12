package com.musinsa.demo.service;

import com.musinsa.demo.domain.RewardPublish;
import com.musinsa.demo.domain.User;
import com.musinsa.demo.event.Events;
import com.musinsa.demo.event.RewardPublishedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RewardQueueService {
    private static final long FIRST_ELEMENT = 0;
    private static final long PUBLISH_SIZE = 10;
    private static final long LAST_INDEX = 1;
    private final RedisTemplate<String, Object> redisTemplate;

    public void addQueue(RewardPublish rewardPublish, User user, long timestamp) {
        redisTemplate.opsForZSet().add(String.valueOf(rewardPublish.getRewardPublishNo()), user.getId(), timestamp);
        log.info("added to queue - {} ({} seconds)", user.getId(), timestamp);
    }

    public void publishAll(List<RewardPublish> rewardPublishes) {
        final long start = FIRST_ELEMENT;
        final long end = PUBLISH_SIZE - LAST_INDEX;

        for (RewardPublish publish : rewardPublishes) {
            Set<Object> users = redisTemplate.opsForZSet().range(String.valueOf(publish.getRewardPublishNo()), start, end);
            for (Object value : users) {
                String userId = String.valueOf(value);
                log.info("user reward register for queue - {} ({} reward)", userId, publish.getRewardPublishNo());
                log.info("user thread - {}, timeStamp - {}", Thread.currentThread(), System.currentTimeMillis());
                redisTemplate.opsForZSet().remove(String.valueOf(publish.getRewardPublishNo()), userId);
                Events.raise(new RewardPublishedEvent(userId, publish.getRewardPublishNo()));
            }
        }
    }


    public Long getOrder(RewardPublish rewardPublish, User user) {
        Long rank = redisTemplate.opsForZSet().rank(String.valueOf(rewardPublish.getRewardPublishNo()), user.getId());
        return rank;
    }
}
