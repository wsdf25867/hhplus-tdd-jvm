package io.hhplus.tdd.point

data class UserPoint(
    val id: Long = 0L,
    val point: Long = 0L,
    val updateMillis: Long = System.currentTimeMillis(),
) {
    init {
        validateAmount(point)
    }

    fun charge(amount: Long): UserPoint {
        validateAmount(amount)

        return copy(point = point + amount, updateMillis = System.currentTimeMillis())
    }

    fun use(amount: Long): UserPoint {
        validateAmount(amount)

        if (point < amount) throw IllegalStateException("포인트가 부족합니다.")

        return copy(point = point - amount, updateMillis = System.currentTimeMillis())
    }

    private fun validateAmount(amount: Long) {
        if (amount < 0) throw IllegalArgumentException("음수는 불가능 합니다.")
    }
}
