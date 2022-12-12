package com.musinsa.demo.domain;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.concurrent.atomic.AtomicInteger;

@Embeddable
@Getter
@Builder
public class Stock {
    @Column(name = "stock")
    private AtomicInteger remains;


    protected Stock() {
    }

    public Stock(AtomicInteger remains) {
        this.remains = remains;
    }

    public boolean end() {
        return remains.intValue() == 0;
    }

    public Stock decrease() {
        this.remains.decrementAndGet();
        return new Stock(new AtomicInteger(remains.get()));
    }
}
