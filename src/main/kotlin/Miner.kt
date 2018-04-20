object Miner {

    fun mine(head: Block): MiningResponse {
        return mine(head, System.currentTimeMillis(), TransactionPool.takeForMining())
    }

    fun mine(head: Block, timestamp: Long, transactions: List<Transaction>): MiningResponse {
        var hashes : Long =0
        val start = System.currentTimeMillis()
        val headHash = head.hash()
        var proof = 0L
        while (true) {
            val block = Block(head.index + 1, timestamp, proof++, transactions, headHash)
            val hash = block.hash()
            hashes++
            if (hash.startsWith(HASH_PREFIX)) {
                val stop = System.currentTimeMillis()
                val seconds = (stop.toDouble() - start.toDouble()) / 1000.toDouble()
                val hashesPerSecond = hashes / seconds
                return MiningResponse("""Mined a new block in $seconds s. Hashing power: $hashesPerSecond hashes/s.""", block)
            }
        }
    }

}