import com.google.gson.Gson
import java.nio.charset.StandardCharsets
import java.security.MessageDigest

class Miner {
    private val digest = MessageDigest.getInstance("SHA-256")!!
    private fun hashBlock(block: Block): String {
        return digest.digest(Gson().toJson(block).toByteArray(StandardCharsets.UTF_8)).toHex()
    }

    fun mine(head: Block): Block {
        val headHash = hashBlock(head)
        val block = Block(head.index + 1, System.currentTimeMillis(), 0, listOf(), headHash)
        do {
            ++block.proof
            val hash = hashBlock(block)
        } while (!hash.startsWith("000"))
        return block
    }
}