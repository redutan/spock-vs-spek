package io.redutan.spockspek.refund

import spock.lang.Specification
import spock.lang.Unroll

class RefundServiceSpockTest extends Specification {
    def service = new RefundService()

    // data-driven test
    // String template : #name
    @Unroll("GetRefundFee 주문액이 #amount 이면 환불수수료는 #fee 이어야한다.") // 순차적으로 실행
    def getRefundFee() {
        expect:
        service.getRefundFee(new Order(amount)) == fee

        where:
        amount | fee
        9999L  | 0L
        10000L | 1000L
        49999L | 4999L
        50000L | 10000L
        // amount << [9999L, 100000L, 49999L, 50000L] << 연산자는 list인 경우 add
        // fee << [0L, 1000L, 4999L, 10000L]
    }
}
