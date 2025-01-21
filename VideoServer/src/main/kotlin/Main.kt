import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.http.content.*
import io.ktor.server.netty.Netty
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.utils.io.*
import java.io.File
import java.io.RandomAccessFile
import java.nio.ByteBuffer

const val videoName = "video.mp4" // Set the video name
const val subtitlesName = "subtitles.srt" // Set the subtitles name (SRT format)

const val port = 3000 // Port to listen on

const val storagePath = "storage" // Folder where the video and subtitles are stored
const val videoPath = "$storagePath/$videoName"
const val subtitlesPath = "$storagePath/$subtitlesName"

fun main() {
    val vttSubtitlesPath = loadSrt(subtitlesPath) ?: run { // Load the subtitles and convert them to VTT format
        println("Subtitles file not found")
        return
    }

    embeddedServer(factory = Netty, port = port) { // Start the server
        routing {
            staticResources("/", "static")

            get("/video") { // Serve the video file
                val videoFile = File(videoPath)
                if (!videoFile.exists()) {
                    call.respond(HttpStatusCode.NotFound)
                    return@get
                }

                val range = call.request.headers["Range"]
                if (range == null) {
                    call.respondFile(videoFile)
                } else {
                    val ranges = range.removePrefix("bytes=").split("-")
                    val start = ranges[0].toLong()
                    val end = if (ranges.size > 1 && ranges[1].isNotEmpty()) ranges[1].toLong() else videoFile.length() - 1
                    val length = end - start + 1

                    call.respond(object : OutgoingContent.ReadChannelContent() {
                        override val contentLength: Long = length
                        override val contentType = ContentType.Video.MP4
                        override val status = HttpStatusCode.PartialContent
                        override val headers = headersOf(
                            "Content-Range" to listOf("bytes $start-$end/${videoFile.length()}")
                        )

                        override fun readFrom(): ByteReadChannel = writer {
                            val fileChannel = RandomAccessFile(videoFile, "r").channel
                            fileChannel.position(start)
                            val buffer = ByteBuffer.allocate(8192)
                            var remaining = length
                            while (remaining > 0) {
                                buffer.clear()
                                val bytesRead = fileChannel.read(buffer)
                                if (bytesRead == -1) break
                                buffer.flip()
                                channel.writeFully(buffer)
                                remaining -= bytesRead
                            }
                            fileChannel.close()
                            channel.close()
                        }.channel
                    })
                }
            }

            get("/subtitles") { // Serve the subtitles file
                call.respondText(vttSubtitlesPath.readText(charset))
            }
        }
    }.start(wait = false)

    println("Press Enter to exit")
    readln() // Wait for the user to press Enter before exiting
}
