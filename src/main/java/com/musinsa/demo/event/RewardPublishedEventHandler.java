package com.musinsa.demo.event;

import com.musinsa.demo.service.RewardPublishService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RewardPublishedEventHandler {
    private final RewardPublishService receiveService;

    @Async
    @EventListener(RewardPublishedEvent.class)
    public void handler(RewardPublishedEvent publishedItem) {
        log.info("event thread - {}", Thread.currentThread());
        receiveService.publish(publishedItem.getUserId(), publishedItem.getRewardNo());
    }
}
