import com.google.gson.Gson

val genesisBlockString = "{\"index\":1,\"timestamp\":0,\"proof\":1917336,\"transactions\":[{\"id\":\"b3c973e2-db05-4eb5-9668-3e81c7389a6d\",\"timestamp\":0,\"payload\":\"I am Heribert Innoq\"}],\"previousBlockHash\":\"0\"}"

val genesisBlock = Gson().fromJson<Block>(genesisBlockString, Block::class.java)


var HASH_PREFIX = "000000"

object Chain {
    private val blocks = mutableListOf<Block>(genesisBlock)

    fun getBlocks() = blocks.toList()

    fun size() = blocks.size.toLong()

    fun head() = blocks.last()

    @Synchronized fun add(block: Block) {
        assert(block.index == head().index + 1, { "There exists already another block with the given index" })
        assert(block.previousBlockHash.equals(head().hash()), { "previousBlockHash is not correct" })
        assert(block.hash().startsWith(HASH_PREFIX), {"block's hash doesn't start with " + HASH_PREFIX})

        blocks.add(block)
        Events.add(block)
    }

    fun containsTransaction(transactionId: String) = blocks.stream()
            .flatMap { block -> block.transactions.stream() }
            .anyMatch {transaction -> transaction.id.equals(transactionId)}

    @Synchronized fun clear() {
        blocks.clear()
        blocks.add(genesisBlock)
    }

    @Synchronized fun synchronize(chain: ChainResponse) {
        if (chain.blockHeight <= size() + 1)
            return

        assert(chain.blocks[0].equals(genesisBlock))

        var lastHash = "0"
        for (block in chain.blocks) {
            assert(block.previousBlockHash.equals(lastHash))
            lastHash = block.hash()
        }

        blocks.clear()
        blocks.addAll(chain.blocks)
    }

}