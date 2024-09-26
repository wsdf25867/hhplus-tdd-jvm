package io.hhplus.tdd.point

data class PointHistoryResponse(
    val id: Long,
    val userId: Long,
    val type: TransactionType,
    val amount: Long,
) {
    companion object {
        fun from(history: PointHistory): PointHistoryResponse =
            PointHistoryResponse(history.id, history.userId, history.type, history.amount)
    }
}
