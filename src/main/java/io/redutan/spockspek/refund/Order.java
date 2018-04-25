package io.redutan.spockspek.refund;

public class Order {
    private long amount;

    public Order(long amount) {
        this.amount = amount;
    }

    public long getAmount() {
        return this.amount;
    }
}
