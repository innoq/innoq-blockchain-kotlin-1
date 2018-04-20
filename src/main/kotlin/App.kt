import com.google.gson.Gson
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import java.security.MessageDigest
import java.util.*


val uuid = UUID.randomUUID().toString()

fun Application.blockChain() {
    install(ContentNegotiation) {
        gson {
            //setPrettyPrinting()
        }
    }
    install(CallLogging)

    routing {
        get("/") {
            call.respond(Node(uuid, Chain.size()))
        }
        get("/blocks") {
            call.respond(ChainResponse(Chain.getBlocks()))
        }
        get("/mine") {

            val miningResponse = Miner.mine(Chain.head())
            Chain.add(miningResponse.block)
            call.respond(miningResponse)
        }

        post("/transactions") {
            val requestTransaction = call.receive<Transaction>()
            val tx = Transaction(payload = requestTransaction.payload)
            TransactionPool.add(tx)
            call.respond(TxResponse(tx.id, tx.timestamp, tx.payload, false))
        }

        get("/transactions/{id}") {
            val id = call.parameters["id"] ?: ""
            val tx = TransactionPool.get(id)
            if (tx == null)
                call.respond(HttpStatusCode.NotFound)
            else
                call.respond(TxResponse(tx.id, tx.timestamp, tx.payload, Chain.containsTransaction(id)))

        }
    }
}

data class TxResponse(val id: String,
                      val timestamp: Long,
                      val payload: String,
                      val confirmed: Boolean)

data class ChainResponse(
        val blocks: List<Block>
) {
    val blockHeight = blocks.size.toLong()
}

data class MiningResponse(val message: String, val block: Block)

fun main(args: Array<String>) {
    val server = embeddedServer(Netty, port = 8333, module = Application::blockChain)
    server.start(wait = true)
}
