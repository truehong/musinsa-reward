package com.musinsa.demo.common.schedule;

import com.musinsa.demo.common.enums.Status;
import com.musinsa.demo.domain.Reward;
import com.musinsa.demo.domain.RewardPublish;
import com.musinsa.demo.domain.Stock;
import com.musinsa.demo.repository.RewardPublishRepository;
import com.musinsa.demo.repository.RewardRepository;
import com.musinsa.demo.service.RewardQueueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
@RequiredArgsConstructor
public class RewardEventScheduler {

    private final RewardQueueService rewardQueueService;

    private final RewardPublishRepository rewardPublishRepository;
    private final RewardRepository rewardRepository;

    // todo : create cache server
    @Scheduled(fixedDelay = 1000)
    private void rewardPublishScheduler() {
        log.info("reward event scheduler executed [ at {} ] ", LocalDateTime.now());
        List<RewardPublish> rewardPublish = rewardPublishRepository.findAllByStatus(Status.OPEN);
        if (!rewardPublish.isEmpty()) {
            rewardQueueService.publishAll(rewardPublish);
        }
    }

    // todo : create cache server
    @Scheduled(cron = "0 0 * * * *")
    private void rewardOpenScheduler() {
        log.info("reward event scheduler executed [ at {} ] ", LocalDateTime.now());
        List<Reward> rewards = rewardRepository.findAllByCronExpressionAndRewardStatus("0 0 * * * *", Status.OPEN);
        for (Reward reward : rewards) {
            List<RewardPublish> publishes = rewardPublishRepository.findAllByRewardAndStatus(reward, Status.OPEN);
            closeExistSchedule(publishes);
            rewardPublishRepository.saveAll(publishes);
            RewardPublish newPublish = RewardPublish.builder()
                    .reward(reward)
                    .stock(new Stock(new AtomicInteger(10)))
                    .status(Status.OPEN)
                    .build();
            rewardPublishRepository.save(newPublish);
        }


    }

    private void closeExistSchedule(List<RewardPublish> publishes) {
        for (RewardPublish rewardPublish : publishes) {
            rewardPublish.closeRewardPublish();
        }
    }
}

