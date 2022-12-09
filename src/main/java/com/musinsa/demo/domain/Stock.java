package com.musinsa.demo.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Getter
@Builder
@EqualsAndHashCode
public class Stock {
    @Column(name = "stock")
    private int limit;
    private int remains;


    protected Stock() {
    }

    Stock(int limit, int remains) {
        this.limit = limit;
        this.remains = remains;
    }

    public Stock decrease() {
        if(remains <= 0) throw new RuntimeException();
        return new Stock(this.getLimit(), -- remains);
    }
}
