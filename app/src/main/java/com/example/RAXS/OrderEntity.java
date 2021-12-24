package com.example.RAXS;

public class OrderEntity {

    private String answer;

    private int count;

    public OrderEntity(String answer, int count) {
        this.answer = answer;
        this.count = count;
    }

    public OrderEntity() {
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "OrderEntity{" +
                "answer='" + answer + '\'' +
                ", count=" + count +
                '}';
    }
}
