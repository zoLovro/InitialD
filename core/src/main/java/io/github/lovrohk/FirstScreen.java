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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class FirstScreen implements Screen {
    ExtendViewport viewport;
    SpriteBatch spriteBatch;
    Texture ae86Texture;
    Sprite ae86Sprite;
    TextureRegion ae86_closed;
    TextureRegion ae86_open;
    boolean lightsOpen = false;

    Pixmap roadMap;
    Texture roadTexture;

    Vector2 position = new Vector2(32,13);
    Vector2 velocity = new Vector2(0, 0);
    float maxSpeed = 150;
    float rotationDeg = 0;
    float acceleration = 10f;
    float centerX;
    float centerY;
    int[] checkpoint = {100, 100};

    public FirstScreen() {
        viewport = new ExtendViewport(300, 300);
        spriteBatch = new SpriteBatch();
        ae86Texture = new Texture(Gdx.files.internal("cars/ae86.png"));
        ae86_closed = new TextureRegion(ae86Texture, 0, 0, 24, 44);
        ae86_open = new TextureRegion(ae86Texture, 24, 0, 24, 44);
        ae86Sprite = new Sprite(ae86_closed);
        ae86Sprite.setSize(24,44);
        ae86Sprite.setPosition(32, 13);

        roadMap = new Pixmap(Gdx.files.internal("maps/basicCircuit.png"));
        roadTexture = new Texture(roadMap);
    }

    @Override
    public void render(float delta) {
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

        spriteBatch.end();
    }

    private void input(float delta) {
        // Sprite change
        if(Gdx.input.isKeyPressed(Input.Keys.M) && !lightsOpen) {
            ae86Sprite = new Sprite(ae86_open);
            lightsOpen = true;
        } else if(Gdx.input.isKeyPressed(Input.Keys.M) && lightsOpen) {
            ae86Sprite = new Sprite(ae86_closed);
            lightsOpen = false;
        }

        Vector2 forward = new Vector2(MathUtils.cosDeg(rotationDeg), MathUtils.sinDeg(rotationDeg));
        float turnSpeed = 100f;

        // Accelerate forward/backward
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            velocity.add(forward.cpy().scl(acceleration * delta));
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            velocity.sub(forward.cpy().scl(acceleration * delta));
        }

        // Cap to max speed
        if (velocity.len() > maxSpeed) {
            velocity.setLength(maxSpeed);
        }

        // Turn left/right
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            rotationDeg += turnSpeed * delta;
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            rotationDeg -= turnSpeed * delta;
        }

        // Drift grip: remove sideways movement
        Vector2 forwardDir = new Vector2(MathUtils.cosDeg(rotationDeg), MathUtils.sinDeg(rotationDeg));
        Vector2 lateral = velocity.cpy().sub(forwardDir.cpy().scl(velocity.dot(forwardDir)));
        velocity.sub(lateral.scl(0.02f));

        // Drag/friction
        velocity.scl(0.98f);

        // Move car
        position.add(velocity.cpy());
        ae86Sprite.setPosition(position.x, position.y);
        ae86Sprite.setRotation(rotationDeg - 90);
    }


    private void logic() {
        float spriteCenterX = ae86Sprite.getX() + ae86Sprite.getWidth() / 2f;
        float spriteCenterY = ae86Sprite.getY() + ae86Sprite.getHeight() / 2f;

        int pixelX = MathUtils.clamp((int) spriteCenterX, 0, roadMap.getWidth() - 1);
        int pixelY = MathUtils.clamp((int) spriteCenterY, 0, roadMap.getHeight() - 1);

        // checking if im on the road or not
        int pixel = roadMap.getPixel(pixelX, pixelY);
        Color color = new Color();
        Color.rgba8888ToColor(color, pixel); // extracts RGBA components from raw pixel
        Color target = Color.valueOf("#302f2f");
        if(!color.equals(target)) {
            ae86Sprite.setPosition(checkpoint[0], checkpoint[1]);
            //velocity.scl(0);
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
