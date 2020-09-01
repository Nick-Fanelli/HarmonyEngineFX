const canvas = document.querySelector("canvas");
const ctx = canvas.getContext("2d");

const stateManager = new StateManager();

const shouldLoop = true;

let deltaTime = 0;
let passedTimestamp = 0;
let launcherState;

window.onresize = resizeCanvas;

function startProject(state) {
    launcherState = state;
    waitForDocumentLoad();
}

function initialize() {
    if(canvas == NaN || canvas == null || ctx == NaN || ctx == null) waitForDocumentLoad();
    resizeCanvas();

    stateManager.setCurrentState(launcherState);

    window.requestAnimationFrame(loop);
}

function resizeCanvas() {
    canvas.height = window.innerHeight;
    canvas.width = window.innerWidth;
}

function loop(timestamp) {
    deltaTime = timestamp - passedTimestamp;
    passedTimestamp = timestamp;

    update(deltaTime);
    draw();

    if(shouldLoop) requestAnimationFrame(loop);
}

function update(deltaTime) {
    stateManager.update(deltaTime);
}

function draw() {
    ctx.clearRect(0, 0, canvas.width, canvas.height); // First
    stateManager.draw(ctx);
}

function waitForDocumentLoad() { document.addEventListener("DOMContentLoaded", initialize); }