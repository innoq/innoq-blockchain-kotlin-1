import com.google.gson.Gson
import java.nio.charset.StandardCharsets
import java.security.MessageDigest

object Miner {
    private val digest = MessageDigest.getInstance("SHA-256")!!

    fun hashBlock(block: Block): String {
        return digest.digest(Gson().toJson(block).toByteArray(StandardCharsets.UTF_8)).toHex()
    }

    fun mine(head: Block): Block {
        return mine(head, System.currentTimeMillis())
    }

    fun mine(head: Block, timestamp: Long): Block {
        val headHash = hashBlock(head)
        val block = Block(head.index + 1, timestamp, 0, listOf(), headHash)
        do {
            ++block.proof
            val hash = hashBlock(block)
        } while (!hash.startsWith("000"))
        return block
    }


}