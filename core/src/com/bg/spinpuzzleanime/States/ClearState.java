package com.bg.spinpuzzleanime.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.bg.spinpuzzleanime.SpinPuzzleAnime;

public class ClearState extends State {
    private BitmapFont font;
    private long score, time, bestScore, bestTime;
    private Texture image;
    private boolean isBestScore = false, isBestTime = false;

    public ClearState(final GameStateManager gsm, long s, long t, Texture img, final int number) {
        super(gsm);
        camera.setToOrtho(false, SpinPuzzleAnime.WIDTH, SpinPuzzleAnime.HEIGHT);
        score = s;
        time = t;
        image = img;

        Gdx.input.setInputProcessor(new GestureDetector(new GestureDetector.GestureListener() {
            @Override
            public boolean touchDown(float x, float y, int pointer, int button) {
                return false;
            }

            @Override
            public boolean tap(float x, float y, int count, int button) {
                SpinPuzzleAnime.showInterstitial();
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
        Gdx.input.setCatchBackKey(true);

        FileHandle file;
        String text;
        file = Gdx.files.local("last.lvl");
        file.writeString(String.valueOf(number),false);

        //Cleared
        file = Gdx.files.local("data_" + number +".cl");
        file.writeString("true",false);

        //Best score
        file = Gdx.files.local("data_" + number + ".sc");
        text = file.readString();
        bestScore = Long.valueOf(text);
        if(score < bestScore){
            file.writeString(String.valueOf(score),false);
            isBestScore = true;
        }

        //BestTime
        file = Gdx.files.local("data_" + number + ".tm");
        text = file.readString();
        bestTime = Long.valueOf(text);
        if(time < bestTime){
            file.writeString(String.valueOf(time),false);
            isBestTime = true;
        }

        font = new BitmapFont();
        generator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.characters = font_chars;
        parameter.size = 50;
        parameter.color = Color.WHITE;
        font = generator.generateFont(parameter);
        generator.dispose();
    }

    @Override
    protected void handleInput() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.BACK)){
            SpinPuzzleAnime.showInterstitial();
            gsm.set(new LevelState(gsm));
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        font.draw(sb,"Clear!", SpinPuzzleAnime.WIDTH/2 - 75, 1000);
        font.draw(sb,"Score: " + score ,SpinPuzzleAnime.WIDTH/4 - 100, 110);
        font.draw(sb,"Time: " + time, SpinPuzzleAnime.WIDTH/4*3 - 250, 110);
        if(isBestScore) font.draw(sb, "Best", SpinPuzzleAnime.WIDTH/4 + 190, 110);
        if(isBestTime) font.draw(sb, "Best",SpinPuzzleAnime.WIDTH/4*3 + 20, 110 );
        sb.draw(image, SpinPuzzleAnime.WIDTH/2 - image.getWidth()/2, SpinPuzzleAnime.HEIGHT/2 - image.getHeight()/2);
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
        image.dispose();
    }
}
