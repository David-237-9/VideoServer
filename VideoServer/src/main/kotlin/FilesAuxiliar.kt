import java.nio.charset.Charset

val charset: Charset = Charset.forName("windows-1252") // Charset used to read and write the subtitles

/**
 * Convert SRT subtitles to VTT format.
 * @param srt The SRT subtitles.
 * @return The subtitles in VTT format.
 */
fun srtSubsToVtt(srt: String): String {
    val vttBuilder = StringBuilder()

    vttBuilder.append("WEBVTT\n\n") // Add the VTT header

    srt.lines().forEach { line ->// Process each line
        val convertedLine = line.replace(Regex("(\\d{2}:\\d{2}:\\d{2}),(\\d{3})"), "$1.$2") // Replace timestamp commas with periods
        vttBuilder.append(convertedLine).append("\n") // Append the line to the VTT subtitles
    }

    return vttBuilder.toString()
}

/**
 * Get the video and subtitles filenames from the user.
 * @param filenames The list of available filenames.
 * @return A pair containing the video filename and the subtitles filename (or null if not selected), or null if the input is invalid.
 */
fun getVideoAndSubtitlesFromUser(filenames: List<String>): Pair<String, String?>? {
    val compatibleVideos = filenames.filter { isCompatibleVideoFile(it) }
    if (compatibleVideos.isEmpty()) return null
    println("Choose the video file name by its number:")
    filenamesPrinter(compatibleVideos)
    val inputV = readln().toIntOrNull()
    if (inputV == null || inputV !in 1.. compatibleVideos.size) return null
    val videoName = compatibleVideos[inputV - 1]
    println("Video selected: $videoName")

    val compatibleSubtitles = filenames.filter { isCompatibleSubtitlesFile(it) }
    if (compatibleSubtitles.isEmpty()) return Pair(videoName, null)
    println("Choose the subtitles file name by its number (or press Enter to skip):")
    filenamesPrinter(compatibleSubtitles)
    val inputS = readln().toIntOrNull()
    val subtitlesName = if (inputS == null || inputS !in 1.. compatibleSubtitles.size) null
        else compatibleSubtitles[inputS - 1]
    if (subtitlesName == null) println("No subtitles selected")
    else println("Subtitles selected: $subtitlesName")

    return Pair(videoName, subtitlesName)
}

/**
 * Check if the given filename is a compatible video file.
 */
private fun isCompatibleVideoFile(filename: String): Boolean =
    filename.endsWith(".mp4") || filename.endsWith(".mkv")

/**
 * Check if the given filename is a compatible subtitles file.
 */
private fun isCompatibleSubtitlesFile(subtitle: String): Boolean =
    subtitle.endsWith(".srt")

/**
 * Print the list of filenames in a formatted way.
 * @param filenames The list of filenames.
 * @return The formatted string of filenames.
 */
private fun filenamesPrinter(filenames: List<String>) {
    if (filenames.isEmpty()) return

    var counter = 1
    val builder = StringBuilder()
    filenames.forEach { filename ->
        builder.append("${counter++} - $filename\n")
    }

    val result = builder.toString().removeSuffix("\n")
    println(result)
}
