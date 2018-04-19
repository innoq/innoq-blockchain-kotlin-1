data class Transaction(
        val id: String,
        val timestamp: Long,
        val payload: String
)

data class Block(
        val index: Long,
        val timestamp: Long,
        val proof: Long,
        val transactions: List<Transaction>,
        val previousBlockHash: String
)

data class Chain (
    val blocks: List<Block>
) {
    fun blockHeight(): Int {
        return blocks.size
    }
}