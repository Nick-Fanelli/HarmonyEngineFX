class GameObject {
    constructor(position = Vector2(), scale = new Scale()) {
        this.position = position;
        this.scale = scale;
    }

    onCreate() {}
    update(deltaTime) {}
    draw(ctx) {}
    onDestroy() {}
}