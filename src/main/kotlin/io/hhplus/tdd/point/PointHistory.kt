package io.hhplus.tdd.point

data class PointHistory(
    val id: Long = 0L,
    val userId: Long = 0L,
    val type: TransactionType,
    val amount: Long = 0L,
    val timeMillis: Long = System.currentTimeMillis(),
) {
    init {
        require(amount >= 0) { "음수는 불가능합니다." }
    }
    companion object {
        fun ofCharge(userId: Long, amount: Long) =
            PointHistory(userId = userId, amount = amount, type = TransactionType.CHARGE)

        fun ofUse(userId: Long, amount: Long) =
            PointHistory(userId = userId, amount = amount, type = TransactionType.USE)
    }
}

/**
 * 포인트 트랜잭션 종류
 * - CHARGE : 충전
 * - USE : 사용
 */
enum class TransactionType {
    CHARGE, USE
}
