# Video Player Server

This application is a simple video player server built using Kotlin and Ktor. It serves a video file and its corresponding subtitles, converting the subtitles from SRT to VTT format. The application includes the following features:

- Serves a video file from the `storage` directory.
- Converts SRT subtitles to VTT format and serves them.
- Supports partial content requests for efficient video streaming.
- Provides a basic HTML page with a video player to play the video and display subtitles.

## Running the Application

To run the application, execute the `main` function in `Main.kt`. The server will start on port selected port, and you can access the video player at `http://localhost:{port}/` or by LAN using another device.

## File Structure

- `src/main/resources/static/index.html`: Contains the HTML for the video player.
- `src/main/kotlin/Auxiliar.kt`: Contains functions to convert SRT subtitles to VTT format.
- `src/main/kotlin/Main.kt`: Contains the main server code to serve the video and subtitles.
- `storage/video.mp4`: The video file to be served.
- `storage/subtitles.srt`: The SRT subtitles file to be converted and served.
