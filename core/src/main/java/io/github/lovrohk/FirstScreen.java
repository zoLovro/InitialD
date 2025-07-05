package io.github.lovrohk;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class FirstScreen implements Screen {
    ExtendViewport viewport;
    SpriteBatch spriteBatch;
    Texture ae86Texture;
    Sprite ae86Sprite;

    Pixmap roadMap;
    Texture roadTexture;

    float speedX = 0;
    float speedY = 0;
    float maxSpeed = 300;
    float acceleration = 5f;
    float centerX;
    float centerY;

    public FirstScreen() {
        viewport = new ExtendViewport(200, 200);
        spriteBatch = new SpriteBatch();
        ae86Texture = new Texture(Gdx.files.internal("cars/ae86.png"));
        TextureRegion ae86_closed = new TextureRegion(ae86Texture, 0, 0, 24, 44);
        TextureRegion ar86_open = new TextureRegion(ae86Texture, 24, 0, 24, 44);
        ae86Sprite = new Sprite(ae86_closed);
        ae86Sprite.setSize(24,44);
        ae86Sprite.setPosition(32, 13);

        roadMap = new Pixmap(Gdx.files.internal("maps/basicCircuit.png"));
        roadTexture = new Texture(roadMap);
    }

    @Override
    public void render(float delta) {
        float angle = (float) Math.toDegrees(Math.atan2(speedY, speedX));
        ScreenUtils.clear(Color.WHITE);
        centerX = ae86Sprite.getX() + ae86Sprite.getWidth() / 2f;
        centerY = ae86Sprite.getY() + ae86Sprite.getHeight() / 2f;

        //camera
        viewport.getCamera().position.set(centerX, centerY, 0);
        viewport.getCamera().update();

        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();

        spriteBatch.draw(roadTexture, 0, 0);
        logic();
        input(delta);
        ae86Sprite.draw(spriteBatch);
        ae86Sprite.setOriginCenter();
        if (speedX != 0 || speedY != 0) {
            ae86Sprite.setRotation(angle - 90);
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

        ae86Sprite.translate(speedX * delta, speedY * delta);
    }

    private void logic() {
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        float spriteX = ae86Sprite.getX();
        float spriteY = ae86Sprite.getY();

        float mapHeight = roadMap.getHeight();
        float mapWidth = roadMap.getWidth();

        // checking if im on the road or not
        int pixel = roadMap.getPixel((int) spriteX, (int) spriteY);
        Color color = new Color();
        Color.rgba8888ToColor(color, pixel); // extracts RGBA components from raw pixel
        if(!color.equals(Color.BLACK)) {
            System.out.println("Nisic");
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
