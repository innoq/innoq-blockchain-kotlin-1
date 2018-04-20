import io.ktor.application.*
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.google.gson.*
import io.ktor.request.receive
import java.security.MessageDigest
import java.util.*

val genesisBlockString = "{\"index\":1,\"timestamp\":0,\"proof\":1917336,\"transactions\":[{\"id\":\"b3c973e2-db05-4eb5-9668-3e81c7389a6d\",\"timestamp\":0,\"payload\":\"I am Heribert Innoq\"}],\"previousBlockHash\":\"0\"}"

val genesisBlock = Gson().fromJson<Block>(genesisBlockString, Block::class.java)

val chain:Chain = Chain(mutableListOf(genesisBlock))

val uuid = UUID.randomUUID().toString()

val digest = MessageDigest.getInstance("SHA-256")!!


data class MiningResponse(val message: String, val block: Block)


fun Application.blockChain(){
    install(ContentNegotiation) {
        gson {
            //setPrettyPrinting()
        }
    }
    routing {
        get("/") {
            call.respond(Node(uuid, chain.blockHeight))
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
            val txRequest = call.receive<TxRequest>()
            println(txRequest)
            val newTransaction = Transaction(payload = txRequest.payload)
            TransactionPool.add(newTransaction)
            call.respond(newTransaction)
        }
    }
}

data class TxRequest(val payload: String)

fun main(args: Array<String>) {
    val server = embeddedServer(Netty, port = 8089, module = Application::blockChain)
    server.start(wait = true)
}
