package com.musinsa.demo.service;

import com.musinsa.demo.domain.Reward;
import com.musinsa.demo.domain.Stock;
import com.musinsa.demo.repository.RewardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@RequiredArgsConstructor
public class TestDateSettingComponent {
    private final RewardRepository rewardRepository;

    @PostConstruct
    public void createReward() {
        Stock stock = Stock.builder().limit(10).remains(10).build();
        rewardRepository.save(Reward.builder().stock(stock).build());
    }
}
