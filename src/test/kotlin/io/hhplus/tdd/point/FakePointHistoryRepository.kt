package io.hhplus.tdd.point

class FakePointHistoryRepository : PointHistoryRepository {
    private val pointHistories = mutableMapOf<Long, PointHistory>()

    override fun findByIdOrNull(id: Long): PointHistory? = pointHistories[id]

    override fun save(pointHistory: PointHistory): PointHistory {
        pointHistories[pointHistory.id] = pointHistory
        return pointHistory
    }

}
