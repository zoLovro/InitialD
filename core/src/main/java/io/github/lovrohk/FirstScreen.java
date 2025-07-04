package io.github.lovrohk;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class FirstScreen implements Screen {
    ExtendViewport viewport;
    SpriteBatch spriteBatch;
    Texture ae86Texture;

    public FirstScreen() {
        viewport = new ExtendViewport(800, 600);
        spriteBatch = new SpriteBatch();
        ae86Texture = new Texture(Gdx.files.internal("cars/ae86.jpg"));
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();
        spriteBatch.draw(ae86Texture, 0, 0, ae86Texture.getWidth(), ae86Texture.getHeight());
        spriteBatch.end();
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
