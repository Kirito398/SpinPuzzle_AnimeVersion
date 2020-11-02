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

import java.util.Random;

public class NameState extends State {
    private Texture image, cube, redCube, greenCube, silverCube, reload;
    private BitmapFont font, letterFont;
    private int number;
    private Vector3[] positions, letterPosition;
    private char[] name, settedName;
    private boolean[] setedLetters;
    private boolean clear = false;

    public NameState(final GameStateManager gsm, final int num, Texture img) {
        super(gsm);
        camera.setToOrtho(false, SpinPuzzleAnime.WIDTH, SpinPuzzleAnime.HEIGHT);
        number = num;

        Gdx.input.setInputProcessor(new GestureDetector(new GestureDetector.GestureListener() {
            @Override
            public boolean touchDown(float x, float y, int pointer, int button) {
                return false;
            }

            @Override
            public boolean tap(float x, float y, int count, int button) {
                if(clear){
                    FileHandle file;
                    file = Gdx.files.local("last.lvl");
                    file.writeString(String.valueOf(number),false);
                    file = Gdx.files.local("data_" + number +".nm");
                    file.writeString("true",false);
                    SpinPuzzleAnime.showInterstitial();
                    gsm.set(new LevelState(gsm));
                }
                Vector3 tap = new Vector3(x,y,0);
                camera.unproject(tap);
                for(int i=0;i<names[number].length();i++){
                    if(letterPosition[i].x < tap.x && tap.x < letterPosition[i].x + cube.getWidth()  && letterPosition[i].y < tap.y && tap.y < letterPosition[i].y + cube.getHeight()){
                        if(!setedLetters[i]) {
                            for (int j = 0; j < names[number].length(); j++) {
                                if (settedName[j] == ' ') {
                                    settedName[j] = name[i];
                                    setedLetters[i] = true;
                                    break;
                                }
                            }
                        }
                    }
                }

                for(int i=0;i<names[number].length();i++){
                    if(positions[i].x < tap.x && tap.x < positions[i].x + cube.getWidth()  && positions[i].y < tap.y && tap.y < positions[i].y + cube.getHeight()){
                        if(settedName[i] != ' '){
                            for(int j=0;j<names[number].length();j++) {
                                if (name[j] == settedName[i] && setedLetters[j]) {
                                    settedName[i] = ' ';
                                    setedLetters[j] = false;
                                    break;
                                }
                            }
                        }
                    }
                }

                if(50 < tap.x && tap.x < 50 + reload.getWidth() && 920 < tap.y && tap.y < 920 + reload.getHeight()){
                    gsm.set(new NameState(gsm, number, new Texture(image.getTextureData())));
                }

                boolean f = true;
                for(int i=0;i<names[number].length();i++){
                    if(settedName[i] == ' '){
                        f = false;
                    }
                }

                if(f) if(isClear()){
                    cube = greenCube;
                    clear = true;
                } else cube = redCube; else cube = silverCube;

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

        font = new BitmapFont();
        letterFont = new BitmapFont();
        generator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.characters = font_chars;
        parameter.size = 50;
        parameter.color = Color.WHITE;
        font = generator.generateFont(parameter);
        parameter.color = Color.BLACK;
        parameter.size = 60;
        letterFont = generator.generateFont(parameter);
        generator.dispose();

        image = img;
        silverCube = new Texture("cube.png");
        greenCube = new Texture("green_cube.png");
        redCube = new Texture("red_cube.png");
        reload = new Texture("reload.png");
        cube = silverCube;

        int n = names[num].length();
        int z = n/2;
        float x = SpinPuzzleAnime.WIDTH/2 - z * 80;
        positions = new Vector3[n];
        letterPosition = new Vector3[n];
        for(int i=0;i<n;i++){
            positions[i] = new Vector3(x,280,0);
            letterPosition[i] = new Vector3(x, 100,0);
            x+=78;
        }

        Random random = new Random();
        String str = names[num];
        name = str.toCharArray();
        for(int i=0;i<n;i++){
            int j = random.nextInt(n);
            char c = name[i];
            name[i] = name[j];
            name[j] = c;
        }

        String chars = "abcdefghijklmnopqrstuvwxyz";
        for(int i=0;i<n;i++){
            if(name[i] == ' '){
                name[i] = chars.charAt(random.nextInt(chars.length()));
            }
        }

        setedLetters = new boolean[n];
        for(int i=0;i<n;i++){
            setedLetters[i] = false;
        }

        settedName = new char[n];
        for(int i=0;i<n;i++){
            if(str.charAt(i) == ' '){
                settedName[i] = '#';
            }else{
                settedName[i] = ' ';
            }
        }

        SpinPuzzleAnime.loadAd();
    }

    @Override
    protected void handleInput() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.BACK)){
            FileHandle file;
            file = Gdx.files.local("last.lvl");
            file.writeString(String.valueOf(number),false);
            SpinPuzzleAnime.showInterstitial();
            gsm.set(new LevelState(gsm));
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        sb.draw(image, SpinPuzzleAnime.WIDTH/2 - 512, SpinPuzzleAnime.HEIGHT/2 - 150, 1024, 576);
        if(!clear)font.draw(sb, answers[number], SpinPuzzleAnime.WIDTH/2 - answers[number].length()*14,1030); else font.draw(sb,"Clear!", SpinPuzzleAnime.WIDTH/2 - 75, 1050);
        for(int i=0;i<names[number].length();i++) {
            if(names[number].charAt(i) != ' '){
                sb.draw(cube, positions[i].x, positions[i].y);
                letterFont.draw(sb, "" + settedName[i], positions[i].x + cube.getWidth() / 3 - 5, positions[i].y + cube.getHeight() - 18);
            }
            if(!clear){
                if(!setedLetters[i]) {
                    sb.draw(cube, letterPosition[i].x, letterPosition[i].y);
                    letterFont.draw(sb, "" + name[i], letterPosition[i].x + cube.getWidth() / 3 - 5, letterPosition[i].y + cube.getHeight() - 18);
                }
            }else font .draw(sb, "Touch to continue", SpinPuzzleAnime.WIDTH/2 - 238, 150);
        }
        sb.draw(reload, 50, 920, 128,128);
        sb.end();
    }

    public boolean isClear(){
        for(int i=0;i<names[number].length();i++){
            if(settedName[i] != names[number].charAt(i)){
                if(settedName[i] != '#' && names[number].charAt(i) != ' ') {
                    return false;
                }
            }
        }
        return true;
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
        image.dispose();
        cube.dispose();
        redCube.dispose();
        greenCube.dispose();
        silverCube.dispose();
        font.dispose();
        letterFont.dispose();
    }
}
