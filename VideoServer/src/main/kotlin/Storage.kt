import java.io.File

private const val STORAGE_PATH = "storage"
private const val VIDEO_PATH_PREFIX = "$STORAGE_PATH/"
private const val SUBTITLES_PATH_PREFIX = "$STORAGE_PATH/"

object Storage {
    /**
     * Creates the storage path if it doesn't exist.
     */
    fun init() {
        val storageDir = File(STORAGE_PATH)
        if (!storageDir.exists()) {
            storageDir.mkdirs()
            val created = storageDir.exists()
            if (!created) {
                println("Failed to create storage directory at $STORAGE_PATH")
                return
            }
            println("Storage directory created at $STORAGE_PATH")
        }
    }

    /**
     * Lists all files in the storage directory.
     * @return A list of file names in the storage directory.
     */
    fun listFiles(): List<String> {
        val storageDir = File(STORAGE_PATH)
        return storageDir.list()?.toList() ?: emptyList()
    }

    /**
     * Returns the video file if it exists, otherwise returns null.
     * @param videoName The name of the video file.
     * @return The video file or null if it doesn't exist.
     */
    fun getVideoFile(videoName: String): File? {
        val videoFile = File(VIDEO_PATH_PREFIX + videoName)
        return if (videoFile.exists()) videoFile else null
    }

    /**
     * If the subtitles file exists, load it and convert it to VTT format, and return it, otherwise return null.
     * @param subtitlesName The name of the subtitles file.
     * @return The subtitles file or null if it doesn't exist.
     */
    fun getSubtitlesFile(subtitlesName: String): File? {
        val subtitlesFile = File(SUBTITLES_PATH_PREFIX + subtitlesName)
        if (!subtitlesFile.exists()) return null
        return loadSrt(SUBTITLES_PATH_PREFIX + subtitlesName)
    }

    /**
     * Load a srt file, convert it to vtt format and save it.
     * @param path The path to the srt file.
     * @return The filepath of the vtt file or null if the file could not be loaded.
     */
    private fun loadSrt(path: String): File? {
        val srtFile = File(path)
        if (!srtFile.exists()) return null // Return null if the file does not exist

        val srt = srtFile.readText(charset) // Read the SRT subtitles
        val vtt = srtSubsToVtt(srt) // Convert the SRT subtitles to VTT format
        val vttFile = File(srtFile.parent, srtFile.nameWithoutExtension + ".vtt") // Create a new file with the same name but with the VTT extension
        vttFile.writeText(vtt, charset) // Write the VTT subtitles to the new file
        return vttFile // Return the VTT file
    }
}
