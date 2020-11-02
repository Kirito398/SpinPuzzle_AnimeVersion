package com.bg.spinpuzzleanime.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.bg.spinpuzzleanime.SpinPuzzleAnime;

public class MenuState extends State {
    private BitmapFont font, name, version;

    public MenuState(final GameStateManager gsm) {
        super(gsm);
        camera.setToOrtho(false, SpinPuzzleAnime.WIDTH, SpinPuzzleAnime.HEIGHT);

        Gdx.input.setInputProcessor(new GestureDetector(new GestureDetector.GestureListener() {
            @Override
            public boolean touchDown(float x, float y, int pointer, int button) {
                return false;
            }

            @Override
            public boolean tap(float x, float y, int count, int button) {
                gsm.set(new LevelState(gsm));
                return false;
            }

            @Override
            public boolean longPress(float x, float y) {
                return false;
            }

            @Override
            public boolean fling(float velocityX, float velocityY, int button) {
                return false;
            }

            @Override
            public boolean pan(float x, float y, float deltaX, float deltaY) {
                return false;
            }

            @Override
            public boolean panStop(float x, float y, int pointer, int button) {
                return false;
            }

            @Override
            public boolean zoom(float initialDistance, float distance) {
                return false;
            }

            @Override
            public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
                return false;
            }

            @Override
            public void pinchStop() {

            }
        }));

        font = new BitmapFont();
        name = new BitmapFont();
        version = new BitmapFont();
        generator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.characters = font_chars;
        parameter.size = 50;
        parameter.color = Color.WHITE;
        font = generator.generateFont(parameter);
        parameter.size = 150;
        name = generator.generateFont(parameter);
        parameter.size = 80;
        parameter.color = Color.RED;
        version = generator.generateFont(parameter);
        generator.dispose();
    }

    @Override
    protected void handleInput() {
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        name.draw(sb, "SpinPuzzle", 600, 900);
        font.draw(sb, "Touch to start", 800, 400);
        version.draw(sb, "Anime", 1200,780);
        sb.end();
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void create() {

    }

    @Override
    public void dispose() {
        font.dispose();
        name.dispose();
        version.dispose();
    }
}
