package com.pennypop.project;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.graphics.GL20;
import com.pennypop.project.GUI.GameScreen;
import com.pennypop.project.controller.heurstics.WeightedPlacesHeurstic;
//import com.pennypop.project.controller.heurstics.zayady;

/**
 * The {@link ApplicationListener} for this project, create(), resize() and
 * render() are the only methods that are relevant
 * 
 * @author Richard Taylor, Kevin Chen
 */
public class ProjectApplication extends Game {

	private Screen screen;
	public static ProjectApplication app;

	public static void main(String[] args) {
//		char[][] zoz = {
//				{'0','0','0','0'},
//				{'0','2','0','0'},
//				{'0','0','0','0'},
//				{'0','0','0','0'}
//		};
//		System.out.println(new zayady().evaluate(zoz));
//		new WeightedPlacesHeurstic(6,7);
		new LwjglApplication(new ProjectApplication(), "Connect4", 800, 680, true);
	}

	@Override
	public void create() {
		app = this;
		Assets.load();
		Assets.manager.finishLoading();
		screen = new GameScreen();
		setScreen(screen);
	}

	@Override
	public void dispose() {
		screen.hide();
		screen.dispose();
		Assets.dispose();
	}

	@Override
	public void pause() {
		screen.pause();
	}

	@Override
	public void render() {
		clearWhite();
		super.render();
		// screen.render(Gdx.graphics.getDeltaTime());
	}

	/** Clears the screen with a white color */
	private void clearWhite() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}

	@Override
	public void resize(int width, int height) {
		screen.resize(width, height);
	}

	@Override
	public void resume() {
		screen.resume();
	}
}
