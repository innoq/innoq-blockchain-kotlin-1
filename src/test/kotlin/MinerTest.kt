import com.google.gson.Gson
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class MinerTest {

    @Test
    fun `should mine new block on genesis`() {
        // given
        val genesisBlockString = "{\"index\":1,\"timestamp\":0,\"proof\":1917336,\"transactions\":[{\"id\":\"b3c973e2-db05-4eb5-9668-3e81c7389a6d\",\"timestamp\":0,\"payload\":\"I am Heribert Innoq\"}],\"previousBlockHash\":\"0\"}"
        val genesisBlock = Gson().fromJson<Block>(genesisBlockString, Block::class.java)

        // when
        val miningResponse = Miner.mine(genesisBlock, 324345345)

        // then
        assertEquals("""00096CB02B4D30119EB874FB79AC77F4DEDD76D5965A6ACBEB2ACBF0C2DE5260""", Miner.hashBlock(miningResponse.block))
    }

    @Test
    fun `hash should have leading zeros`() {
        // given
        val genesisBlockString = "{\"index\":1,\"timestamp\":0,\"proof\":1917336,\"transactions\":[{\"id\":\"b3c973e2-db05-4eb5-9668-3e81c7389a6d\",\"timestamp\":0,\"payload\":\"I am Heribert Innoq\"}],\"previousBlockHash\":\"0\"}"
        val genesisBlock = Gson().fromJson<Block>(genesisBlockString, Block::class.java)

        // when
        val miningResponse = Miner.mine(genesisBlock)

        // then
        assertTrue(Miner.hashBlock(miningResponse.block).startsWith("000"))
    }
}