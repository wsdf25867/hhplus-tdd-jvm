package io.hhplus.tdd.point

import org.springframework.stereotype.Service
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

@Service
class UserPointService(
    private val userPointRepository: UserPointRepository,
    private val pointHistoryRepository: PointHistoryRepository,
) {
    private val locks: MutableMap<Long, Lock> = mutableMapOf()

    fun charge(userId: Long, amount: Long): UserPointResponse = withLock(userId) {
        val userPoint = requireNotNull(userPointRepository.findByIdOrNull(userId)) {
            "유효하지 않은 userId: $userId"
        }.charge(amount)

        userPointRepository.save(userPoint)

        val pointHistory = PointHistory.ofCharge(userId = userPoint.id, amount = amount)
        pointHistoryRepository.save(pointHistory)

        userPoint.let { UserPointResponse.from(it) }
    }

    fun use(userId: Long, amount: Long): UserPointResponse = withLock(userId) {
        val userPoint = requireNotNull(userPointRepository.findByIdOrNull(userId)) {
            "유효하지 않은 userId: $userId"
        }.use(amount)

        userPointRepository.save(userPoint)

        val pointHistory = PointHistory.ofUse(userId = userPoint.id, amount = amount)
        pointHistoryRepository.save(pointHistory)

        userPoint.let { UserPointResponse.from(it) }
    }

    fun find(userId: Long): UserPointResponse =
        requireNotNull(userPointRepository.findByIdOrNull(userId)) { "유효하지 않은 userId: $userId" }
            .let { UserPointResponse.from(it) }

    fun findHistories(userId: Long): List<PointHistoryResponse> = pointHistoryRepository.findAllByUserId(userId)
        .map { PointHistoryResponse.from(it) }

    private fun <T> withLock(userId: Long, action: () -> T): T {
        locks.putIfAbsent(userId, ReentrantLock())
        val lock = locks[userId] ?: ReentrantLock()
        lock.lock()
        return try {
            action()
        } finally {
            lock.unlock()
        }
    }
}
