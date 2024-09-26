package io.hhplus.tdd.point

import io.hhplus.tdd.database.PointHistoryTable
import org.springframework.stereotype.Repository

@Repository
class PointHistoryInMemoryRepository(
    private val pointHistoryTable: PointHistoryTable
) : PointHistoryRepository {
    override fun findAllByUserId(userId: Long): List<PointHistory> =
        pointHistoryTable.selectAllByUserId(userId)


    override fun save(pointHistory: PointHistory): PointHistory =
        pointHistoryTable.insert(
            id = pointHistory.id,
            amount = pointHistory.amount,
            transactionType = pointHistory.type,
            updateMillis = pointHistory.timeMillis,
        )
}
