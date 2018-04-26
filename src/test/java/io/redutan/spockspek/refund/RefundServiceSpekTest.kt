package io.redutan.spockspek.refund

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import kotlin.test.assertEquals

object RefundServiceSpec : Spek({
    // Value Object + Not Null Fields
    data class RefundTestData(val order: Order, val fee: Long)

    describe("환불 수수료 테스트") {
        val service = RefundService()
        val datas = listOf(
                RefundTestData(Order(9999L), 0L),
                RefundTestData(Order(10000L), 1000L),
                RefundTestData(Order(49999L), 4999L),
                RefundTestData(Order(50000L), 10000L)
        )
        // 메서드 별로 각각 parameterized 테스트 가능
        datas.forEach { data ->
            on("주문액이 ${data.order.amount} 이면") {
                val fee = service.getRefundFee(data.order)

                it("환불수수료는 ${data.fee} 여야 한다.") {
                    assertEquals(data.fee, fee)
                }
            }
        }
    }
})
