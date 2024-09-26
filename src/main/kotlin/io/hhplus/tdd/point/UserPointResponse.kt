package io.hhplus.tdd.point

data class UserPointResponse(
    val id: Long,
    val point: Long,
) {
    companion object {
        fun from(point: UserPoint): UserPointResponse = UserPointResponse(point.id, point.point)
    }
}
