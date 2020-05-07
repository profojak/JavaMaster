package cz.cvut.fel.pjv.modes.draw;

import cz.cvut.fel.pjv.modes.Mode;

import javafx.scene.layout.StackPane;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.image.Image;

/**
 * Abstract class Draw implementing basic functionality of draw classes.
 */
public abstract class Draw {
  public static enum State {
    DEFAULT,
    COMBAT,
    LOOT,
    STORY_BEFORE,
    STORY_AFTER,
    MENU
  }

  private final String FONT = "/silkscreen.ttf", STROKE_COLOR = "#282828";

  protected final Image BUTTON = new Image("/sprites/menu/button.png"),
    BUTTON_ACTIVE = new Image("/sprites/menu/active.png");
  protected final Integer TEXT_X_OFFSET = 138, TEXT_Y_OFFSET = 60, BUTTON_HEIGHT = 95;

  protected StackPane stack;
  protected GraphicsContext gc;

  /**
   * @param stack - StackPane to draw images to
   */
  public Draw(StackPane stack) {
    this.stack = stack;
  }

  /**
   * @deprecated use Draw(GraphicsContext) constructor instead
   */
  @Deprecated
  public Draw() {
  }

  /**
   * Redraws screen.
   *
   * @param state - current state of Mode object to draw
   */
  abstract public void redraw(State state);

  /**
   * Sets font to draw with.
   *
   * @author profojak
   */
  protected void setGC() {
    this.gc.setFont(Font.loadFont(getClass().getResourceAsStream(FONT), 50));
    this.gc.setStroke(Color.web(STROKE_COLOR));
    this.gc.setFontSmoothingType(null);
    this.gc.setLineWidth(10);
    this.gc.setTextAlign(TextAlignment.CENTER);
  }

  /**
   * Properly closes Draw object.
   *
   * <p>This can be used for example to stop all running threads.
   */
  abstract public void close();
}

