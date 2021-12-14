package com.pennypop.project.GUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.pennypop.project.Assets;
import com.pennypop.project.Config;
import com.pennypop.project.ProjectApplication;

public class MainScreen implements Screen {

    public static Stage stage;
    public static Skin skin;
    private final SpriteBatch spriteBatch;
    public static Screen screen;

    public MainScreen(){
        MainScreen.screen = this;
        spriteBatch = new SpriteBatch();
        stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false, spriteBatch);
        initSkin();
        initScreenElements();
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
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.DARK_GRAY);
        skin.add("default", labelStyle);

        // Create label with color to be chosen later
        Label.LabelStyle coloredLabel = new Label.LabelStyle(font, Color.WHITE);
        skin.add("colored", coloredLabel);

        // Create Reset button style
        ImageButton.ImageButtonStyle resetButtonStyle = new ImageButton.ImageButtonStyle();
        resetButtonStyle.imageUp = new Image(Assets.manager.get(Assets.RESET_TXT, Texture.class)).getDrawable();
        resetButtonStyle.imageDown = new Image(Assets.manager.get(Assets.BTN_DOWN_TXT, Texture.class)).getDrawable();
        skin.add("resetButton", resetButtonStyle);

        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = font;
        textFieldStyle.background = new Image(Assets.manager.get(Assets.GREY_BACK,Texture.class)).getDrawable();
        Image cursor = new Image(Assets.manager.get(Assets.CURSOR,Texture.class));
        Drawable cursorD = cursor.getDrawable();
        cursorD.setMinWidth(15);
        textFieldStyle.cursor = cursorD;
        textFieldStyle.messageFont = font;
        textFieldStyle.selection = new Image(Assets.manager.get(Assets.SQUARE_TXT,Texture.class)).getDrawable();
        textFieldStyle.fontColor = Color.WHITE;
        textFieldStyle.disabledFontColor = Color.CYAN;
        textFieldStyle.messageFontColor = Color.RED;
        skin.add("default",textFieldStyle);

        // Create game button style
        ImageButton.ImageButtonStyle gameButtonStyle = new ImageButton.ImageButtonStyle();
        gameButtonStyle.imageUp = new Image(Assets.manager.get(Assets.PLAY, Texture.class)).getDrawable();
        gameButtonStyle.imageUp.setMinWidth(100);
        gameButtonStyle.imageUp.setMinHeight(100);
        gameButtonStyle.imageDown = new Image(Assets.manager.get(Assets.BTN_DOWN_TXT, Texture.class)).getDrawable();
        gameButtonStyle.imageDown.setMinWidth(100);
        gameButtonStyle.imageDown.setMinHeight(100);
        skin.add("gameButton", gameButtonStyle);

        SelectBox.SelectBoxStyle selectBoxStyle = new SelectBox.SelectBoxStyle();
        selectBoxStyle.font = font;
        selectBoxStyle.background = new Image(Assets.manager.get(Assets.GREY_BACK,Texture.class)).getDrawable();
        selectBoxStyle.background.setMinHeight(30);
        selectBoxStyle.background.setMinWidth(200);
        selectBoxStyle.fontColor = Color.BLACK;
        selectBoxStyle.listSelection = new Image(Assets.manager.get(Assets.GREY_BACK,Texture.class)).getDrawable();
        selectBoxStyle.backgroundOpen = new Image(Assets.manager.get(Assets.GREY_BACK,Texture.class)).getDrawable();
        selectBoxStyle.backgroundOver = new Image(Assets.manager.get(Assets.GREY_BACK,Texture.class)).getDrawable();
        selectBoxStyle.listBackground = new Image(Assets.manager.get(Assets.GREY_BACK,Texture.class)).getDrawable();
        selectBoxStyle.itemSpacing = 10;
        skin.add("default",selectBoxStyle);
    }

    private void initScreenElements(){
        Label widthLabel = new Label("Width", skin);
        widthLabel.setPosition(Gdx.graphics.getWidth() / 4 - widthLabel.getWidth() / 2,
                (float) (Gdx.graphics.getHeight() * 0.6));
        stage.addActor(widthLabel);

        TextField widthField = new TextField("7",skin);
        widthField.setWidth(300);
        widthField.setHeight(40);
        widthField.setPosition(Gdx.graphics.getWidth() / 4 + widthLabel.getWidth() / 2 + 40,
                (float) (Gdx.graphics.getHeight() * 0.6));
        stage.addActor(widthField);

        Label heightLabel = new Label("Height", skin);
        heightLabel.setPosition(Gdx.graphics.getWidth() / 4 - widthLabel.getWidth() / 2,
                (float) (Gdx.graphics.getHeight() * 0.5));
        stage.addActor(heightLabel);

        TextField heightField = new TextField("6",skin);
        heightField.setWidth(300);
        heightField.setHeight(40);
        heightField.setPosition(Gdx.graphics.getWidth() / 4 + widthLabel.getWidth() / 2 + 40,
                (float) (Gdx.graphics.getHeight() * 0.5));
        stage.addActor(heightField);

        Label depthLabel = new Label("Depth", skin);
        depthLabel.setPosition(Gdx.graphics.getWidth() / 4 - widthLabel.getWidth() / 2,
                (float) (Gdx.graphics.getHeight() * 0.4));
        stage.addActor(depthLabel);

        TextField depthField = new TextField("8",skin);
        depthField.setWidth(300);
        depthField.setHeight(40);
        depthField.setPosition(Gdx.graphics.getWidth() / 4 + widthLabel.getWidth() / 2 + 40,
                (float) (Gdx.graphics.getHeight() * 0.4));
        stage.addActor(depthField);


        SelectBox selectBox = new SelectBox(new Object[]{"Minimax with alpha-beta","Minimax without alpha-beta"},skin);
        selectBox.setPosition(Gdx.graphics.getWidth() / 4 + widthLabel.getWidth() / 2 - 40,
                (float) (Gdx.graphics.getHeight() * 0.3));
        stage.addActor(selectBox);


        ImageButton gameButton = new ImageButton(skin, "gameButton");
        gameButton.setPosition(Gdx.graphics.getWidth() / 4 + widthLabel.getWidth() / 2 + 100,
                (float) (Gdx.graphics.getHeight() * 0.1));
        gameButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                int width = Integer.parseInt(widthField.getText());
                int height = Integer.parseInt(heightField.getText());
                Config.maxDepth = Integer.parseInt(depthField.getText());
                boolean withPruning = selectBox.getSelectionIndex()==0;

                Screen gameScreen = new GameScreen(width,height,withPruning,skin);
                ProjectApplication.app.setScreen(gameScreen);
                return true;
            }
        });
        stage.addActor(gameButton);
    }
}
