import com.google.gson.Gson
import java.nio.charset.StandardCharsets

data class Transaction(
        val id: String,
        val timestamp: Long,
        val payload: String
)

data class Block(
        val index: Long,
        var timestamp: Long,
        var proof: Long,
        val transactions: List<Transaction>,
        val previousBlockHash: String
)

data class Chain(
        val blocks: MutableList<Block>
) {
    val blockHeight = blocks.size.toLong()
}

fun Block.hash(): String {
    return digest.digest(Gson().toJson(this).toByteArray(StandardCharsets.UTF_8)).toHex()
}

