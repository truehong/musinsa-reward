package com.musinsa.demo.service;

import com.musinsa.demo.common.exception.RewardNotFoundException;
import com.musinsa.demo.common.exception.UserNotFoundException;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RewardSearchService {
    private final RewardRepository rewardRepository;
    private final UserRepository userRepository;
    private final RewardQueueService rewardQueueService;
    private final RewardHistoryRepository rewardHistoryRepository;

    public RewardResponseDto getDetail(String userId, Long rewardNo) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        Reward reward = rewardRepository.findById(rewardNo)
                .orElseThrow(() -> new RewardNotFoundException(String.valueOf(rewardNo)));
        Optional<RewardPublish> rewardPublish = rewardHistoryRepository.findTopByUserAndRewardOrderById(user, reward);
        Point point = rewardPublish.map((RewardPublish::getPoint)).orElse(new Point());
        Long rank = rewardQueueService.getOrder(reward, user);
        return RewardResponseDto
                .builder()
                .userId(userId)
                .rank(rank)
                .rewardNo(rewardNo)
                .point(point.getAmount())
                .build();
    }

    public List<RewardResponseDto> getDetailsBy(Long rewardNo, LocalDate localDate) {
        Reward reward = rewardRepository.findById(rewardNo)
                .orElseThrow(() -> new RewardNotFoundException(String.valueOf(rewardNo)));
        List<RewardPublish> publishes = rewardHistoryRepository.findAllByRewardAndRegisterDateOrderByRegisterDateDesc(reward, LocalDate.now());
        return RewardResponseDto.from(publishes);
    }
}
