package com.musinsa.demo.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Point {
    @Column(name = "point")
    private int amount;

    public void initialize(){
        this.amount = 0;
    }

    public Point add(int amount){
        this.amount += amount;
        return new Point(amount);
    }
}
