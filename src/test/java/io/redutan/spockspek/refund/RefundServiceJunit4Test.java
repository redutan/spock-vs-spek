package io.redutan.spockspek.refund;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * 가격 별 취소수수료
 * 0 ~ 9999 : 0%
 * 10000 ~ 49999 : 10%
 * 50000 ~ : 20%
 */
@SuppressWarnings("WeakerAccess")
@RunWith(Parameterized.class)   // 파라메터화된 테스트를 위한 선언 - 즉 data 별로 하나의 클래스만 가능 (메서드 별로 불가능)
public class RefundServiceJunit4Test {
    // @Parameter로 주입 시 public 으로 선언되어야 한다.
    @Parameter()   // data() 항 항목의 첫번째 인자
    public long amount;
    @Parameter(1)   // data() 항 항목의 두번째 인자
    public long refundFee;
    Order order;
    RefundService refundService;

    // 파라미터들 제공 메소드 : static 이면서 Collection 을 반환해야한다. 경계영역이 잘 설정되어야함.
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {9999L, 0L},
                {10000L, 1000L},
                {49999L, 4999L},
                {50000L, 10000L}
        });
    }

    @Before
    public void setUp() {
        refundService = new RefundService();
        order = new Order(amount);
    }

    @Test
    public void testGetRefundFee() {
        assertThat(refundService.getRefundFee(order), is(this.refundFee));
    }
}

