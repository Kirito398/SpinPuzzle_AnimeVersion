package com.bg.spinpuzzleanime.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bg.spinpuzzleanime.SpinPuzzleAnime;

import java.util.Stack;
import java.util.logging.FileHandler;

public class GameStateManager {
    private Stack<State> states;

    public GameStateManager(){
        states = new Stack<State>();
        boolean data = Gdx.files.local("last.lvl").exists();
        if(!data){
            FileHandle file1 = Gdx.files.local("last.lvl");
            file1.writeString("1",false);
        }

        for(int i=1;i<=SpinPuzzleAnime.LVLNUM;i++){
            data = Gdx.files.local("data_" + i +".cl").exists();//clear
            if(!data){
                FileHandle file1 = Gdx.files.local("data_" + i +".cl");
                file1.writeString("0",false);
            }

            data = Gdx.files.local("data_" + i + ".sc").exists();//best score
            if(!data){
                FileHandle file1 = Gdx.files.local("data_" + i +".sc");
                file1.writeString("99999999",false);
            }

            data = Gdx.files.local("data_" + i + ".tm").exists();//best time
            if(!data){
                FileHandle file1 = Gdx.files.local("data_" + i +".tm");
                file1.writeString("99999999",false);
            }

            data = Gdx.files.local("data_" + i +".nm").exists();//clear
            if(!data){
                FileHandle file1 = Gdx.files.local("data_" + i +".nm");
                file1.writeString("0",false);
            }
        }
    }

    public void push(State state){
        states.push(state);
    }

    public void pop(){
        states.pop().dispose();
    }

    public void set(State state){
        states.pop().dispose();
        states.push(state);
    }

    public void render(SpriteBatch sb){
        states.peek().render(sb);
    }

    public void update(float dt){
        states.peek().update(dt);
    }
}
