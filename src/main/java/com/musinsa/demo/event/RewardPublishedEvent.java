package com.musinsa.demo.event;

import com.musinsa.demo.dto.request.PublishedItem;

public class RewardPublishedEvent {
    PublishedItem item;

    public RewardPublishedEvent(PublishedItem item) {
        this.item = item;
    }
}
