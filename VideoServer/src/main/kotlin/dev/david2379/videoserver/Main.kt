package dev.david2379.videoserver

fun main() {
    Storage.init()

    val filenames = Storage.listFiles()
    val (videoName, subtitlesName) = getVideoAndSubtitlesFromUser(filenames)
        ?: run {
            println("File input failed. Exiting...")
            return
        }

    if (runServer(videoName, subtitlesName)) {
        println("Press Enter to exit")
        readln() // Wait for the user to press Enter before exiting
    }
    println("Exiting...")
}
