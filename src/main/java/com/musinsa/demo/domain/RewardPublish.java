package com.musinsa.demo.domain;

import com.musinsa.demo.common.AbstractBaseRegisterDate;
import com.musinsa.demo.common.enums.Status;
import com.musinsa.demo.common.exception.ServiceErrorType;
import com.musinsa.demo.common.exception.RewardServiceException;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RewardPublish extends AbstractBaseRegisterDate {
    @Id
    @Column(name = "reward_publish_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rewardPublishNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reward_no")
    private Reward reward;

    @Embedded
    @Column(name = "stock")
    private Stock stock;

    @OneToMany(mappedBy = "rewardPublish")
    private final List<RewardHistory> histories = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Status status;

    public RewardPublish(Stock stock){
        this.stock = stock;
    }

    public void closeRewardPublish(){
        this.status = Status.CLOSED;
    }

    public void checkDuplication(User user) {
        boolean HistoryExists = histories.stream()
                .anyMatch(v -> v.getUser() == user && LocalDate.now().equals(v.getRegisterDate()));
        if (HistoryExists) throw new RewardServiceException(ServiceErrorType.USER_DUPLICATE_REGISTER);
    }
}
