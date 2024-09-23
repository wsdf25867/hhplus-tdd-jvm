package io.hhplus.tdd.point

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

class UserPointRepositoryTest {
    private val userPointRepository: UserPointRepository = FakeUserPointRepository()

    @Test
    fun `UserPoint를 저장할 수 있다`() {
        //given
        val updateMillis = System.currentTimeMillis()
        val userPoint = UserPoint(1L, 1000L, updateMillis)

        //when
        val saved = userPointRepository.save(userPoint)

        //then
        assertThat(saved).extracting("id", "point", "updateMillis")
            .contains(1L, 1000L, updateMillis)
    }

    @Test
    fun `id로 UserPoint를 조회시 해당 id에 UserPoint가 없다면 Null 이 나올 수 있다`() {
        //given
        //when
        val found = userPointRepository.findByIdOrNull(1L)

        //then
        assertThat(found).isNull()
    }

    @Test
    fun `id로 UserPoint를 조회시 있다면 UserPoint를 반환한다`() {
        //given
        val updateMillis = System.currentTimeMillis()
        val userPoint = UserPoint(1L, 1000L, updateMillis)
        userPointRepository.save(userPoint)

        //when
        val found = userPointRepository.findByIdOrNull(1L)!!

        //then
        assertThat(found).extracting("id", "point", "updateMillis")
            .contains(1L, 1000L, updateMillis)
    }
}
