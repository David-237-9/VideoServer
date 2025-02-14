document.addEventListener('keydown', onKeyDown);

const buttonToggleMenu = '1';
const buttonToggleFullscreen = '2';
const buttonPlayPause = '3';
const buttonSkipBackward = '4';
const buttonSkipForward = '5';
const buttonToggleSubtitles = '6';
const buttonBigSkipBackward = '7';
const buttonBigSkipForward = '8';

const smallSkip = 10;
const bigSkip = 120;

function onKeyDown(event) {
    if (event.key === buttonToggleMenu/** || event.keyCode === 49 || event.keyCode === 101*/) {
        toggleMenu();
    } else if (event.key === buttonToggleFullscreen) {
        toggleFullscreen();
    } else if (event.key === buttonPlayPause) {
        togglePlayPause();
    } else if (event.key === buttonSkipBackward) {
        skipBackward(smallSkip);
    } else if (event.key === buttonSkipForward) {
        skipForward(smallSkip);
    } else if (event.key === buttonToggleSubtitles) {
        toggleSubtitles();
    } else if (event.key === buttonBigSkipBackward) {
        skipBackward(bigSkip);
    } else if (event.key === buttonBigSkipForward) {
        skipForward(bigSkip);
    }
}

/**
 * Get the menu toggler function to show/hide the menu (call 1 time only)
 * @returns {(function(): void)|*}
 */
