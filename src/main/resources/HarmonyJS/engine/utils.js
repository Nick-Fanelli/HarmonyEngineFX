class Vector2 {
    constructor(x = 0, y = 0) {
        this.x = x;
        this.y = y;
    }

    set(x, y) { 
        this.x = x; 
        this.y = y;
        return this;
    }

    copy() { return new Vector2(this.x, this.y); } 

    add(x, y) {
        this.x += x;
        this.y += y;
        return this;
    }

    sub(x, y) {
        this.x -= x;
        this.y -= y;
        return this;
    }

    mul(x, y) {
        this.x *= x;
        this.y *= y;
        return this; 
    }

    div(x, y) {
        this.x /= x;
        this.y /= y;
        return this;
    }

    add(vector2) {
        this.x += vector2.x;
        this.y += vector2.y;
        return this;
    }

    sub(vector2) {
        this.x -= vector2.x;
        this.y -= vector2.y;
        return this;
    }

    mul(vector2) {
        this.x *= vector2.x;
        this.y *= vector2.y;
        return this;
    }

    div(vector2) {
        this.x /= vector2.x;
        this.y /= vector2.y;
        return this;
    }

    inverse() {
        this.x = -this.x;
        this.y = -this.y;
        return this;
    }

    abs() {
        this.x = Math.abs(this.x);
        this.y = Math.abs(this.y);
        return this;
    }

    ceil() {
        this.x = Math.ceil(this.x);
        this.y = Math.ceil(this.y);
        return this;
    }

    floor() {
        this.x = Math.floor(this.x);
        this.y = Math.floor(this.y);
        return this;
    }

    min(minX, minY) {
        this.x = Math.min(this.x, minX);
        this.y = Math.min(this.y, minY);
        return this;
    }

    minX(min) { this.x = Math.min(this.x, min); return this; }
    minY(min) { this.y = Math.min(this.y, min); return this; }

    max(maxX, maxY) {
        this.x = Math.max(this.x, maxX);
        this.y = Math.max(this.y, maxY);
        return this;
    }

    maxX(max) { this.x = Math.max(this.x, max); return this; }
    maxY(max) { this.y = Math.max(this.y, max); return this; }
}

class Scale {
    constructor(width = 0, height = 0) {
        this.width = width;
        this.height = height;
    }

    set(width, height) {
        this.width = width;
        this.height = height;
    }
}

const texturesArray = [];
const statesArray = [];
const camera = new Vector2();