import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.ktor.application.Application
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.StringReader

internal class EndpointTest : BaseTest() {

    @Test
    fun testGetNodeId() = withTestApplication(Application::blockChain) {
        neighbors.clear()
        with(handleRequest(HttpMethod.Get, "/") {
            addHeader(HttpHeaders.Accept, ContentType.Application.Json.toString())
        }) {
            assertEquals(HttpStatusCode.OK, response.status())

            assertEquals("""{"nodeId":"$uuid","currentBlockHeight":1,"neighbors":[],"host":"10.100.110.45"}""", response.content)
        }

    }

    @Test
    fun testGetBlockChain() = withTestApplication(Application::blockChain) {
        with(handleRequest(HttpMethod.Get, "/blocks") {
            addHeader(HttpHeaders.Accept, ContentType.Application.Json.toString())
        }) {
            assertEquals(HttpStatusCode.OK, response.status())

            assertEquals("""{"blockHeight":${Chain.size()},"blocks":[$genesisBlockString]}""", response.content)
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


    @Test
    fun testGetEvents() = withTestApplication(Application::blockChain) {

        TransactionPool.add(Transaction(payload = "test payload"))
        Chain.add(Miner.mine(Chain.head()).block)

        with(handleRequest(HttpMethod.Get, "/events") {
            addHeader(HttpHeaders.Accept, ContentType.Application.Json.toString())
        }) {
            assertEquals(HttpStatusCode.OK, response.status())
            assertTrue(response.content?.contains("new_block") ?: false)
            assertTrue(response.content?.contains("new_transaction") ?: false)
        }
    }

    @Test
    fun testPostNodeRegistration() = withTestApplication(Application::blockChain) {
        with(handleRequest(HttpMethod.Post, "/nodes/register") {
            addHeader(HttpHeaders.Accept, ContentType.Application.Json.toString())
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            body = """{"host": "10.100.110.25"}"""
        }) {
            assertEquals(HttpStatusCode.OK, response.status())
            val node = Gson().fromJson(response.content, RegistrationResponseNode::class.java)!!
            assertEquals("New node added", node.message)
        }
    }

}