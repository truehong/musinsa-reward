package com.musinsa.demo.service;

import com.musinsa.demo.common.enums.Status;
import com.musinsa.demo.domain.Reward;
import com.musinsa.demo.domain.RewardPublish;
import com.musinsa.demo.domain.Stock;
import com.musinsa.demo.domain.User;
import com.musinsa.demo.repository.RewardHistoryRepository;
import com.musinsa.demo.repository.RewardPublishRepository;
import com.musinsa.demo.repository.RewardRepository;
import com.musinsa.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
public abstract class AbstractRewardReceiveServiceTest {
    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected RewardRepository rewardRepository;

    @Autowired
    protected RewardPublishRepository rewardPublishRepository;
    @Autowired
    protected RewardHistoryRepository rewardHistoryRepository;

    private AtomicInteger index = new AtomicInteger((int) System.currentTimeMillis());

    protected void init() {
        this.rewardHistoryRepository.deleteAll();
        this.rewardRepository.deleteAll();
        this.userRepository.deleteAll();
    }

    protected User 유저_생성() {
        String randomId = String.valueOf(index.getAndAdd(1));
        User user = User.builder()
                .id(randomId).build();
        return userRepository.save(user);
    }

    protected List<User> 다중_유저_생성(int userCount) {

        List<User> users = IntStream.range(1, userCount + 1)
                .mapToObj(value ->
                        User.builder()
                                .id(String.valueOf(index.getAndAdd(1))).build())
                .collect(Collectors.toList());

        return userRepository.saveAll(users);
    }

    protected RewardPublish 보상_생성() {
        Reward reward = Reward.builder()
                .rewardStatus(Status.OPEN)
                .title("보상 테스트")
                .build();
        Reward saved = rewardRepository.save(reward);
        RewardPublish rewardPublish = RewardPublish
                .builder()
                .reward(saved)
                .status(Status.OPEN)
                .stock(new Stock(new AtomicInteger(10)))
                .build();
        return rewardPublishRepository.save(rewardPublish);
    }

}
