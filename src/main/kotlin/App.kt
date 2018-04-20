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

val genesisBlockString = "{\"index\":1,\"timestamp\":0,\"proof\":1917336,\"transactions\":[{\"id\":\"b3c973e2-db05-4eb5-9668-3e81c7389a6d\",\"timestamp\":0,\"payload\":\"I am Heribert Innoq\"}],\"previousBlockHash\":\"0\"}"

val genesisBlock = Gson().fromJson<Block>(genesisBlockString, Block::class.java)

val chain: Chain = Chain(mutableListOf(genesisBlock))

val uuid = UUID.randomUUID().toString()

val digest = MessageDigest.getInstance("SHA-256")!!

val neighbors = mutableListOf<Node>()


data class MiningResponse(val message: String, val block: Block)


fun Application.blockChain() {
    install(ContentNegotiation) {
        gson {
            //setPrettyPrinting()
        }
    }
    install(CallLogging)

    routing {
        get("/") {
            call.respond(Node(uuid, chain.blockHeight, neighbors))
        }
        get("/blocks") {
            call.respond(chain)
        }
        get("/mine") {

            val miningResponse = Miner.mine(chain.blocks.last())
            chain.blocks.add(miningResponse.block)
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
                call.respond(TxResponse(tx.id, tx.timestamp, tx.payload, false))

        }
    }
}

data class TxResponse(val id: String,
                      val timestamp: Long ,
                      val payload: String,
                      val confirmed: Boolean)

fun main(args: Array<String>) {
    findNeighbors()
    val server = embeddedServer(Netty, port = 8333, module = Application::blockChain)
    server.start(wait = true)
}
