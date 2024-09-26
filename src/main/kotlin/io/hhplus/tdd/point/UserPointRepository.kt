package io.hhplus.tdd.point

interface UserPointRepository {

    fun findByIdOrNull(id: Long): UserPoint?

    fun save(point: UserPoint): UserPoint
}
