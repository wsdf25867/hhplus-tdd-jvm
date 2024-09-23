package io.hhplus.tdd.point

class FakePointHistoryRepository : PointHistoryRepository {
    private val pointHistories = mutableMapOf<Long, PointHistory>()

    override fun findAllByUserId(userId: Long): List<PointHistory> =
        pointHistories.values.filter { it.userId == userId }.toList()

    override fun save(pointHistory: PointHistory): PointHistory {
        pointHistories[pointHistory.id] = pointHistory
        return pointHistory
    }

}
