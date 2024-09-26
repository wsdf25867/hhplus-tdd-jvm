package io.hhplus.tdd.point

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

class UserPointTest {
    @Test
    fun `포인트는 음수로 생성할 수 없다`() {
        // given // when // then
        assertThatIllegalArgumentException().isThrownBy {
            UserPoint(point = -1L)
        }
    }

    @Test
    fun `포인트 충전시 음수를 입력하면 예외가 발생한다`() {
        // given
        val userPoint = UserPoint()

        // when // then
        assertThatIllegalArgumentException().isThrownBy {
            userPoint.charge(-1L)
        }.withMessage("음수는 불가능 합니다.")
    }

    @Test
    fun `포인트를 충전할 수 있다`() {
        // given
        val userPoint = UserPoint()

        // when
        val chargedUserPoint = userPoint.charge(1L)

        // then
        assertThat(chargedUserPoint.point).isEqualTo(1L)
    }

    @Test
    fun `포인트 사용시 음수를 임렵하면 예외가 발생한다`() {
        // given
        val userPoint = UserPoint()

        // when // then
        assertThatIllegalArgumentException().isThrownBy {
            userPoint.use(-1L)
        }.withMessage("음수는 불가능 합니다.")
    }

    @Test
    fun `포인트 사용시 잔여 포인트가 부족하면 예외가 발생한다`() {
        // given
        val userPoint = UserPoint(point = 1L)

        // when // then
        assertThatIllegalStateException().isThrownBy {
            userPoint.use(2L)
        }.withMessage("포인트가 부족합니다.")
    }

    @Test
    fun `포인트 사용할 수 있다`() {
        // given
        val userPoint = UserPoint(point = 1L)

        // when
        val usedUserPoint = userPoint.use(1L)

        // then
        assertThat(usedUserPoint.point).isEqualTo(0L)
    }
}
