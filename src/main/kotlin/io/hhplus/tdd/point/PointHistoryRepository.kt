package io.hhplus.tdd.point

interface PointHistoryRepository {

    fun findByIdOrNull(id: Long): PointHistory?

    fun save(pointHistory: PointHistory): PointHistory
}
