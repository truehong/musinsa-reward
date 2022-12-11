package com.musinsa.demo.service;

import com.musinsa.demo.common.exception.RewardErrorCode;
import com.musinsa.demo.common.exception.RewardServiceException;
import com.musinsa.demo.domain.*;
import com.musinsa.demo.repository.RewardRepository;
import com.musinsa.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
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
                .orElseThrow(() -> new RewardServiceException(RewardErrorCode.NOT_FOUND_REWARD));
        long now = System.currentTimeMillis();
        validStocks(reward);
        reward.checkDuplication(user);
        rewardQueueService.addQueue(reward, user, now);
    }

    @Override
    public void publish(String userId, Long rewardNo) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RewardServiceException(RewardErrorCode.NOT_FOUND_USER));
        Reward reward = rewardRepository.findById(rewardNo)
                .orElseThrow(() -> new RewardServiceException(RewardErrorCode.NOT_FOUND_REWARD));
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
