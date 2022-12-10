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
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RewardPublish extends AbstractBaseRegisterDate {
    @Id
    @Column(name = "history_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_no")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reward_no")
    private Reward reward;

    @Embedded
    private Point point;

    public RewardPublish(){}
    public RewardPublish(User user, Point point) {
        this.user = user;
        this.point = point;
    }

    public void setReward(Reward reward) {
        this.reward = reward;
    }
}
