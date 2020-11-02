package com.bg.spinpuzzleanime;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bg.spinpuzzleanime.States.GameStateManager;
import com.bg.spinpuzzleanime.States.MenuState;

public class SpinPuzzleAnime extends ApplicationAdapter {
	public static final int WIDTH = 1920;
	public static final int HEIGHT = 1080;
	public static final int LVLNUM = 6; // Добавь в State режимы

	public static IActivityRequestHandler application;

	SpriteBatch batch;
	private GameStateManager gsm;

	public SpinPuzzleAnime(IActivityRequestHandler app){
		application = app;
	}

	public static void showInterstitial(){
	    application.showInterstitial();
    }

    public static void loadAd(){
		application.loadAd();
	}
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		Gdx.gl.glClearColor(0, 0, 0, 1);

		gsm = new GameStateManager();
		gsm.push(new MenuState(gsm));
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gsm.render(batch);
		gsm.update(Gdx.graphics.getDeltaTime());
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
