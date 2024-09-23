package io.hhplus.tdd.point

data class UserPoint(
    val id: Long = 0,
    val point: Long = 0,
    val updateMillis: Long = System.currentTimeMillis(),
) {
    fun charge(amount: Long): UserPoint {
        validateAmount(amount)

        return copy(point = point + amount)
    }

    fun use(amount: Long): UserPoint {
        validateAmount(amount)

        val usedPoint = this.point - amount
        if (usedPoint < 0) {
            throw IllegalStateException("포인트가 부족합니다.")
        }

        return copy(point = usedPoint)
    }

    private fun validateAmount(amount: Long) {
        if (amount < 0) {
            throw IllegalArgumentException("음수는 불가능 합니다.")
        }
    }
}
