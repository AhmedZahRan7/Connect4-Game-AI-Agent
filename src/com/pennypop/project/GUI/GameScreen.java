package com.pennypop.project.GUI;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.pennypop.project.Assets;
import com.pennypop.project.Config;
import com.pennypop.project.GUI.connect4board.Board;
import com.pennypop.project.GUI.connect4board.Cell;
import com.pennypop.project.ProjectApplication;
import com.pennypop.project.controller.GameLogic;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;

public class GameScreen implements Screen {
	public static Stage stage;
	public static GameLogic game;
	public static Screen screen;
	public static Skin skin;
	private final SpriteBatch spriteBatch;
	private Board board;

	public GameScreen() {
		GameScreen.screen = this;
		spriteBatch = new SpriteBatch();
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false, spriteBatch);
		initSkin();
		this.board = new Board(7,6);
		game = new GameLogic(board,this);
	}

	public void showWinner(Config.Player player){
		Label win;
		if (player == Config.Player.PLAYER) {
			win = new Label("VICTORY RED", skin, "colored");
			win.setColor(Color.RED);
		} else if (player == Config.Player.AI) {
			win = new Label("VICTORY YELLOW", skin, "colored");
			win.setColor(Color.YELLOW);
		} else {
			win = new Label("STALE MATE", skin, "colored");
			win.setColor(Color.DARK_GRAY);
		}
		win.setPosition((int)(Gdx.graphics.getWidth()/2) - (int)(win.getWidth()/2),(int)(Gdx.graphics.getHeight()/2));
		GameScreen.stage.addActor(win);

		// Add 'Reset' button and listener
		ImageButton resetButton = new ImageButton(GameScreen.skin, "resetButton");
		resetButton.setPosition(win.getX() + 50, (int)(Gdx.graphics.getHeight() / 2) - 100);
		resetButton.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				reset();
				return true;
			}
		});
		GameScreen.stage.addActor(resetButton);

	}
	/**
	 * resets game
	 */
	public void reset() {
		GameScreen.screen.dispose();
		ProjectApplication.app.setScreen(new GameScreen());
	}
	/**
	 * Defines the styles for various UI Widgets and stores them in the Skin
	 * variable
	 */
	private void initSkin() {
		// Create a font
		BitmapFont font = Assets.manager.get(Assets.FONT);
		skin = new Skin();
		skin.add("default", font);

		// Create label style with default red color
		Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.RED);
		skin.add("default", labelStyle);

		// Create label with color to be chosen later
		Label.LabelStyle coloredLabel = new Label.LabelStyle(font, Color.WHITE);
		skin.add("colored", coloredLabel);

		// Create Reset button style
		ImageButton.ImageButtonStyle resetButtonStyle = new ImageButton.ImageButtonStyle();
		resetButtonStyle.imageUp = new Image(Assets.manager.get(Assets.RESET_TXT, Texture.class)).getDrawable();
		resetButtonStyle.imageDown = new Image(Assets.manager.get(Assets.BTN_DOWN_TXT, Texture.class)).getDrawable();
		skin.add("resetButton", resetButtonStyle);
	}

	@Override
	public void dispose() {
		spriteBatch.dispose();
		stage.dispose();
	}

	@Override
	public void render(float delta) {
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.setViewport(width, height, false);
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void pause() {
		// Irrelevant on desktop, ignore this
	}

	@Override
	public void resume() {
		// Irrelevant on desktop, ignore this
	}
}
