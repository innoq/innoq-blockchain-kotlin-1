import java.util.stream.LongStream
import java.util.stream.Stream

object Miner {

    fun mine(head: Block): MiningResponse {
        return mine(head, System.currentTimeMillis())
    }

    fun mine(head: Block, timestamp: Long): MiningResponse {
        var hashes : Long =0
        val start = System.currentTimeMillis()
        val headHash = head.hash()

//        val block = Block(head.index + 1, timestamp, 0, listOf(), headHash)
//
//        do {
//            ++block.proof
//            val hash = block.hash()
//            hashes++
//        } while (!hash.startsWith("000"))

        val index = head.index + 1

        val block = Stream.iterate(0L, {i -> hashes=i; i+1})
                .map { Block(index, timestamp, it, listOf(), headHash) }
                .filter{ it.hash().startsWith("00000") }
                .findFirst()
                .get()


        val stop =System.currentTimeMillis()
        val seconds = (stop.toDouble()-start.toDouble())/1000.toDouble()
        val hashesPerSecond= hashes/seconds
        return MiningResponse("""Mined a new block in $seconds s. Hashing power: $hashesPerSecond hashes/s.""", block)
    }

}