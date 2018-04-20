import com.google.gson.Gson
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.request.get
import kotlinx.coroutines.experimental.async
import java.util.stream.Collectors
import java.util.stream.IntStream

data class Node(
        val nodeId: String,
        val currentBlockHeight: Long,
        val neighbors: MutableList<Node>
)



val client = HttpClient(Apache)

fun findNeighbors(){
    val found= IntStream.range(30, 250).boxed().map { findNeighbor(it) }.collect(Collectors.toList())!!
}

fun findNeighbor(ipSub : Int){

    async {
       val json=client.get<String>("""http://10.100.110.$ipSub:8333/""")
        val node=Gson().fromJson(json, Node::class.java)!!
        println("""found neighbor at http://10.100.110.$ipSub:8333/""")
        neighbors.add(node)
    }

}


