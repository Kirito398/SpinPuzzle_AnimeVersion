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
import com.badlogic.gdx.math.Vector3;
import com.bg.spinpuzzleanime.SpinPuzzleAnime;

import static java.lang.Math.abs;

public class LevelState extends State {
    private BitmapFont font;
    private Texture image, prevImage, nextImage, empty;
    private float move;
    private int number, max;
    private boolean[] cleared, clearedName;
    private long[] bestScore, bestTime;

    public LevelState(final GameStateManager gsm){
        super(gsm);
        camera.setToOrtho(false, SpinPuzzleAnime.WIDTH, SpinPuzzleAnime.HEIGHT);
        max = SpinPuzzleAnime.LVLNUM;

        Gdx.input.setInputProcessor(new GestureDetector(new GestureDetector.GestureListener() {
            @Override
            public boolean touchDown(float x, float y, int pointer, int button) {
                return false;
            }

            @Override
            public boolean tap(float x, float y, int count, int button) {
                Vector3 tap = new Vector3(x,y,0);
                camera.unproject(tap);
                if(SpinPuzzleAnime.WIDTH/2 - image.getWidth()/2 < tap.x && tap.x < SpinPuzzleAnime.WIDTH/2 + image.getWidth()/2 && 900 < tap.y && tap.y < 1100){
                    gsm.set(new NameState(gsm,number,image));
                }else{
                    gsm.set(new PuzzleState(gsm,number));
                }
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
                move += deltaX;
                return false;
            }

            @Override
            public boolean panStop(float x, float y, int pointer, int button) {
                reset();
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

        font = new BitmapFont();
        generator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.characters = font_chars;
        parameter.size = 50;
        parameter.color = Color.WHITE;
        font = generator.generateFont(parameter);
        generator.dispose();

        cleared = new boolean[max+1];
        clearedName = new boolean[max+1];
        bestTime = new long[max+1];
        bestScore = new long[max+1];

        FileHandle file;
        String text;
        file = Gdx.files.local("last.lvl");
        text = file.readString();
        number = Integer.valueOf(text);
        for(int i=1;i<=max;i++){
            //Cleared
            file = Gdx.files.local("data_" + i +".cl");
            text = file.readString();
            cleared[i] = Boolean.valueOf(text);
            //ClearedName
            file = Gdx.files.local("data_" + i +".nm");
            text = file.readString();
            clearedName[i] = Boolean.valueOf(text);
            //Best score
            file = Gdx.files.local("data_" + i + ".sc");
            text = file.readString();
            bestScore[i] = Long.valueOf(text);
            //BestTime
            file = Gdx.files.local("data_" + i + ".tm");
            text = file.readString();
            bestTime[i] = Long.valueOf(text);
        }

        empty = new Texture("emptyImage.png");
        if(cleared[number])image = new Texture(number+".png"); else image = empty;
        if(number == max){
            nextImage = empty;
        }else{
            if(cleared[number + 1])nextImage = new Texture((number + 1)+".png"); else nextImage = empty;
        }
        if(number == 1) {
            prevImage = empty;
        }else{
            if(cleared[number-1])prevImage = new Texture((number-1)+".png"); else prevImage = empty;
        }
    }

    @Override
    protected void handleInput() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.BACK)){
            gsm.set(new MenuState(gsm));
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        if(move == 0) {
            if(clearedName[number])font.draw(sb, number + "." + names[number], SpinPuzzleAnime.WIDTH / 2 - (3 + names[number].length())*14, 1000); else font.draw(sb, number + ".???", SpinPuzzleAnime.WIDTH / 2 - 40, 1000);
            if(cleared[number]) {
                font.draw(sb, "Best Score: " + bestScore[number], SpinPuzzleAnime.WIDTH / 5, 110);
                font.draw(sb, "Best Time: " + bestTime[number], SpinPuzzleAnime.WIDTH / 5 * 3 + 50, 110);
            }else{
                font.draw(sb, "Best Score: ???", SpinPuzzleAnime.WIDTH / 5, 110);
                font.draw(sb, "Best Time: ???", SpinPuzzleAnime.WIDTH / 5 * 3 + 50, 110);
            }
        }
        sb.draw(image, SpinPuzzleAnime.WIDTH/2 - image.getWidth()/2 + move, SpinPuzzleAnime.HEIGHT/2 - image.getHeight()/2);
        if(number != max)sb.draw(nextImage, SpinPuzzleAnime.WIDTH/2 - nextImage.getWidth()/2 + SpinPuzzleAnime.WIDTH + move, SpinPuzzleAnime.HEIGHT/2 - nextImage.getHeight()/2);
        if(number != 1)sb.draw(prevImage, SpinPuzzleAnime.WIDTH/2 - prevImage.getWidth()/2 - SpinPuzzleAnime.WIDTH + move, SpinPuzzleAnime.HEIGHT/2 - prevImage.getHeight()/2);
        sb.end();
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    public void reset(){
        if(abs(move) > 800){
            if(move < 0){
                if(number != max) {
                    number++;
                    prevImage = image;
                    image = nextImage;
                    if(number != max) if(cleared[number + 1])nextImage = new Texture((number + 1) + ".png"); else nextImage = empty;
                }
            }else{
                if(number != 1) {
                    number--;
                    nextImage = image;
                    image = prevImage;
                    if(number != 1) if(cleared[number - 1])prevImage = new Texture((number - 1)+".png"); else prevImage = empty;
                }
            }
        }
        move = 0;
    }

    @Override
    public void create() {

    }

    @Override
    public void dispose() {
        font.dispose();
        prevImage.dispose();
        nextImage.dispose();
    }
}
