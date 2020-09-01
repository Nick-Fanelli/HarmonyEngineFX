class State {
    constructor(name) {
        this.name = name;
        this.gameObjects = [];
    }

    onCreate() {

    }

    update(deltaTime) {
        for(let i = 0; i < this.gameObjects.length; i++) {
            this.gameObjects[i].update(deltaTime);
        }
    }

    draw(ctx) {
        for(let i = 0; i < this.gameObjects.length; i++) {
            this.gameObjects[i].draw(ctx);
        }
    }

    onDestroy() {
        for(let i = 0; i < this.gameObjects.length; i++) {
            this.gameObjects[i].onDestroy()
        }
    }

    addGameObject(gameObject) {
        gameObject.onCreate();
        this.gameObjects.push(gameObject);
    }

    removeGameObject(gameObject) {
        if(this.gameObjects.includes(gameObject)) {
            gameObject.onDestroy();
            let index = this.gameObjects.indexOf(gameObject);
            if(index > -1) { this.gameObjects.splice(index, 1); }
        }
    }
}

class StateManager {

    constructor() {
        this.currentState = null;
    }

    update(deltaTime) {
        if(this.currentState != null) this.currentState.update(deltaTime);
    }

    draw(ctx) {
        if(this.currentState != null) this.currentState.draw(ctx);
    }

    setCurrentState(state) {
        if(this.currentState != null) this.currentState.onDestroy();
        state.onCreate();
        this.currentState = state;
    }
}