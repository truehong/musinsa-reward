package com.musinsa.demo.service;

import com.musinsa.demo.domain.Point;
import com.musinsa.demo.domain.Reward;
import com.musinsa.demo.domain.RewardPublish;
import com.musinsa.demo.domain.User;
import com.musinsa.demo.repository.RewardHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PointCalculationService {
    private final RewardHistoryRepository rewardHistoryRepository;

    public Point calculatePointAmount(Reward reward, User user) {
        Optional<RewardPublish> rewardPublish = rewardHistoryRepository.findTopByUserAndRewardOrderById(user, reward);
        Point point = rewardPublish.map((RewardPublish::getPoint)).orElse(new Point());
        if (point.getAmount() < 1000) {
            point.initialize();
        }
        return point.add(100);
    }
}
