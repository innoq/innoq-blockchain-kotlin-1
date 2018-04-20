import java.util.*
import java.util.concurrent.SynchronousQueue

data class Transaction(
        val id: String = UUID.randomUUID().toString(),
        val timestamp: Long,
        val payload: String
)

object TransactionPool {
    private val pool: Queue<Transaction> = SynchronousQueue()
    fun add(trx: Transaction): Unit {
        pool.add(trx)
    }

    fun get() = pool.take(5)
}