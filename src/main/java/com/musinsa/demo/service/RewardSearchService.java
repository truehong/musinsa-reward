package com.musinsa.demo.service;

import com.musinsa.demo.common.exception.RewardNotFoundException;
import com.musinsa.demo.common.exception.UserNotFoundException;
import com.musinsa.demo.domain.Point;
import com.musinsa.demo.domain.RewardPublish;
import com.musinsa.demo.domain.RewardHistory;
import com.musinsa.demo.domain.User;
import com.musinsa.demo.repository.RewardHistoryRepository;
import com.musinsa.demo.repository.RewardPublishRepository;
import com.musinsa.demo.repository.UserRepository;
import com.musinsa.demo.dto.response.RewardResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RewardSearchService {
    private final RewardPublishRepository rewardPublishRepository;
    private final UserRepository userRepository;
    private final RewardQueueService rewardQueueService;
    private final RewardHistoryRepository rewardHistoryRepository;

    public RewardResponseDto getDetail(String userId, Long rewardPublishNo) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        RewardPublish rewardPublish = rewardPublishRepository.findById(rewardPublishNo)
                .orElseThrow(() -> new RewardNotFoundException(String.valueOf(rewardPublishNo)));
        Optional<RewardHistory> rewardHistory = rewardHistoryRepository.findTopByUserAndRewardPublishOrderByRegisterDate(user, rewardPublish);
        Point point = rewardHistory.map((RewardHistory::getPoint)).orElse(new Point());
        Long rank = rewardQueueService.getOrder(rewardPublish, user);
        return RewardResponseDto
                .builder()
                .userId(userId)
                .rank(rank)
                .rewardNo(rewardPublish.getRewardPublishNo())
                .point(point.getAmount())
                .build();
    }

    public List<RewardResponseDto> getDetailsBy(Long rewardPublishNo, LocalDate localDate, Sort.Direction direction) {
        RewardPublish rewardPublish = rewardPublishRepository.findById(rewardPublishNo)
                .orElseThrow(() -> new RewardNotFoundException(String.valueOf(rewardPublishNo)));
        Sort sort = Sort.by(direction);
        List<RewardHistory> publishes = rewardHistoryRepository.findAllByRewardPublishAndRegisterDate(rewardPublish,localDate, sort);
        return RewardResponseDto.from(publishes);
    }
}
