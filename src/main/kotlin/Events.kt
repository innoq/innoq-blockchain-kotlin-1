import java.util.concurrent.atomic.AtomicLong

abstract class Event(val event: String)

data class NewBlockEvent(val id: Long, val data: Block) : Event("new_block")
data class NewTransactionEvent(val id: Long, val data: Transaction) : Event("new_transaction")
data class NewNodeEvent(val id: Long, val data: Node) : Event("new_node")

object Events {
    val eventCounter = AtomicLong()
    val events = mutableListOf<Event>()

    fun get() = events.toList()

    fun add(block: Block) {
        events.add(NewBlockEvent(eventCounter.incrementAndGet(), block))
    }

    fun add(tx: Transaction) {
        events.add(NewTransactionEvent(eventCounter.incrementAndGet(), tx))
    }

    fun add(node: Node) {
        events.add(NewNodeEvent(eventCounter.incrementAndGet(), node))
    }

    fun clear() {
        events.clear()
    }
}

