import com.google.gson.Gson
import java.nio.charset.StandardCharsets
import java.security.MessageDigest

data class Block(
        val index: Long,
        val timestamp: Long,
        val proof: Long,
        val transactions: List<Transaction>,
        val previousBlockHash: String
)

val digest = MessageDigest.getInstance("SHA-256")!!

fun Block.hash(): String {
    return digest.digest(Gson().toJson(this).toByteArray(StandardCharsets.UTF_8)).toHex()
}