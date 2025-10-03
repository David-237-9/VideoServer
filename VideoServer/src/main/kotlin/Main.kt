private const val VIDEO_NAME = "video.mp4" // Set the video name
private const val SUBTITLES_NAME = "subtitles.srt" // Set the subtitles name (SRT format)

fun main() {
    Storage.init()

    if (runServer(VIDEO_NAME, SUBTITLES_NAME)) {
        println("Press Enter to exit")
        readln() // Wait for the user to press Enter before exiting
    }
    println("Exiting...")
}
