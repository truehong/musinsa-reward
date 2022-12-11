package com.musinsa.demo.repository;

import com.musinsa.demo.domain.Reward;
import com.musinsa.demo.domain.RewardPublish;
import com.musinsa.demo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RewardHistoryRepository extends JpaRepository<RewardPublish, Long> {
    List<RewardPublish> findAllByRewardAndRegisterDateOrderByRegisterDateDesc(Reward reward, LocalDate localDate);
    Optional<RewardPublish> findTopByUserAndRewardOrderById(User user, Reward reward);
    List<RewardPublish> findAllByRewardAndRegisterDate(Reward reward, LocalDate localDate);
}
