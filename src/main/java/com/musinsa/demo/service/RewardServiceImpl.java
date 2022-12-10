package com.musinsa.demo.service;

import com.musinsa.demo.domain.Point;
import com.musinsa.demo.domain.Reward;
import com.musinsa.demo.domain.RewardPublish;
import com.musinsa.demo.domain.User;
import com.musinsa.dto.response.RewardResponseDto;
import com.musinsa.demo.repository.RewardRepository;
import com.musinsa.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RewardServiceImpl implements ReceiveService {
    private final RewardRepository rewardRepository;
    private final UserRepository userRepository;
    private final PointCalculationService pointCalculationService;
    @Override
    public RewardResponseDto receive(String userId, Long rewardNo) {
        User user =  userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException());
        Reward reward = rewardRepository.findById(rewardNo)
                .orElseThrow(()-> new RuntimeException()); // todo : exception 생성
        Point point = pointCalculationService.calculatePointAmount(reward, user);
        RewardPublish rewardPublish = new RewardPublish(user, point);
        reward.publish(rewardPublish);
        rewardRepository.save(reward);
        return RewardResponseDto
                .builder()
                .userId(userId)
                .point(point.getAmount())
                .build();
    }
}