function getMenuToggler(){
    let menuVisible = 0; // Initial state will be toggled automatically 1 time on load
    const lang = navigator.language || navigator.userLanguage; // Get the browser language

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
            menuElem.innerHTML = getInfoToggleMenu(lang, buttonToggleMenu);
        } else if (menuVisible === 2) {
            menuElem.innerHTML = getMenuInfo(
                lang,
                buttonToggleMenu,
                buttonToggleFullscreen,
                buttonPlayPause,
                buttonSkipBackward,
                buttonSkipForward,
                buttonToggleSubtitles,
                buttonBigSkipBackward,
                buttonBigSkipForward,
                smallSkip,
                bigSkip
            );
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

/**
 * Get the menu info text
 * @param lang - Language code
 * @param button - Button to toggle the menu
 * @returns {string} - Menu info text
 */
function getInfoToggleMenu(lang, button) {
    if (lang === 'pt' || lang === 'pt-PT' || lang === 'pt-BR')
        return `Pressione '${button}' para alterar o menu`;
    else if (lang === 'es' || lang === 'es-ES' || lang === 'es-AR' || lang === 'es-MX')
        return `Presiona '${button}' para alternar el menú`;
    else if (lang === 'fr' || lang === 'fr-FR')
        return `Appuyez sur '${button}' pour basculer le menu`;
    else if (lang === 'de' || lang === 'de-DE')
        return `Drücken Sie '${button}', um das Menü umzuschalten`;
    else
        return `Press '${button}' to toggle menu`;
}

/**
 * Get the menu info text
 * @param lang - Language code
 * @param buttonToggleMenu - Button to toggle the menu
 * @param buttonToggleFullscreen - Button to toggle fullscreen
 * @param buttonPlayPause - Button to play/pause
 * @param buttonSkipBackward - Button to skip backward
 * @param buttonSkipForward - Button to skip forward
 * @param buttonToggleSubtitles - Button to toggle subtitles
 * @param buttonBigSkipBackward - Button to skip backward 2 minutes
 * @param buttonBigSkipForward - Button to skip forward 2 minutes
 * @param smallSkip - Number of seconds to skip in small skip
 * @param bigSkip - Number of seconds to skip in big skip
 * @returns {string} - Menu info text
 */
function getMenuInfo(
    lang,
    buttonToggleMenu,
    buttonToggleFullscreen,
    buttonPlayPause,
    buttonSkipBackward,
    buttonSkipForward,
    buttonToggleSubtitles,
    buttonBigSkipBackward,
    buttonBigSkipForward,
    smallSkip,
    bigSkip
) {
    const infoToggleMenu = getInfoToggleMenu(lang, buttonToggleMenu);
    if (lang === 'pt' || lang === 'pt-PT' || lang === 'pt-BR')
        return infoToggleMenu +
            `\nPressione '${buttonToggleFullscreen}' para alterar o modo de ecrã inteiro` +
            `\nPressione '${buttonPlayPause}' para reproduzir/pausar o vídeo` +
            `\nPressione '${buttonSkipBackward}' para retroceder ${smallSkip} segundos` +
            `\nPressione '${buttonSkipForward}' para avançar ${smallSkip} segundos` +
            `\nPressione '${buttonToggleSubtitles}' para alterar legendas` +
            `\nPressione '${buttonBigSkipBackward}' para retroceder ${bigSkip} segundos` +
            `\nPressione '${buttonBigSkipForward}' para avançar ${bigSkip} segundos`;
    else if (lang === 'es' || lang === 'es-ES' || lang === 'es-AR' || lang === 'es-MX')
        return infoToggleMenu +
            `\nPresiona '${buttonToggleFullscreen}' para alternar el modo de pantalla completa` +
            `\nPresiona '${buttonPlayPause}' para reproducir/pausar el video` +
            `\nPresiona '${buttonSkipBackward}' para retroceder ${smallSkip} segundos` +
            `\nPresiona '${buttonSkipForward}' para avanzar ${smallSkip} segundos` +
            `\nPresiona '${buttonToggleSubtitles}' para alternar subtítulos` +
            `\nPresiona '${buttonBigSkipBackward}' para retroceder ${bigSkip} segundos` +
            `\nPresiona '${buttonBigSkipForward}' para avanzar ${bigSkip} segundos`;
    else if (lang === 'fr' || lang === 'fr-FR')
        return infoToggleMenu +
            `\nAppuyez sur '${buttonToggleFullscreen}' pour basculer en mode plein écran` +
            `\nAppuyez sur '${buttonPlayPause}' pour lire/mettre en pause la vidéo` +
            `\nAppuyez sur '${buttonSkipBackward}' pour reculer de ${smallSkip} secondes` +
            `\nAppuyez sur '${buttonSkipForward}' pour avancer de ${smallSkip} secondes` +
            `\nAppuyez sur '${buttonToggleSubtitles}' pour basculer les sous-titres` +
            `\nAppuyez sur '${buttonBigSkipBackward}' pour reculer de ${bigSkip} secondes` +
            `\nAppuyez sur '${buttonBigSkipForward}' pour avancer de ${bigSkip} secondes`;
    else if (lang === 'de' || lang === 'de-DE')
        return infoToggleMenu +
            `\nDrücken Sie '${buttonToggleFullscreen}', um den Vollbildmodus zu aktivieren` +
            `\nDrücken Sie '${buttonPlayPause}', um das Video abzuspielen/anzuhalten` +
            `\nDrücken Sie '${buttonSkipBackward}', um ${smallSkip} Sekunden zurückzugehen` +
            `\nDrücken Sie '${buttonSkipForward}', um ${smallSkip} Sekunden vorzuspulen` +
            `\nDrücken Sie '${buttonToggleSubtitles}', um Untertitel umzuschalten` +
            `\nDrücken Sie '${buttonBigSkipBackward}', um ${bigSkip} Sekunden zurückzugehen` +
            `\nDrücken Sie '${buttonBigSkipForward}', um ${bigSkip} Sekunden vorzuspulen`;
    else
        return infoToggleMenu +
            `\nPress '${buttonToggleFullscreen}' to toggle full screen mode` +
            `\nPress '${buttonPlayPause}' to play/pause the video` +
            `\nPress '${buttonSkipBackward}' to go backwards ${smallSkip} seconds` +
            `\nPress '${buttonSkipForward}' to go forward ${smallSkip} seconds` +
            `\nPress '${buttonToggleSubtitles}' to toggle subtitles` +
            `\nPress '${buttonBigSkipBackward}' to go backwards ${bigSkip} seconds` +
            `\nPress '${buttonBigSkipForward}' to go forward ${bigSkip} seconds`;
}
