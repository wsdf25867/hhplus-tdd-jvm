package io.hhplus.tdd.point

class FakeUserPointRepository : UserPointRepository {

    private val userPoints = mutableMapOf<Long, UserPoint>()

    override fun findByIdOrNull(id: Long): UserPoint? = userPoints[id]


    override fun save(point: UserPoint): UserPoint {
        userPoints[point.id] = point
        return point
    }
}
