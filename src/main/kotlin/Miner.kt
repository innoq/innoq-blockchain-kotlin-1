import com.google.gson.Gson
import java.nio.charset.StandardCharsets
import java.security.MessageDigest

object Miner {
    private val digest = MessageDigest.getInstance("SHA-256")!!

    fun hashBlock(block: Block): String {
        return digest.digest(Gson().toJson(block).toByteArray(StandardCharsets.UTF_8)).toHex()
    }

    fun mine(head: Block): MiningResponse {
        return mine(head, System.currentTimeMillis())
    }

    fun mine(head: Block, timestamp: Long): MiningResponse {
        var hashes : Long =0
        val start = System.currentTimeMillis()
        val headHash = hashBlock(head)
        val block = Block(head.index + 1, timestamp, 0, listOf(), headHash)
        do {
            ++block.proof
            val hash = hashBlock(block)
            hashes++
        } while (!hash.startsWith("000"))
        val stop =System.currentTimeMillis()
        val seconds = (stop.toDouble()-start.toDouble())/1000.toDouble()
        val hashesPerSecond= hashes/seconds
        return MiningResponse("""Mined a new block in $seconds s. Hashing power: $hashesPerSecond hashes/s.""", block)
    }


}