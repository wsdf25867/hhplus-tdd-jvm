package io.hhplus.tdd.point

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

class UserPointServiceTest {

    private val userPointRepository: UserPointRepository = FakeUserPointRepository()
    private val pointHistoryRepository: PointHistoryRepository = FakePointHistoryRepository()
    private val sut = UserPointService(userPointRepository, pointHistoryRepository)

    @Test
    fun `포인트 충전시 없는 UserPoint id 라면 예외가 발생한다`() {
        // given
        val userId = 1L
        val amount = 1L

        // when // then
        assertThatIllegalArgumentException().isThrownBy {
            sut.charge(userId, amount)
        }.withMessageContaining("유효하지 않은")
    }

    @Test
    fun `포인트 충전시 포인트가 음수라면 예외가 발생한다`() {
        // given
        userPointRepository.save(UserPoint(id = 1L))
        val userId = 1L
        val amount = -1L

        // when // then
        assertThatIllegalArgumentException().isThrownBy {
            sut.charge(userId, amount)
        }.withMessage("음수는 불가능 합니다.")
    }

    @Test
    fun `포인트를 충전하면 포인트가 증가하고, 포인트 충전 이력이 저장된다`() {
        // given
        userPointRepository.save(UserPoint(id = 1L))
        val userId = 1L
        val amount = 1L

        // when
        val result = sut.charge(userId, amount)

        // then
        val pointHistories = pointHistoryRepository.findAllByUserId(userId)
        assertThat(result.point).isEqualTo(1L)
        assertThat(pointHistories).isNotEmpty
    }

    @Test
    fun `포인트 사용시 없는 UserPoint id 라면 예외가 발생한다`() {
        // given
        val userId = 1L
        val amount = 1L

        // when // then
        assertThatIllegalArgumentException().isThrownBy {
            sut.use(userId, amount)
        }.withMessageContaining("유효하지 않은")
    }

    @Test
    fun `포인트 사용시 포인트가 음수라면 예외가 발생한다`() {
        // given
        userPointRepository.save(UserPoint(id = 1L))
        val userId = 1L
        val amount = -1L

        // when // then
        assertThatIllegalArgumentException().isThrownBy {
            sut.use(userId, amount)
        }.withMessage("음수는 불가능 합니다.")
    }

    @Test
    fun `포인트 사용시 포인트가 부족하면 예외가 발생한다`() {
        // given
        userPointRepository.save(UserPoint(id = 1L))
        val userId = 1L
        val amount = 1L

        // when // then
        assertThatIllegalStateException().isThrownBy {
            sut.use(userId, amount)
        }.withMessage("포인트가 부족합니다.")
    }

    @Test
    fun `포인트를 사용하면 포인트가 감소하고, 포인트 사용 이력이 저장된다`() {
        // given
        userPointRepository.save(UserPoint(id = 1L, point = 1L))
        val userId = 1L
        val amount = 1L

        // when
        val result = sut.use(userId, amount)

        // then
        val pointHistories = pointHistoryRepository.findAllByUserId(userId)
        assertThat(result.point).isEqualTo(0L)
        assertThat(pointHistories[0])
            .extracting("id", "userId", "amount", "type")
            .contains(1L, 1L, 1L, TransactionType.USE)
    }
}
