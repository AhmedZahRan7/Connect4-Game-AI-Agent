package com.pennypop.project;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Assets {
	public static final AssetManager manager = new AssetManager();

	public static final String SFX_TXT = "assets/sfxButton.png";
	public static final String API_TXT = "assets/apiButton.png";
	public static final String GAME_TXT = "assets/gameButton.png";
	public static final String BTN_DOWN_TXT = "assets/buttonDown.png";
	public static final String MENU_TXT = "assets/menu.png";
	public static final String RESET_TXT = "assets/reset.png";
	public static final String AI_TXT = "assets/AIGame.png";
	public static final String SQUARE_TXT = "assets/square.png";
	public static final String RED_TXT = "assets/red.png";
	public static final String YELLOW_TXT = "assets/yellow.png";
	public static final String CLICK_SOUND = "assets/button_click.wav";
	public static final String FONT = "assets/font.fnt";
	public static final String GREY_BACK = "assets/Grey.png";
	public static final String CURSOR = "assets/curser.png";
	public static final String PLAY = "assets/play.png";

	public static void load() {
		manager.load(SFX_TXT, Texture.class);
		manager.load(API_TXT, Texture.class);
		manager.load(GAME_TXT, Texture.class);
		manager.load(AI_TXT, Texture.class);
		manager.load(BTN_DOWN_TXT, Texture.class);
		manager.load(MENU_TXT, Texture.class);
		manager.load(RESET_TXT, Texture.class);
		manager.load(SQUARE_TXT, Texture.class);
		manager.load(RED_TXT, Texture.class);
		manager.load(YELLOW_TXT, Texture.class);
		manager.load(CLICK_SOUND, Sound.class);
		manager.load(FONT, BitmapFont.class);
		manager.load(CURSOR, Texture.class);
		manager.load(GREY_BACK, Texture.class);
		manager.load(PLAY, Texture.class);
	}

	public static void dispose() {
		manager.dispose();
	}

}
