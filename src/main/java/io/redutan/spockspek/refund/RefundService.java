package io.redutan.spockspek.refund;

public class RefundService {
    public long getRefundFee(Order order) {
        long amount = order.getAmount();
        if (amount < 10000L)    // 0%
            return 0;
        else if (amount < 50000L)   // 10%
            return amount * 10 / 100;
        else    // 20%
            return amount * 20 / 100;
    }

}
