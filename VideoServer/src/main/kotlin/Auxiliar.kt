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
