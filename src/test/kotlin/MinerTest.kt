import com.google.gson.Gson
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class MinerTest : BaseTest() {

    @Test
    fun `should mine new block on genesis`() {
        // given
        val genesisBlockString = "{\"index\":1,\"timestamp\":0,\"proof\":1917336,\"transactions\":[{\"id\":\"b3c973e2-db05-4eb5-9668-3e81c7389a6d\",\"timestamp\":0,\"payload\":\"I am Heribert Innoq\"}],\"previousBlockHash\":\"0\"}"
        val genesisBlock = Gson().fromJson<Block>(genesisBlockString, Block::class.java)

        // when
        val miningResponse = Miner.mine(genesisBlock, 324345345, listOf())

        // then
        assertEquals("""00B2CBCD749814572C35BA9A265420E9A86C97170C2BDE91513F2A0363FEDCEE""", miningResponse.block.hash())
    }

    @Test
    fun `hash should have leading zeros`() {
        // given
        val genesisBlockString = "{\"index\":1,\"timestamp\":0,\"proof\":1917336,\"transactions\":[{\"id\":\"b3c973e2-db05-4eb5-9668-3e81c7389a6d\",\"timestamp\":0,\"payload\":\"I am Heribert Innoq\"}],\"previousBlockHash\":\"0\"}"
        val genesisBlock = Gson().fromJson<Block>(genesisBlockString, Block::class.java)

        // when
        val miningResponse = Miner.mine(genesisBlock)

        // then
        assertTrue(miningResponse.block.hash().startsWith(HASH_PREFIX))
    }
}