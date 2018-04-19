import io.ktor.application.*
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

val genesisBlockString = "{\"index\":1,\"timestamp\":0,\"proof\":1917336,\"transactions\":[{\"id\":\"b3c973e2-db05-4eb5-9668-3e81c7389a6d\",\"timestamp\":0,\"payload\":\"I am Heribert Innoq\"}],\"previousBlockHash\":\"0\"}";


data class MiningResponse(val message: String, val block: Block)

fun main(args: Array<String>) {
    val server = embeddedServer(Netty, port = 8080) {
        install(ContentNegotiation) {
            gson {
                //setPrettyPrinting()
            }
        }
        routing {
            get("/") {
                call.respond(Node("", 1))
            }
            get("/blocks") {
                call.respond(Chain(listOf(Block(1, 0, 0, listOf(), "0"))))
            }
            get("/mine") {
                call.respond(MiningResponse("Mined ...", Block(1, 0, 0, listOf(), "0")))
            }
        }
    }
    server.start(wait = true)
}
