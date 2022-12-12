package com.musinsa.demo.repository;

import com.musinsa.demo.common.enums.Status;
import com.musinsa.demo.domain.Reward;
import com.musinsa.demo.domain.RewardPublish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RewardPublishRepository extends JpaRepository<RewardPublish, Long> {
    Optional<RewardPublish> findById(Long id);

    Optional<RewardPublish> findByRewardPublishNoAndStatus(Long rewardPublishNo, Status status);

    List<RewardPublish> findAllByStatus(Status status);

    List<RewardPublish> findAllByRewardAndStatus(Reward reward, Status status);
}
