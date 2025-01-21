import java.io.File
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
 * Load a srt file, convert it to vtt format and save it.
 * @param path The path to the srt file.
 * @return The filepath of the vtt file or null if the file could not be loaded.
 */
fun loadSrt(path: String): File? {
    val srtFile = File(path)
    if (!srtFile.exists()) return null // Return null if the file does not exist

    val srt = srtFile.readText(charset) // Read the SRT subtitles
    val vtt = srtSubsToVtt(srt) // Convert the SRT subtitles to VTT format
    val vttFile = File(srtFile.parent, srtFile.nameWithoutExtension + ".vtt") // Create a new file with the same name but with the VTT extension
    vttFile.writeText(vtt, charset) // Write the VTT subtitles to the new file
    return vttFile // Return the VTT file
}
