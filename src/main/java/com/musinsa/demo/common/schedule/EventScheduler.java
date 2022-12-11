package com.musinsa.demo.common.schedule;

import com.musinsa.demo.domain.Reward;
import com.musinsa.demo.repository.RewardRepository;
import com.musinsa.demo.service.RewardQueueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventScheduler {

    private final RewardQueueService rewardQueueService;
    private final RewardRepository rewardRepository;

    @Scheduled(fixedDelay = 1000)
    private void rewardEventScheduler() {
        log.info("reward event scheduler executed [ at {} ] ", LocalDateTime.now());
        List<Reward> reward = rewardRepository.findAll();
        rewardQueueService.publishAll(reward);
    }
}

