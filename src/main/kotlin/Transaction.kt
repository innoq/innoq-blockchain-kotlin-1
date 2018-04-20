import java.util.*

data class Transaction(
        val id: String = UUID.randomUUID().toString(),
        val timestamp: Long = System.currentTimeMillis(),
        val payload: String
)

object TransactionPool {
    private val pool: ArrayDeque<Transaction> = ArrayDeque()
    fun add(trx: Transaction) = pool.add(trx)

    fun get() = pool.take(5)
}