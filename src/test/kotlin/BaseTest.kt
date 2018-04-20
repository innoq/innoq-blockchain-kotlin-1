import org.junit.jupiter.api.BeforeEach

open class BaseTest {

    @BeforeEach
    fun clearAndSetup() {
        Chain.clear()
        Events.clear()
        HASH_PREFIX = "00"
    }
}