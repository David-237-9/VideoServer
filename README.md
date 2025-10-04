# Video Player Server

This application is a simple video player server built using Kotlin and Ktor. It serves a video file and its corresponding subtitles, converting the subtitles from SRT to VTT format. The application includes the following features:

- Serves a video file from the `storage` directory.
- Converts SRT subtitles to VTT format and serves them.
- Supports partial content requests for efficient video streaming.
- Provides a basic HTML page with a video player to play the video and display subtitles.

## Running the Application

Run the application with the following command at the project base directory (where gradlew is located):
#### Unix / macOS
```
./gradlew run
```
#### Windows (PowerShell / CMD)
```
.\gradlew run
```
or
```
gradlew run
```

## File Structure

- `src/main/resources/static/index.html`: Contains the HTML for the video player.
- `src/main/resources/static/js/commands.js`: Contains JavaScript for handling video player commands.
- `src/main/kotlin/FilesAuxiliar.kt`: Contains functions related to file handling and subtitle conversion.
- `src/main/kotlin/Ip.kt`: Contains functions to get the local IP address.
- `src/main/kotlin/Main.kt`: Contains the main server code to serve the video and subtitles.
- `src/main/kotlin/Server.kt`: Contains server configuration and routing.
- `src/main/kotlin/Storage.kt`: Contains functions related to storage management.
- `src/main/test/`: Directory that contains test cases for the application.
- `storage/`: Directory where the video file and subtitles are stored.
