import com.google.gson.Gson
import io.ktor.application.Application
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class EndpointTest {

    @Test
    fun testGetNodeId() = withTestApplication(Application::blockChain) {
        with(handleRequest(HttpMethod.Get, "/") {
            addHeader(HttpHeaders.Accept, ContentType.Application.Json.toString())
        }) {
            assertEquals(HttpStatusCode.OK, response.status())

            assertEquals("""{"nodeId":"$uuid","currentBlockHeight":1}""", response.content)
        }

    }

    @Test
    fun testGetBlockChain() = withTestApplication(Application::blockChain) {
        with(handleRequest(HttpMethod.Get, "/blocks") {
            addHeader(HttpHeaders.Accept, ContentType.Application.Json.toString())
        }) {
            assertEquals(HttpStatusCode.OK, response.status())

            assertEquals("""{"blockHeight":${chain.blockHeight},"blocks":[$genesisBlockString]}""", response.content)
        }

    }

    @Test
    fun testGetMining() = withTestApplication(Application::blockChain) {
        with(handleRequest(HttpMethod.Get, "/mine") {
            addHeader(HttpHeaders.Accept, ContentType.Application.Json.toString())
        }) {
            assertEquals(HttpStatusCode.OK, response.status())
            val miningResponse = Gson().fromJson(response.content, MiningResponse::class.java)!!
            assertEquals(2, miningResponse.block.index)
        }

    }

    @Test
    fun testPostTransaction() = withTestApplication(Application::blockChain) {
        with(handleRequest(HttpMethod.Post, "/transactions") {
            addHeader(HttpHeaders.Accept, ContentType.Application.Json.toString())
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            body = """{"payload": "test payload"}"""
        }) {
            assertEquals(HttpStatusCode.OK, response.status())
            val transaction = Gson().fromJson(response.content, Transaction::class.java)!!
            assertEquals("test payload", transaction.payload)
        }
    }

    @Test
    fun testGetTransaction() = withTestApplication(Application::blockChain) {
        val tx = Transaction(payload = "test payload")
        TransactionPool.add(tx)
        with(handleRequest(HttpMethod.Get, "/transactions/" + tx.id) {
            addHeader(HttpHeaders.Accept, ContentType.Application.Json.toString())
        }) {
            assertEquals(HttpStatusCode.OK, response.status())
            val transaction = Gson().fromJson(response.content, Transaction::class.java)!!
            assertEquals("test payload", transaction.payload)
        }
    }

}