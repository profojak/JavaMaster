package cz.cvut.fel.pjv.modes.draw;

import cz.cvut.fel.pjv.modes.Mode;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

abstract public class Draw {
  protected GraphicsContext gc;

  /**
   * @param gc - GraphicsContext to draw images to
   * @param mode - Mode from which to get information to draw
   */
  public Draw(GraphicsContext gc) {
    this.gc = gc;
  }

  /**
   * @deprecated use Draw(GraphicsContext, Mode) constructor instead
   */
  @Deprecated
  public Draw() {
  }

  /** Redraws window. */
  abstract public void redraw();
}

