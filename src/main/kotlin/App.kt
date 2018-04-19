import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

val genesisBlockString = "{\"index\": 1,\"timestamp\": 0,\"proof\": 955977,\"transactions\":" +
        " [{\"id\": \"b3c973e2-db05-4eb5-9668-3e81c7389a6d\",\"timestamp\": 0,\"payload\":" +
        " \"I am Heribert Innoq\"  }],\"previousBlockHash\": \"0\"}";

fun main(args: Array<String>) {
    val server = embeddedServer(Netty, port = 8080) {
        routing {
            get("/") {
                call.respondText("Hello World!", ContentType.Text.Plain)
            }
            get("/demo") {
                call.respondText("HELLO WORLD!")
            }
        }
    }
    server.start(wait = true)
}
