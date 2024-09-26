package io.hhplus.tdd.point

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

class PointHistoryTest {

    @Test
    fun `History 생성시 amount에 음수가 들어가 IllegalArgumentException이 발생`() {
        // given
        val amount = -1L
        // when
        // then
        assertThatIllegalArgumentException().isThrownBy {
            PointHistory(amount = amount, type = TransactionType.USE)
        }.withMessageContaining("음수는 불가능")
    }

    @Test
    fun `ofCharge 메서드로 생성시 자동으로 type 은 CHARGE 로 생성된다`() {
        // given
        val amount = 1L
        // when
        val result = PointHistory.ofCharge(userId = 0L, amount = amount)
        // then
        assertThat(result).extracting("userId", "amount", "type")
            .contains(0L, 1L, TransactionType.CHARGE)
    }

    @Test
    fun `ofUse 메서드로 생성시 자동으로 type 은 USE 로 생성된다`() {
        // given
        val amount = 1L
        // when
        val result = PointHistory.ofUse(userId = 0L, amount = amount)
        // then
        assertThat(result).extracting("userId", "amount", "type")
            .contains(0L, 1L, TransactionType.USE)
    }
}
