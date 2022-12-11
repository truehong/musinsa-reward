package com.musinsa.demo.common.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class RewardNotFoundException extends ServiceNotFoundException {
    public RewardNotFoundException(String rewardId) {
        super(ServiceNotFoundErrorType.REWARD);
        log.info("reward not found id - {}", rewardId);
    }
}
