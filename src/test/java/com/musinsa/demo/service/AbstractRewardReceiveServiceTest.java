package com.musinsa.demo.service;

import com.musinsa.demo.domain.Reward;
import com.musinsa.demo.domain.Stock;
import com.musinsa.demo.domain.User;
import com.musinsa.demo.repository.RewardRepository;
import com.musinsa.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
public abstract class AbstractRewardReceiveServiceTest {
    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected RewardRepository rewardRepository;

    protected User 유저_생성(String id) {
        User user = User.builder()
                .id(id).build();
        return userRepository.save(user);
    }

    protected List<User> 다중_유저_생성(Integer userCount, String idPattern) {

        List<User> users = IntStream.range(1, userCount + 1)
                .mapToObj(value ->
                        User.builder()
                                .id(idPattern + value).build())
                .collect(Collectors.toList());

        return userRepository.saveAll(users);
    }

    protected Reward 보상_생성() {
        Stock stock = Stock.builder()
                .limit(10)
                .remains(10)
                .build();
        Reward reward = Reward
                .builder()
                .stock(stock)
                .build();
        return rewardRepository.save(reward);
    }

}
