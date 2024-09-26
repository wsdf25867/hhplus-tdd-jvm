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
        try {
            val chargedPoint = userPointRepository.findByIdOrNull(userId)
                ?.charge(amount)
                ?: throw IllegalArgumentException("유효하지 않은 userId: $userId")
            val savedPoint = userPointRepository.save(chargedPoint)

            val pointHistory = PointHistory(
                userId = userId,
                amount = amount,
                type = TransactionType.CHARGE
            )
            pointHistoryRepository.save(pointHistory)

            return savedPoint
        } finally {
            lock.unlock()
        }

    }

    fun use(userId: Long, amount: Long): UserPoint {
        lock.lock()
        try {
            val chargedPoint = userPointRepository.findByIdOrNull(userId)
                ?.use(amount)
                ?: throw IllegalArgumentException("유효하지 않은 userId: $userId")
            val savedPoint = userPointRepository.save(chargedPoint)

            val pointHistory = PointHistory(
                userId = userId,
                amount = amount,
                type = TransactionType.USE
            )
            pointHistoryRepository.save(pointHistory)

            return savedPoint
        } finally {
            lock.unlock()
        }

    }

    fun find(userId: Long): UserPoint = userPointRepository.findByIdOrNull(userId)
        ?: throw IllegalArgumentException("유효하지 않은 userId: $userId")

    fun findHistories(userId: Long): List<PointHistory> = pointHistoryRepository.findAllByUserId(userId)
}
