package cz.cvut.fel.pjv.modes.draw;

import cz.cvut.fel.pjv.modes.Mode;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/** @see Draw.java */
public class GameDraw extends Draw {
  public GameDraw(GraphicsContext gc, Mode parent) {
    super(gc, parent);
    redraw();
  }

  /**
   * @deprecated use GameDraw(GraphicsContext, Mode) contstructor instead
   */
  @Deprecated
  public GameDraw() {
  }

  public void redraw() {
    gc.clearRect(0, 0, 1000, 525);
    gc.fillText("WELCOME TO THE JAVA MASTER: THE LEGEND OF SEGFAULT! LET'S ROLL!!!", 50, 250);
  }
}
