package com.musinsa.demo.service;

import com.musinsa.demo.common.enums.Status;
import com.musinsa.demo.common.exception.RewardNotFoundException;
import com.musinsa.demo.common.exception.UserNotFoundException;
import com.musinsa.demo.domain.*;
import com.musinsa.demo.repository.RewardHistoryRepository;
import com.musinsa.demo.repository.RewardPublishRepository;
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
    private final RewardPublishRepository rewardPublishRepository;

    private final RewardHistoryRepository rewardHistoryRepository;

    private final UserRepository userRepository;
    private final PointCalculationService pointCalculationService;
    private final RewardQueueService rewardQueueService;

    @Override
    public void register(String userId, Long rewardPublishNo) {
        User user = userRepository.findById(userId)
                .orElseGet(() -> create(userId));
        RewardPublish rewardPublish = rewardPublishRepository.findByRewardPublishNoAndStatus(rewardPublishNo, Status.OPEN)
                .orElseThrow(() -> new RewardNotFoundException(String.valueOf(rewardPublishNo)));
        long now = System.currentTimeMillis();
        rewardPublish.checkDuplication(user);
        rewardQueueService.addQueue(rewardPublish, user, now);
    }

    @Override
    public void publish(String userId, Long rewardPublishNo) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        RewardPublish rewardPublish = rewardPublishRepository.findById(rewardPublishNo)
                .orElseThrow(() -> new RewardNotFoundException(String.valueOf(rewardPublishNo)));
        if (!validStocks(rewardPublish)) return;
        Point point = pointCalculationService.calculatePointAmount(rewardPublish, user);
        publish(rewardPublish, user, point);
        rewardPublishRepository.save(rewardPublish);
        log.info("user reward published from queue - [no={}, userNo ={}, remains={}, timeStamp = {} ]", rewardPublish.getRewardPublishNo(), user.getUserNo(), rewardPublish.getStock().getRemains(), System.currentTimeMillis());
    }

    private void publish(RewardPublish rewardPublish, User user, Point point) {
        RewardHistory rewardHistory = new RewardHistory(user, point, rewardPublish);
        int count = rewardHistoryRepository.countAllByRewardPublish(rewardPublish);
        if(rewardPublish.getStock().getRemains().intValue() == count) {
            rewardPublish.closeRewardPublish();
            rewardPublishRepository.save(rewardPublish);
            return;
        }
        rewardHistoryRepository.save(rewardHistory);
    }


    private boolean validStocks(RewardPublish rewardPublish) {
        final Stock stock = rewardPublish.getStock();
        return stock != null && !stock.end();
    }

    private User create(String userId) {
        User newUser = User.builder()
                .id(userId).build();
        return userRepository.save(newUser);
    }
}
