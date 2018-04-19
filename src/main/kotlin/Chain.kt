data class Transaction(
        val id: String,
        val timestamp: Long,
        val payload: String
)

data class Block(
        val index: Long,
        var timestamp: Long,
        var proof: Long,
        val transactions: List<Transaction>,
        val previousBlockHash: String
)

data class Chain(
        val blocks: List<Block>
) {
    val blockHeight get() = blocks.size
}