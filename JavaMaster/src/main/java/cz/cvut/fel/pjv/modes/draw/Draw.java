package cz.cvut.fel.pjv.modes.draw;

import cz.cvut.fel.pjv.modes.Mode;

import javafx.scene.layout.StackPane;
import javafx.scene.image.Image;

/**
 * Abstract class Draw implementing basic functionality of draw classes.
 */
public abstract class Draw {
  public static enum State {
    DEFAULT,
    COMBAT,
    LOOT,
    DIALOG_FIRST,
    DIALOG_SECOND
  }

  protected StackPane stack;

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
   * Properly closes Draw object.
   *
   * <p>This can be used for example to stop all running threads.
   */
  abstract public void close();
}

