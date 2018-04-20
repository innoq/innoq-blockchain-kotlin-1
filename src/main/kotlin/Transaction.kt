import java.util.*

data class Transaction(
        val id: String = UUID.randomUUID().toString(),
        val timestamp: Long = System.currentTimeMillis(),
        val payload: String
)

object TransactionPool {
    private val map = hashMapOf<String, Transaction>()
    private val pool: ArrayDeque<Transaction> = ArrayDeque()
    fun add(tx: Transaction) {
        pool.add(tx)
        map.put(tx.id, tx)
    }
    
    fun get(id: String) = map.get(id)
    
    fun takeForMining() = pool.take(5)
}