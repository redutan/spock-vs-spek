package io.redutan.spockspek.refund

import com.nhaarman.mockito_kotlin.reset
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.github.benas.randombeans.api.EnhancedRandom.random
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.springframework.security.core.userdetails.UsernameNotFoundException
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

object RefundServiceSpec : Spek({
    data class RefundTestData(val order: Order, val fee: Long)

    describe("환불 수수료 테스트") {
        val service = RefundService()
        val datas = listOf(
                RefundTestData(Order(9999L), 0L),
                RefundTestData(Order(10000L), 1000L),
                RefundTestData(Order(49999L), 4999L),
                RefundTestData(Order(50000L), 10000L)
        )
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
