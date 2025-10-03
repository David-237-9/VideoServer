package storage

import java.io.File
import kotlin.test.Test

private const val STORAGE_PATH = "storage"

class StorageTests {
    @Test
    fun initTest() {
        // Ensure a clean state before test
        val storageDir = File(STORAGE_PATH)
        storageDir.deleteRecursively()

        // Execute
        Storage.init()

        // Assert
        assert(storageDir.exists()) { "Storage directory was not created at $STORAGE_PATH"}

        // Cleanup after test
        storageDir.deleteRecursively()
    }
}
