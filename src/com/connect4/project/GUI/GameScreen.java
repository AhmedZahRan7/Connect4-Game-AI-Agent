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
    private final int width;
    private final int height;
    private final boolean withPruning;
    public GameScreen(int width,int height,boolean withPruning,Skin skin) {
        GameScreen.screen = this;
        this.width = width;
        this.height = height;
        this.withPruning = withPruning;
        GameScreen.skin = skin;
        spriteBatch = new SpriteBatch();
        stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true, spriteBatch);
        Board board = new Board(this.width,this.height);
        game = new GameLogic(board,this.withPruning, this);
    }

    public void showResult(Integer playerScore, Integer AIScore) {
        Label win;
        win = new Label("player score : " + playerScore.toString() + " AI score : " + AIScore.toString(), skin, "colored");
        win.setColor(Color.DARK_GRAY);

        win.setPosition((int) (Gdx.graphics.getWidth() / 2) - (int) (win.getWidth() / 2), (int) (Gdx.graphics.getHeight() / 2));
        GameScreen.stage.addActor(win);

        // Add 'Reset' button and listener
        ImageButton resetButton = new ImageButton(GameScreen.skin, "resetButton");
        resetButton.setPosition(win.getX() + 50, (int) (Gdx.graphics.getHeight() / 2) - 100);
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
        ProjectApplication.app.setScreen(new GameScreen(this.width,this.height,this.withPruning,GameScreen.skin));
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
        stage.setViewport(width, height, true);
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
