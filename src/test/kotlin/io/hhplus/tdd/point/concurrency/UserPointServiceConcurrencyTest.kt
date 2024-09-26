package io.hhplus.tdd.point.concurrency

import io.hhplus.tdd.point.FakePointHistoryRepository
import io.hhplus.tdd.point.FakeUserPointRepository
import io.hhplus.tdd.point.UserPoint
import io.hhplus.tdd.point.UserPointService
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

class UserPointServiceConcurrencyTest {

    private val userPointRepository = FakeUserPointRepository()
    private val userPointService: UserPointService = UserPointService(userPointRepository, FakePointHistoryRepository())

    @RepeatedTest(10)
    fun `동시에 100개의 충전 요청이 들어와도 모두 처리한다`() {
        // given
        val numberOfConcurrencyRequests = 100
        val threadPool = Executors.newFixedThreadPool(numberOfConcurrencyRequests)
        val latch = CountDownLatch(numberOfConcurrencyRequests)
        val userPoint = UserPoint(1L)
        userPointRepository.save(userPoint)

        // when
        repeat(numberOfConcurrencyRequests) {
            threadPool.submit {
                try {
                    userPointService.charge(1L, 1)
                } finally {
                    latch.countDown()
                }
            }
        }
        latch.await()

        // then
        val result = checkNotNull(userPointRepository.findByIdOrNull(1L))
        assertThat(result.point).isEqualTo(100)
    }

    @RepeatedTest(10)
    fun `동시에 100개의 사용 요청이 들어와도 모두 처리한다`() {
        // given
        val numberOfConcurrencyRequests = 100
        val threadPool = Executors.newFixedThreadPool(numberOfConcurrencyRequests)
        val latch = CountDownLatch(numberOfConcurrencyRequests)
        val userPoint = UserPoint(1L, 100)
        userPointRepository.save(userPoint)

        // when
        repeat(numberOfConcurrencyRequests) {
            threadPool.submit {
                try {
                    userPointService.use(1L, 1)
                } finally {
                    latch.countDown()
                }
            }
        }
        latch.await()

        // then
        val result = checkNotNull(userPointRepository.findByIdOrNull(1L))
        assertThat(result.point).isEqualTo(0)
    }
}
