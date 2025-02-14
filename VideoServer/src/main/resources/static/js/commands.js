document.addEventListener('keydown', onKeyDown);

const buttonToggleMenu = '1';
const buttonToggleFullscreen = '2';
const buttonPlayPause = '3';
const buttonSkipBackward = '4';
const buttonSkipForward = '5';
const buttonToggleSubtitles = '6';
const buttonBigSkipBackward = '7';
const buttonBigSkipForward = '8';

function onKeyDown(event) {
    if (event.key === buttonToggleMenu/** || event.keyCode === 49 || event.keyCode === 101*/) {
        toggleMenu();
    } else if (event.key === buttonToggleFullscreen) {
        toggleFullscreen();
    } else if (event.key === buttonPlayPause) {
        togglePlayPause();
    } else if (event.key === buttonSkipBackward) {
        skipBackward(10);
    } else if (event.key === buttonSkipForward) {
        skipForward(10);
    } else if (event.key === buttonToggleSubtitles) {
        toggleSubtitles();
    } else if (event.key === buttonBigSkipBackward) {
        skipBackward(120);
    } else if (event.key === buttonBigSkipForward) {
        skipForward(120);
    }
}

/**
 * Get the menu toggler function to show/hide the menu (call 1 time only)
 * @returns {(function(): void)|*}
 */
function getMenuToggler(){
    let menuVisible = 0; // Initial state will be toggled automatically 1 time on load
    const infoToggleMenu = `Press '${buttonToggleMenu}' to toggle menu`;
    const menuInfo = infoToggleMenu +
        `\nPress '${buttonToggleFullscreen}' to toggle full screen mode` +
        `\nPress '${buttonPlayPause}' to play/pause the video` +
        `\nPress '${buttonSkipBackward}' to go backwards 10 seconds` +
        `\nPress '${buttonSkipForward}' to go forward 10 seconds` +
        `\nPress '${buttonToggleSubtitles}' to toggle subtitles` +
        `\nPress '${buttonBigSkipBackward}' to go backwards 2 minutes` +
        `\nPress '${buttonBigSkipForward}' to go forward 2 minutes`;

    /**
     * Toggle menu
     */
    function menuToggler() {
        const menuElemId = 'menu';
        const menuElem = document.getElementById(menuElemId);
        menuVisible = (menuVisible + 1) % 3; // 0 -> 1 -> 2 -> 0
        if (menuVisible === 0) {
            menuElem.innerHTML = '';
        } else if (menuVisible === 1) {
            menuElem.innerHTML = infoToggleMenu;
        } else if (menuVisible === 2) {
            menuElem.innerHTML = menuInfo;
        }
    }

    menuToggler(); // Required to show the initial menu (will be toggle automatically 1 time on load)

    return menuToggler;
}

const toggleMenu = getMenuToggler();

/**
 * Toggle full screen mode
 */
function toggleFullscreen() {
    const videoElemId = 'video';
    const video = document.getElementById(videoElemId);
    if (!document.fullscreenElement) {
        video.requestFullscreen().catch(err => {
            console.log(`Error attempting to enable full-screen mode: ${err.message} (${err.name})`);
        });
    } else {
        document.exitFullscreen();
    }
}

/**
 * Toggle play/pause
 */
function togglePlayPause() {
    const videoElemId = 'video';
    const video = document.getElementById(videoElemId);
    if (video.paused) {
        video.play();
    } else {
        video.pause();
    }
}

/**
 * Skip backward x seconds
 * @param seconds - number of seconds
 */
function skipBackward(seconds) {
    const videoElemId = 'video';
    const video = document.getElementById(videoElemId);
    video.currentTime = Math.max(0, video.currentTime - seconds);
}

/**
 * Skip forward x seconds
 * @param seconds - number of seconds
 */
function skipForward(seconds) {
    const videoElemId = 'video';
    const video = document.getElementById(videoElemId);
    video.currentTime = Math.min(video.duration, video.currentTime + seconds);
}

/**
 * Toggle subtitles
 */
function toggleSubtitles() {
    const videoElemId = 'video';
    const video = document.getElementById(videoElemId);
    const tracks = video.textTracks;
    let currentTrack = -1;

    for (let i = 0; i < tracks.length; i++) {
        if (tracks[i].mode === 'showing') {
            currentTrack = i;
            tracks[i].mode = 'disabled';
            break;
        }
    }

    const nextTrack = (currentTrack + 1) % (tracks.length + 1);
    if (nextTrack < tracks.length) {
        tracks[nextTrack].mode = 'showing';
    }
}
