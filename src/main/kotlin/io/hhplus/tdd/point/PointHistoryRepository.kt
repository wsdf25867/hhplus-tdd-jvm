package io.hhplus.tdd.point

interface PointHistoryRepository {

    fun findAllByUserId(userId: Long): List<PointHistory>

    fun save(pointHistory: PointHistory): PointHistory
}
