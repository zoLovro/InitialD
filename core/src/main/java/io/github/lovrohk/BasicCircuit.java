package io.github.lovrohk;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class BasicCircuit implements Screen {
    ExtendViewport viewport;
    SpriteBatch spriteBatch;
    Texture ae86Texture;
    Sprite ae86Sprite;
    TextureRegion ae86_closed;
    TextureRegion ae86_open;
    boolean lightsOpen = false;

    Pixmap roadMap;
    Texture roadTexture;
    TmxMapLoader tmxMapLoader = new TmxMapLoader();
    TiledMap map = tmxMapLoader.load("maps/basicCircuit.tmx");
    OrthogonalTiledMapRenderer renderer = new OrthogonalTiledMapRenderer(map);

    Vector2 position = new Vector2(32,13);
    Vector2 velocity = new Vector2(0, 0);
    float maxSpeed = 150;
    float rotationDeg = 0;
    float acceleration = 10f;
    float centerX;
    float centerY;

    OrthographicCamera camera;


    public BasicCircuit() {
        viewport = new ExtendViewport(300, 300);
        spriteBatch = new SpriteBatch();

        ae86Texture = new Texture(Gdx.files.internal("cars/ae86.png"));
        ae86_closed = new TextureRegion(ae86Texture, 0, 0, 24, 44);
        ae86_open = new TextureRegion(ae86Texture, 24, 0, 24, 44);
        ae86Sprite = new Sprite(ae86_closed);
        ae86Sprite.setSize(24,44);
        ae86Sprite.setPosition(300, 100);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.WHITE);

        centerX = ae86Sprite.getX() + ae86Sprite.getWidth() / 2f;
        centerY = ae86Sprite.getY() + ae86Sprite.getHeight() / 2f;

        // Explicitly center the camera on the car
        camera = (OrthographicCamera) viewport.getCamera();
        camera.position.set(ae86Sprite.getX() + ae86Sprite.getWidth() / 2f,
            ae86Sprite.getY() + ae86Sprite.getHeight() / 2f,
            0);

        // Clamp camera so it stays within map bounds
        TiledMapTileLayer baseLayer = (TiledMapTileLayer) map.getLayers().get(0);
        float mapWidth = baseLayer.getWidth() * baseLayer.getTileWidth();
        float mapHeight = baseLayer.getHeight() * baseLayer.getTileHeight();
        float halfWidth = camera.viewportWidth * 0.5f * camera.zoom;
        float halfHeight = camera.viewportHeight * 0.5f * camera.zoom;

        camera.position.x = MathUtils.clamp(camera.position.x, halfWidth - 128, mapWidth - halfWidth - 128);
        camera.position.y = MathUtils.clamp(camera.position.y, halfHeight, mapHeight - halfHeight);
        camera.update();

        // Apply camera projection
        viewport.apply();
        spriteBatch.setProjectionMatrix(camera.combined);

        // Render map
        renderer.setView(camera);
        renderer.render();

        // Game logic and input
        spriteBatch.begin();

        logic();
        input(delta);
        ae86Sprite.draw(spriteBatch);
        ae86Sprite.setOriginCenter();

        spriteBatch.end();
    }

    private void input(float delta) {
        Vector2 forward = new Vector2(MathUtils.cosDeg(rotationDeg), MathUtils.sinDeg(rotationDeg));
        float turnSpeed = 100f;

        // Handle rotation
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            rotationDeg += turnSpeed * delta;
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            rotationDeg -= turnSpeed * delta;
        }

        // Apply acceleration
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            velocity.add(forward.cpy().scl(acceleration * delta));
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            velocity.sub(forward.cpy().scl(acceleration * delta));
        }

        // Limit speed
        if (velocity.len() > maxSpeed) {
            velocity.setLength(maxSpeed);
        }

        // Drift grip
        Vector2 forwardDir = new Vector2(MathUtils.cosDeg(rotationDeg), MathUtils.sinDeg(rotationDeg));
        Vector2 lateral = velocity.cpy().sub(forwardDir.cpy().scl(velocity.dot(forwardDir)));
        velocity.sub(lateral.scl(0.02f));

        // Friction
        velocity.scl(0.98f);

        // Try move on X
        Vector2 testX = new Vector2(position.x + velocity.x, position.y);
        if (!isCollision(testX)) {
            position.x += velocity.x;
        } else {
            velocity.x = 0;
        }

        // Try move on Y
        Vector2 testY = new Vector2(position.x, position.y + velocity.y);
        if (!isCollision(testY)) {
            position.y += velocity.y;
        } else {
            velocity.y = 0;
        }

        ae86Sprite.setPosition(position.x, position.y);
        ae86Sprite.setRotation(rotationDeg - 90);
    }

    private boolean isCollision(Vector2 pos) {
        TiledMapTileLayer collisionLayer = (TiledMapTileLayer) map.getLayers().get("Collision");
        float spriteCenterX = pos.x + ae86Sprite.getWidth() / 2f;
        float spriteCenterY = pos.y + ae86Sprite.getHeight() / 2f;

        int tileX = (int) (spriteCenterX / collisionLayer.getTileWidth());
        int tileY = (int) (spriteCenterY / collisionLayer.getTileHeight());

        TiledMapTileLayer.Cell cell = collisionLayer.getCell(tileX, tileY);
        return cell != null;
    }


    private void logic() {

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
