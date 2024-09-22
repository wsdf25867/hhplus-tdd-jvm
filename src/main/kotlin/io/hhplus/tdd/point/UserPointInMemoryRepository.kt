package io.hhplus.tdd.point

import io.hhplus.tdd.database.UserPointTable
import org.springframework.stereotype.Repository

@Repository
class UserPointInMemoryRepository(
    private val userPointTable: UserPointTable
) : UserPointRepository {

    override fun findByIdOrNull(id: Long): UserPoint = userPointTable.selectById(id)

    override fun save(point: UserPoint): UserPoint = userPointTable.insertOrUpdate(point.id, point.point)
}
