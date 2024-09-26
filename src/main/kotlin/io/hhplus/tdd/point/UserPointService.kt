package io.hhplus.tdd.point

import org.springframework.stereotype.Service
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

@Service
class UserPointService(
    private val userPointRepository: UserPointRepository,
    private val pointHistoryRepository: PointHistoryRepository,
) {
    private val lock: Lock = ReentrantLock()

    fun charge(userId: Long, amount: Long): UserPoint {
        lock.lock()
        return try {
            requireNotNull(userPointRepository.findByIdOrNull(userId)) {
                "유효하지 않은 userId: $userId"
            }.charge(amount).let {
                userPointRepository.save(it)
            }.also {
                val pointHistory = PointHistory(
                    userId = it.id,
                    amount = amount,
                    type = TransactionType.CHARGE
                )
                pointHistoryRepository.save(pointHistory)
            }
        } finally {
            lock.unlock()
        }

    }

    fun use(userId: Long, amount: Long): UserPoint {
        lock.lock()
        return try {
            requireNotNull(userPointRepository.findByIdOrNull(userId)) {
                "유효하지 않은 userId: $userId"
            }.use(amount).let {
                userPointRepository.save(it)
            }.also {
                val pointHistory = PointHistory(
                    userId = it.id,
                    amount = amount,
                    type = TransactionType.USE
                )
                pointHistoryRepository.save(pointHistory)
            }
        } finally {
            lock.unlock()
        }

    }

    fun find(userId: Long): UserPoint =
        requireNotNull(userPointRepository.findByIdOrNull(userId)) { "유효하지 않은 userId: $userId" }

    fun findHistories(userId: Long): List<PointHistory> = pointHistoryRepository.findAllByUserId(userId)
}
