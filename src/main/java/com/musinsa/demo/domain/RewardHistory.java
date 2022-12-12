package com.musinsa.demo.domain;

import com.musinsa.demo.common.AbstractBaseRegisterDate;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Embedded;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;
import javax.persistence.GenerationType;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RewardHistory extends AbstractBaseRegisterDate {

    @Id
    @Column(name = "history_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reward_publish_no")
    private RewardPublish rewardPublish;
    @OneToOne
    @JoinColumn(name = "user_no")
    private User user;

    @Embedded
    private Point point;

    public RewardHistory(User user, Point point, RewardPublish rewardPublish) {
        this.rewardPublish = rewardPublish;
        this.user = user;
        this.point = point;
    }

    public void setRewardPublish(RewardPublish rewardPublish) {
        this.rewardPublish = rewardPublish;
    }
}
