import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.OutgoingContent
import io.ktor.http.headersOf
import io.ktor.server.application.call
import io.ktor.server.engine.embeddedServer
import io.ktor.server.http.content.staticResources
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.origin
import io.ktor.server.response.respond
import io.ktor.server.response.respondFile
import io.ktor.server.response.respondRedirect
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.close
import io.ktor.utils.io.writer
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import kotlin.text.removePrefix

const val PORT = 3000 // Port to listen on

const val DEFAULT_PATH = "/"
const val WATCH_PATH = "/watch"
const val VIDEO_ROUTING_PATH = "/video" // Path to access the video
const val SUBTITLES_ROUTING_PATH = "/subtitles" // Path to access the

const val STATIC_PACKAGE_PATH = "/static"

fun runServer(videoName: String, subtitlesName: String?): Boolean {
    val videoFile = Storage.getVideoFile(videoName)
    if (videoFile == null) {
        println("Video file not found")
        return false
    }
    val vttSubtitlesPath = subtitlesName?.let { Storage.getSubtitlesFile(it) } // Load the subtitles and convert them to VTT format if they exist

    embeddedServer(factory = Netty, port = PORT) { // Start the server
        routing {
            get(DEFAULT_PATH) {
                call.respondRedirect(WATCH_PATH)
                val remoteHost = call.request.origin.remoteHost
                println("New connection from: $remoteHost")
            }

            staticResources(WATCH_PATH, "$STATIC_PACKAGE_PATH/watch")

            get(VIDEO_ROUTING_PATH) { // Serve the video file
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

            get(SUBTITLES_ROUTING_PATH) { // Serve the subtitles file
                call.respondText(vttSubtitlesPath?.readText(charset) ?: "No subtitles available")
            }
        }
    }.start(wait = false)

    println("Server started at http://localhost:$PORT")
    getLocalIpAddress().let { ip ->
        println(if (ip == null) "LAN not available" else "For lAN access, visit http://${ip}:$PORT")
    }

    return true
}
