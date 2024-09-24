package io.hhplus.tdd.point

import org.springframework.stereotype.Service

@Service
class UserPointService(
    private val userPointRepository: UserPointRepository,
    private val pointHistoryRepository: PointHistoryRepository,
) {

    fun charge(userId: Long, amount: Long): UserPoint {
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
    }

    fun use(userId: Long, amount: Long): UserPoint {
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
    }
}
