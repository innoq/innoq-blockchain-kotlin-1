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


}