package storage

import dev.david2379.videoserver.Storage
import java.io.File
import kotlin.test.Test

private const val STORAGE_PATH = "storage"
private const val TEMP_STORAGE_PATH = "temp_storage"

class StorageTests {
    @Test
    fun initTest() {
        // Ensure a clean state before test
        val storageDir = File(STORAGE_PATH)
        val tempStorageDir = File(TEMP_STORAGE_PATH)
        moveAllFilesAndFolders(storageDir, tempStorageDir)
        storageDir.deleteRecursively()

        // Execute
        Storage.init()

        // Assert
        assert(storageDir.exists()) { "Storage directory was not created at $STORAGE_PATH"}

        // Cleanup after test
        storageDir.deleteRecursively()
        moveAllFilesAndFolders(tempStorageDir, storageDir)
        tempStorageDir.deleteRecursively()
    }

    fun moveAllFilesAndFolders(sourceDir: File, targetDir: File) {
        if (!targetDir.exists()) targetDir.mkdirs()
        sourceDir.listFiles()?.forEach { file ->
            val target = File(targetDir, file.name)
            file.renameTo(target)
        }
    }
}
