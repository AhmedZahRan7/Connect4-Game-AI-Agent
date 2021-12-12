package com.pennypop.project.GUI.connect4board;

import com.badlogic.gdx.backends.openal.Wav.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.pennypop.project.Assets;
import com.pennypop.project.Config;
import com.pennypop.project.GUI.GameScreen;

public class Cell extends Image {
	public static int SIZE = 100;
	public static int MARGIN = 5;
	public static int OFFSET = 17;
	public int indX;
	public int indY;
	private final Stage stage;
	private final Image img;
	private Config.Player occupied = Config.Player.EMPTY; // 0 = none, -1 = red, 1 = yellow

	/**
	 * Constructor for Cell. Cell holds an image to represent itself on a grid,
	 * and some game information
	 * 
	 * @param indX
	 *            X index
	 * @param indY
	 *            Y index
	 * @param x
	 *            image X position
	 * @param y
	 *            image Y position
	 */
	public Cell(final int indX, final int indY, int x, int y) {
		this.stage = GameScreen.stage;
		this.indX = indX;
		this.indY = indY;

		this.img = new Image(Assets.manager.get(Assets.SQUARE_TXT, Texture.class));
		img.setX(x);
		img.setY(y);
		stage.addActor(img);
		img.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				// Call gameLogic to find where piece belongs and check game state
				GameScreen.game.placePiece(indX, indY);
				return true;
			}
		});
	}
	/**
	 * Sets a game piece in the Cell
	 * 
	 * @param color:
	 *            the color of the piece ( -1 = red, 1 = yellow )
	 */
	public void setCell(Config.Player color) {
		Sound sound = Assets.manager.get(Assets.CLICK_SOUND);
		sound.play();
		occupied = color;
		Image piece;
		if (color == Config.Player.PLAYER) {
			piece = new Image(Assets.manager.get(Assets.RED_TXT, Texture.class));
		} else {
			piece = new Image(Assets.manager.get(Assets.YELLOW_TXT, Texture.class));
		}
		piece.setPosition(img.getX() + OFFSET, img.getY() + OFFSET);
		stage.addActor(piece);
	}
	/**
	 * Checks which player occupies this Cell
	 * 
	 * @return integer between -1 and 1. -1 = red, 0 = empty, 1 = yellow
	 */
	public Config.Player getOccupied() {
		return occupied;
	}
}
