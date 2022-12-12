package com.musinsa.demo.repository;

import com.musinsa.demo.domain.Reward;
import com.musinsa.demo.domain.RewardPublish;
import com.musinsa.demo.domain.RewardHistory;
import com.musinsa.demo.domain.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RewardHistoryRepository extends JpaRepository<RewardHistory, Long> {
    List<RewardHistory> findAllByRewardPublishAndRegisterDate(RewardPublish rewardPublish, LocalDate registerDate, Sort sort);
    Optional<RewardHistory> findTopByUserAndRewardPublishOrderByRegisterDate(User user, RewardPublish RewardPublish);
    List<RewardHistory> findAllByUserAndRewardPublishAndRegisterDate(User user, RewardPublish rewardPublish, LocalDate registerDate);

    List<RewardHistory> findAllByRewardPublish(RewardPublish rewardPublish);

    Integer countAllByRewardPublish(RewardPublish rewardPublish);
    List<RewardHistory> findAllByRewardPublishAndRegisterDate(RewardPublish rewardPublish, LocalDate localDate);
}
