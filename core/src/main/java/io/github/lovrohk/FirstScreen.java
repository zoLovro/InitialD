package io.github.lovrohk;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class FirstScreen implements Screen {
    ExtendViewport viewport;
    SpriteBatch spriteBatch;
    Texture ae86Texture;
    Sprite ae68Sprite;

    Pixmap roadMap;
    Texture roadTexture;
    int pixel;
    Color color;

    float speedX = 0;
    float speedY = 0;
    float maxSpeed = 500;
    float acceleration = 10f;

    public FirstScreen() {
        viewport = new ExtendViewport(1920, 1080);
        spriteBatch = new SpriteBatch();
        ae86Texture = new Texture(Gdx.files.internal("cars/ae86.jpg"));
        ae68Sprite = new Sprite(ae86Texture);

        roadMap = new Pixmap(Gdx.files.internal("maps/black_map.png "));
        roadTexture = new Texture(roadMap);
    }

    @Override
    public void render(float delta) {
        float angle = (float) Math.toDegrees(Math.atan2(speedY, speedX));
        ScreenUtils.clear(Color.WHITE);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();
        spriteBatch.draw(roadTexture, 0, 0);
        logic();
        input(delta);
        ae68Sprite.draw(spriteBatch);
        ae68Sprite.setOriginCenter();
        if (speedX != 0 || speedY != 0) {
            ae68Sprite.setRotation(angle - 90);
        }

        spriteBatch.end();
    }

    private void input(float delta) {
        // Y axis
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            speedY += acceleration;
            if (speedY > maxSpeed) speedY = maxSpeed;
        } else if(Gdx.input.isKeyPressed(Input.Keys.S)) {
            speedY -= acceleration;
            if (speedY < -maxSpeed) speedY = -maxSpeed;
        }
        else {
            if(speedY > 0) speedY -= acceleration;
            else if (speedY < 0) speedY += acceleration;
            if (Math.abs(speedY) < acceleration) speedY = 0;
        }

        // X axis
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            speedX += acceleration;
            if (speedX > maxSpeed) speedX = maxSpeed;
        } else if(Gdx.input.isKeyPressed(Input.Keys.A)) {
            speedX -= acceleration;
            if (speedX < -maxSpeed) speedX = -maxSpeed;
        }
        else {
            if(speedX > 0) speedX -= acceleration;
            else if (speedX < 0) speedX += acceleration;
            if (Math.abs(speedX) < acceleration) speedX = 0;
        }

        ae68Sprite.translate(speedX * delta, speedY * delta);
    }

    private void logic() {
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        float spriteX = ae68Sprite.getX();
        float spriteY = ae68Sprite.getY();

        // collisions with screen edges (for now before my map is implemented)
        if (spriteX < 0 || spriteX + ae68Sprite.getWidth() > worldWidth ||
            spriteY < 0 || spriteY + ae68Sprite.getHeight() > worldHeight) {
            ae68Sprite.setPosition(100, 100);
            speedX = 0;
            speedY = 0;
        }


    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        ae86Texture.dispose();
        spriteBatch.dispose();
    }
    @Override public void show() {}
    @Override public void hide() {}
    @Override public void pause() {}
    @Override public void resume() {}
}
