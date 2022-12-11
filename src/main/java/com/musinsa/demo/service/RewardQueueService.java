package com.musinsa.demo.service;

import com.musinsa.demo.domain.Reward;
import com.musinsa.demo.domain.User;
import com.musinsa.demo.event.Events;
import com.musinsa.dto.request.PublishedItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class RewardQueueService {
    private static final long FIRST_ELEMENT = 0;
    private static final long PUBLISH_SIZE = 10;
    private static final long LAST_INDEX = 1;
    private final RedisTemplate<String, Object> redisTemplate;

    public void addQueue(Reward reward, User user, long timestamp) {
        redisTemplate.opsForZSet().add(String.valueOf(reward.getNo()), user.getId(), timestamp);
        log.info("added to queue - {} ({} seconds)", user.getId(), timestamp);
    }

    public void publishAll(List<Reward> rewards) {
        final long start = FIRST_ELEMENT;
        final long end = PUBLISH_SIZE - LAST_INDEX;

        for (Reward reward : rewards) {
            Set<Object> queue = redisTemplate.opsForZSet().range(String.valueOf(reward.getNo()), start, end);
            for (Object userId : queue) {
                PublishedItem publishedItem = new PublishedItem(String.valueOf(userId), reward.getNo());
                log.info("user reward published - {} ({} reward)", userId, reward.getNo());
                Events.raise(publishedItem);
                redisTemplate.opsForZSet().remove(String.valueOf(reward.getNo()), userId);
            }
        }
    }

    public Long getOrder(Reward reward, User user) {
        Long rank = redisTemplate.opsForZSet().rank(String.valueOf(reward.getNo()), user.getId());
        return rank;
    }
}
