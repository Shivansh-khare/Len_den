package com.example.lenden.models;

public class reward {
    int Money;
    boolean isVisited;
    String add;

    public reward(int money, boolean isVisited, String add) {
        Money = money;
        this.isVisited = isVisited;
        this.add = add;
    }

    public int getMoney() {
        return Money;
    }

    public void setMoney(int money) {
        Money = money;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited(boolean visited) {
        isVisited = visited;
    }

    public String getAdd() {
        return add;
    }

    public void setAdd(String add) {
        this.add = add;
    }
}
