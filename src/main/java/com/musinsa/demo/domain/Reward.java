package com.musinsa.demo.domain;

import com.musinsa.demo.common.AbstractBaseRegisterDate;
import com.musinsa.demo.common.enums.Status;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Reward extends AbstractBaseRegisterDate {
    @Id
    @Column(name = "reward_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rewardNo;

    @Column(name = "title")
    private String title;

    @Column(name = "cron_expression")
    private String cronExpression;

    @Column(name = "reward_status")
    private Status rewardStatus;

    @OneToMany(mappedBy = "reward")
    private List<RewardPublish> rewardPublishList = new ArrayList<>();
}
