package io.hhplus.tdd.point

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PointHistoryRepositoryTest {
    private val pointHistoryRepository: PointHistoryRepository = FakePointHistoryRepository()

    @Test
    fun `PointHistory를 저장할 수 있다`() {
        //given
        val pointHistory = PointHistory(type = TransactionType.USE)

        //when
        val saved = pointHistoryRepository.save(pointHistory)

        //then
        assertThat(saved.type).isEqualTo(TransactionType.USE)
    }

    @Test
    fun `userId로 PointHistory를 조회시 해당 id에 PointHistory가 없다면 빈 리스트를 반환한다`() {
        //given
        //when
        val histories = pointHistoryRepository.findAllByUserId(1L)

        //then
        assertThat(histories).isEmpty()
    }

    @Test
    fun `id로 PointHistory를 조회시 있다면 PointHistory를 반환한다`() {
        //given
        val pointHistory = PointHistory(id = 1L, userId = 1L, type = TransactionType.USE)
        pointHistoryRepository.save(pointHistory)

        //when
        val histories = pointHistoryRepository.findAllByUserId(1L)

        //then
        assertThat(histories).hasSize(1)
        assertThat(histories[0]).extracting("id", "userId", "type")
            .contains(1L, 1L, TransactionType.USE)
    }
}
