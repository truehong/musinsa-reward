package com.musinsa.demo.repository;

import com.musinsa.demo.domain.Reward;
import com.musinsa.demo.domain.RewardPublish;
import com.musinsa.demo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RewardHistoryRepository extends JpaRepository<RewardPublish, Long> {
    Optional<RewardPublish> findTopByUserAndRewardOrderById(User user, Reward reward);
}
