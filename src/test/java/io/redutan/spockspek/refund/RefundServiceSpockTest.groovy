package io.redutan.spockspek.refund

import spock.lang.Specification
import spock.lang.Unroll

class RefundServiceSpockTest extends Specification {
    def service = new RefundService()

    @Unroll
    def "GetRefundFee 주문액이 #amount 이면 환불수수료는 #fee 이어야한다."() {
        expect:
        service.getRefundFee(new Order(amount)) == fee

        where:
        amount | fee
        9999L  | 0L
        10000L | 1000L
        49999L | 4999L
        50000L | 10000L
    }
}
