class Rectangle {
    constructor(x = 0, y = 0, width = 0, height = 0, color = "white") {
        this.x = Number(x);
        this.y = Number(y);
        this.width = Number(width);
        this.height = Number(height);
        this.color = color;
    }

    draw(ctx) {
        ctx.fillStyle = this.color;
        ctx.fillRect(this.x, this.y, this.width, this.height);
    }
}

class Texture {
    constructor(id, name) {
        this.id = id;
        this.rawImage = new Image();
        this.rawImage.src = `./textures/${name}`;
    }
}

class Sprite extends GameObject {

    constructor(position = new Vector2(), scale = new Scale(), texture) {
        super(position, scale);
        this.texture = texture;
    }

    draw(ctx) {
        ctx.drawImage(this.texture.rawImage, this.position.x + camera.x, this.position.y + camera.y);
    }

}