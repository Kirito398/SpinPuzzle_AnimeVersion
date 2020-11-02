package com.bg.spinpuzzleanime.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;
import com.bg.spinpuzzleanime.SpinPuzzleAnime;

import java.sql.Time;
import java.util.Random;

import static java.lang.Math.abs;

public class PuzzleState extends State {
    private BitmapFont font;
    private Texture image;
    private TextureRegion[][] imageRegions;
    private float[][] imagePositionX, imagePositionY, defaultPositionX, defaultPositionY;
    private int N = 3, p, number;
    private float moveX = 0, moveY = 0, px, py;
    private int[] position;
    private long score = 0, startTime, time;

    public PuzzleState(GameStateManager gsm, int num) {
        super(gsm);
        camera.setToOrtho(false, SpinPuzzleAnime.WIDTH, SpinPuzzleAnime.HEIGHT);
        number = num;
        N = modes[number];

        Gdx.input.setInputProcessor(new GestureDetector(new GestureDetector.GestureListener() {
            @Override
            public boolean touchDown(float x, float y, int pointer, int button) {
                px = x;
                py = y;
                return false;
            }

            @Override
            public boolean tap(float x, float y, int count, int button) {
                return false;
            }

            @Override
            public boolean longPress(float x, float y) {
                return false;
            }

            @Override
            public boolean fling(float velocityX, float velocityY, int button) {
                //str = velocityX + " " + velocityY + " " + button;
                return false;
            }

            @Override
            public boolean pan(float x, float y, float deltaX, float deltaY) {
                //str = x + " " + y + " " + deltaX + " " + deltaY;
                Vector3 pos = new Vector3(px,py,0);
                camera.unproject(pos);

                if(abs(deltaX) > abs(deltaY) && abs(moveX) >= abs(moveY)) {
                    if(moveY == 0) {
                        for (int j = 0; j < N; j++) {
                            if (pos.y > imagePositionY[0][j] && pos.y < imagePositionY[0][j] + imageRegions[0][j].getRegionHeight()) {
                                for (int i = 0; i < N; i++) {
                                    imagePositionX[i][j] += deltaX;
                                    moveX += deltaX;
                                    p = j;
                                }
                            }
                        }
                    }else{
                        reset();
                        px = x;
                        py = y;
                    }
                }

                if(abs(deltaY) > abs(deltaX) && abs(moveY) >= abs(moveX)) {
                    if(moveX == 0) {
                        for (int i = 0; i < N; i++) {
                            if (pos.x > imagePositionX[i][0] && pos.x < imagePositionX[i][0] + imageRegions[i][0].getRegionWidth()) {
                                for (int j = 0; j < N; j++) {
                                    imagePositionY[i][j] -= deltaY;
                                    moveY -= deltaY;
                                    p = i;
                                }
                            }
                        }
                    }else{
                        reset();
                        px = x;
                        py = y;
                    }
                }
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
        image = new Texture(number+".png");
        imageRegions = new TextureRegion[N][N];
        imagePositionX = new float[N][N];
        imagePositionY = new float[N][N];
        defaultPositionX = new float[N][N];
        defaultPositionY = new float[N][N];
        position = new int[N*N];

        generator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.characters = font_chars;
        parameter.size = 50;
        parameter.color = Color.WHITE;
        font = generator.generateFont(parameter);
        generator.dispose();

        float x = SpinPuzzleAnime.WIDTH/2 - image.getWidth()/2;
        float y = SpinPuzzleAnime.HEIGHT/2 - image.getHeight()/2;
        for(int i=0;i<N;i++){
            for(int j=0;j<N;j++){
                position[i*N+j] = i*N+j;
                imagePositionX[i][j] = x + image.getWidth()/N * i;
                imagePositionY[i][j] = y + image.getHeight()/N * j;
                defaultPositionX[i][j] = imagePositionX[i][j];
                defaultPositionY[i][j] = imagePositionY[i][j];
                imageRegions[i][j] = new TextureRegion(image, image.getWidth()/N * i, image.getHeight()/N * (N - j - 1), image.getWidth()/N, image.getHeight()/N);
            }
        }

        Random random = new Random();
        while(isClear()) {
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < random.nextInt(N) + 1; j++) {
                    upSlide(i);
                }
            }
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < random.nextInt(N) + 1; j++) {
                    leftSlide(i);
                }
            }
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < random.nextInt(N) + 1; j++) {
                    downSlide(i);
                }
            }
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < random.nextInt(N) + 1; j++) {
                    rightSlide(i);
                }
            }
        }


        startTime = TimeUtils.millis();

        SpinPuzzleAnime.loadAd();
    }

    @Override
    protected void handleInput() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.BACK)){
            FileHandle file;
            file = Gdx.files.local("last.lvl");
            file.writeString(String.valueOf(number),false);
            gsm.set(new LevelState(gsm));
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(camera.combined);
        sb.begin();

        for(int i=0;i<N;i++){
            for(int j=0;j<N;j++){
                sb.draw(imageRegions[i][j], imagePositionX[i][j], imagePositionY[i][j]);
            }
        }

        if(moveX != 0){
            for(int k=0;k<N;k++){
                sb.draw(imageRegions[k][p], imagePositionX[k][p] + imageRegions[k][p].getRegionWidth()*N, imagePositionY[k][p]);
                sb.draw(imageRegions[k][p], imagePositionX[k][p] - imageRegions[k][p].getRegionWidth()*N, imagePositionY[k][p]);
            }
        }

        if(moveY != 0){
            for(int k=0;k<N;k++){
                sb.draw(imageRegions[p][k], imagePositionX[p][k], imagePositionY[p][k] + imageRegions[p][k].getRegionHeight()*N);
                sb.draw(imageRegions[p][k], imagePositionX[p][k], imagePositionY[p][k] - imageRegions[p][k].getRegionHeight()*N);
            }
        }

        font.draw(sb,"Score: " + score, 60, 1000);
        font.draw(sb, "Time: " + time, 1650, 1000);

        sb.end();
    }

    public void reset(){
        if(abs(moveX) > image.getWidth()/2){
            int n = 0;
            score++;
            float x = abs(moveX), d = image.getWidth();
            while(x > d){
                n++;
                x -= d;
            }
            if(x > d/2) n++;
            for(int i=0;i<n;i++){
                if(moveX >= 0) rightSlide(p); else leftSlide(p);
            }
        }

        if(abs(moveY) > image.getHeight()/2){
            int n = 0;
            score++;
            float y = abs(moveY), d = image.getHeight();
            while(y > d){
                n++;
                y -= d;
            }
            if(y > d/2) n++;
            for(int i=0;i<n;i++){
                if(moveY >= 0) upSlide(p); else downSlide(p);
            }
        }

        for(int i=0;i<N;i++){
            for(int j=0;j<N;j++){
                imagePositionX[i][j] = defaultPositionX[i][j];
                imagePositionY[i][j] = defaultPositionY[i][j];
            }
        }
        moveX = 0;
        moveY = 0;

        if(isClear()){
            gsm.set(new ClearState(gsm,score,time,image,number));
        }
    }

    public void downSlide(int n){
        for(int i=1;i<N;i++){
            TextureRegion c = imageRegions[n][i-1];
            imageRegions[n][i-1] = imageRegions[n][i];
            imageRegions[n][i] = c;
            int d = position[n*N+(i-1)];
            position[n*N+(i-1)] = position[n*N+i];
            position[n*N+i] = d;
        }
    }

    public void upSlide(int n){
        for(int i=N-1;i>0;i--){
            TextureRegion c = imageRegions[n][i-1];
            imageRegions[n][i-1] = imageRegions[n][i];
            imageRegions[n][i] = c;
            int d = position[n*N+(i-1)];
            position[n*N+(i-1)] = position[n*N+i];
            position[n*N+i] = d;
        }
    }

    public void rightSlide(int n){
        for(int i=N-1;i>0;i--){
            TextureRegion c = imageRegions[i-1][n];
            imageRegions[i-1][n] = imageRegions[i][n];
            imageRegions[i][n] = c;
            int d = position[(i-1)*N+n];
            position[(i-1)*N+n] = position[i*N+n];
            position[i*N+n] = d;
        }
    }

    public void leftSlide(int n){
        for(int i=1;i<N;i++){
            TextureRegion c = imageRegions[i-1][n];
            imageRegions[i-1][n] = imageRegions[i][n];
            imageRegions[i][n] = c;
            int d = position[(i-1)*N+n];
            position[(i-1)*N+n] = position[i*N+n];
            position[i*N+n] = d;
        }
    }

    public boolean isClear(){
        for(int i = 0;i<N;i++){
            for(int j=0;j<N;j++){
                if(position[i*N+j] != i*N+j){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void update(float dt) {
        handleInput();
        time = TimeUtils.millis() - startTime;
        time /= 1000;
    }

    @Override
    public void create() {

    }

    @Override
    public void dispose() {
        font.dispose();
    }
}
