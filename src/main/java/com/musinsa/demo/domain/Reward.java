package com.musinsa.demo.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Reward {
    @Id
    @Column(name = "reward_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    @Embedded
    @Column(name = "stock")
    private Stock stock;

    @OneToMany(mappedBy = "reward",  cascade = { PERSIST , MERGE})
    private final List<RewardPublish> histories = new ArrayList<>();

    public Reward(Stock stock){
        this.stock = stock;
    }

    public void publish(RewardPublish rewardPublish) {
        updateStock();
        add(rewardPublish);
    }

    private void add(RewardPublish rewardPublish){
        checkDuplication(rewardPublish.getUser());
        rewardPublish.setReward(this);
        this.histories.add(rewardPublish);
    }

    private void updateStock() {
        if (stock.getRemains() <= 0) {
            throw new RuntimeException(); // todo: exception
        }
        stock = stock.decrease();
    }

    public void checkDuplication(User user) {
        boolean HistoryExists = histories.stream()
                .anyMatch(v -> v.getUser() == user && LocalDate.now().equals(v.getRegisterDate()));
        if (HistoryExists) throw new RuntimeException();
    }
}
