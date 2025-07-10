package io.github.lovrohk;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class Main extends Game {
    // Music fields
    Music justAGame;
    Music softTime;
    Music lovePassion;
    Music perfectTime;
    Music crazyLover;
    Music rapture;
    Music hero;
    Music faceToFace;
    Music itsMyTime;
    Music superCatching;
    Music love;
    Music loveForMoney;
    Music divine;

    @Override
    public void create() {
        // Load music
        justAGame     = Gdx.audio.newMusic(Gdx.files.internal("music/album1/01justAGame.mp3"));
        softTime      = Gdx.audio.newMusic(Gdx.files.internal("music/album1/02SoftTime.mp3"));
        lovePassion   = Gdx.audio.newMusic(Gdx.files.internal("music/album1/03LovePassion.mp3"));
        perfectTime   = Gdx.audio.newMusic(Gdx.files.internal("music/album1/04PerfectTime.mp3"));
        crazyLover    = Gdx.audio.newMusic(Gdx.files.internal("music/album1/05CrazyLover.mp3"));
        rapture       = Gdx.audio.newMusic(Gdx.files.internal("music/album1/06Rapture.mp3"));
        hero          = Gdx.audio.newMusic(Gdx.files.internal("music/album1/07Hero.mp3"));
        faceToFace    = Gdx.audio.newMusic(Gdx.files.internal("music/album1/08FaceToFace.mp3"));
        itsMyTime     = Gdx.audio.newMusic(Gdx.files.internal("music/album1/09ItsMyTime.mp3"));
        superCatching = Gdx.audio.newMusic(Gdx.files.internal("music/album1/10SuperCatchingDesire.mp3"));
        love          = Gdx.audio.newMusic(Gdx.files.internal("music/album1/11Love.mp3"));
        loveForMoney  = Gdx.audio.newMusic(Gdx.files.internal("music/album1/12LoveForMoney.mp3"));
        divine        = Gdx.audio.newMusic(Gdx.files.internal("music/album1/13Divine.mp3"));

        // Start with first screen
        setScreen(new AkinaTrack());
    }
}
