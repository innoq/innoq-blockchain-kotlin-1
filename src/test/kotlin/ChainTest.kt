import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class ChainTest : BaseTest() {

    @Test
    fun `has transactions confirmed`() {

        val tx = Transaction(payload = "test payload")
        val miningResponse = Miner.mine(genesisBlock, 324345345, listOf(tx))
        Chain.add(miningResponse.block)

        // then
        assertTrue(Chain.containsTransaction(tx.id))
    }


    @Test
    fun `synchronize with remote blockchain`() {

        // our chain of size 2
        Chain.add(Miner.mine(genesisBlock).block)

        // remote chain of size 4
        val remoteChain = mutableListOf<Block>(genesisBlock)
        for (i in 1..3)
            remoteChain.add(Miner.mine(remoteChain.last()).block)


        Chain.synchronize(ChainResponse(remoteChain))

        assertEquals(4, Chain.size())
    }

}