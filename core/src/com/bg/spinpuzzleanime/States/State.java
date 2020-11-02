package com.bg.spinpuzzleanime.States;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector3;



public abstract class State extends Game {
    protected OrthographicCamera camera;
    protected Vector3 mouse;
    protected GameStateManager gsm;
    protected int[] modes;
    protected String[] names;
    protected String[] answers;

    protected final String font_chars = "абвгдежзийклмнопрстуфхцчшщъыьэюяabcdefghijklmnopqrstuvwxyzАБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;:,{}\"´`'<>";
    protected FreeTypeFontGenerator generator;
    protected FreeTypeFontGenerator.FreeTypeFontParameter parameter;

    public State(GameStateManager gsm){
        this.gsm = gsm;
        camera = new OrthographicCamera();
        mouse = new Vector3();
        modes = new int[]{0,2,2,3,3,3,4};//Сюда и имена не забудь!
        names = new String[]{"", "mekakucity actors", "shigatsu wa kimi no uso", "naruto", "monogatari", "tokyo ghoul", "fairy tail"};
        answers = new String[]{"", "What is the name of this anime?", "What is the name of this anime?", "What is the name of this anime?", "What is the name of this anime?", "What is the name of this anime?", "What is the name of this anime?"};
    }

    protected abstract void handleInput();
    public abstract void render(SpriteBatch sb);
    public abstract void update(float dt);
    public abstract void dispose();
}
