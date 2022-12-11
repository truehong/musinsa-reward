package com.musinsa.demo.service;

import com.musinsa.demo.domain.Point;
import com.musinsa.demo.domain.Reward;
import com.musinsa.demo.domain.RewardPublish;
import com.musinsa.demo.domain.User;
import com.musinsa.demo.repository.RewardHistoryRepository;
import com.musinsa.demo.repository.RewardRepository;
import com.musinsa.demo.repository.UserRepository;
import com.musinsa.demo.dto.response.RewardResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RewardSearchService {
    private final RewardRepository rewardRepository;
    private final UserRepository userRepository;
    private final RewardQueueService rewardQueueService;
    private final RewardHistoryRepository rewardHistoryRepository;

    public RewardResponseDto getDetails(String userId, Long rewardNo) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException());
        Reward reward = rewardRepository.findById(rewardNo)
                .orElseThrow(() -> new RuntimeException());
        Optional<RewardPublish> rewardPublish = rewardHistoryRepository.findTopByUserAndRewardOrderById(user, reward);
        Point point = rewardPublish.map((RewardPublish::getPoint)).orElse(new Point());
        Long rank = rewardQueueService.getOrder(reward, user);
        return RewardResponseDto
                .builder()
                .userId(userId)
                .rank(rank)
                .point(point.getAmount())
                .build();
    }
}
