package com.musinsa.demo.service;

import com.musinsa.demo.common.exception.RewardNotFoundException;
import com.musinsa.demo.common.exception.UserNotFoundException;
import com.musinsa.demo.domain.*;
import com.musinsa.demo.repository.RewardRepository;
import com.musinsa.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RewardPublishServiceImpl implements RewardPublishService {
    private final RewardRepository rewardRepository;
    private final UserRepository userRepository;
    private final PointCalculationService pointCalculationService;
    private final RewardQueueService rewardQueueService;

    @Override
    public void register(String userId, Long rewardNo) {
        User user = userRepository.findById(userId)
                .orElseGet(() -> create(userId));
        Reward reward = rewardRepository.findById(rewardNo)
                .orElseThrow(() -> new RewardNotFoundException(String.valueOf(rewardNo)));
        long now = System.currentTimeMillis();
        validStocks(reward);
        reward.checkDuplication(user);
        rewardQueueService.addQueue(reward, user, now);
    }

    @Override
    public void publish(String userId, Long rewardNo) {
        log.info("RewardPublishService.publish method invoked - [userid ={}, rewardNo ={}]", userId, rewardNo);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        Reward reward = rewardRepository.findById(rewardNo)
                .orElseThrow(() -> new RewardNotFoundException(String.valueOf(rewardNo)));
        if (!validStocks(reward)) return;
        Point point = pointCalculationService.calculatePointAmount(reward, user);
        RewardPublish rewardPublish = new RewardPublish(user, point);
        reward.publish(rewardPublish);
        rewardRepository.save(reward);
    }

    private boolean validStocks(Reward reward) {
        final Stock stock = reward.getStock();
        return stock != null && stock.getRemains() > 0;
    }

    private User create(String userId) {
        User newUser = User.builder()
                .id(userId).build();
        return userRepository.save(newUser);
    }
}
