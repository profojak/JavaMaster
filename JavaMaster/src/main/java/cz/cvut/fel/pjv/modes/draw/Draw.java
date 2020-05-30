package cz.cvut.fel.pjv.modes.draw;

import cz.cvut.fel.pjv.Const;
import cz.cvut.fel.pjv.modes.Mode;

import javafx.scene.layout.StackPane;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.image.Image;

/**
 * Abstract class implementing basic functionality of draw classes.
 *
 * @author profojak
 */
public abstract class Draw {
  // Button images
  protected final Image IMAGE_BUTTON = new Image(Const.BUTTON),
    IMAGE_BUTTON_ACTIVE = new Image(Const.BUTTON_ACTIVE);

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
  abstract public void redraw(Const.State state);

  /**
   * Sets font to draw with in GraphicsContext.
   *
   * @author profojak
   */
  protected void setGC() {
    this.gc.setFont(Font.loadFont(getClass().getResourceAsStream(Const.FONT), 50));
    this.gc.setStroke(Color.web(Const.COLOR_STROKE));
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

