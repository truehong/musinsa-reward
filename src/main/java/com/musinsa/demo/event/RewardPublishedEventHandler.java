package com.musinsa.demo.event;

import com.musinsa.demo.service.RewardPublishService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RewardPublishedEventHandler {
    private final RewardPublishService receiveService;

    @EventListener(RewardPublishedEvent.class)
    public void handler(RewardPublishedEvent publishedItem) {
        receiveService.publish(publishedItem.getUserId(), publishedItem.getRewardNo());
    }
}
