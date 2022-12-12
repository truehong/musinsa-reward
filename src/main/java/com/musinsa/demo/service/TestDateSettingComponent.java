package com.musinsa.demo.service;

import com.musinsa.demo.common.enums.Status;
import com.musinsa.demo.domain.Reward;
import com.musinsa.demo.domain.RewardPublish;
import com.musinsa.demo.domain.Stock;
import com.musinsa.demo.repository.RewardPublishRepository;
import com.musinsa.demo.repository.RewardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
@RequiredArgsConstructor
public class TestDateSettingComponent {
    private final RewardRepository rewardRepository;
    private final RewardPublishRepository rewardPublishRepository;

    @PostConstruct
    public void createReward() {
        Reward reward = Reward.builder().cronExpression("0 0 * * *")
                .rewardStatus(Status.OPEN)
                .title("무신사 보상 이벤트")
                .build();
        Reward saved = rewardRepository.save(reward);

        RewardPublish rewardPublish = RewardPublish.builder()
                .reward(saved)
                .stock(new Stock(new AtomicInteger(10)))
                .status(Status.OPEN)
                .build();
        rewardPublishRepository.save(rewardPublish);
    }
}
