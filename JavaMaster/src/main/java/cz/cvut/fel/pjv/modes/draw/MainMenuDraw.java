package cz.cvut.fel.pjv.modes.draw;

import cz.cvut.fel.pjv.modes.Mode;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/** @see Draw.java */
public class MainMenuDraw extends Draw {
  public MainMenuDraw(GraphicsContext gc, Mode parent) {
    super(gc, parent);
    redraw();
  }

  public void redraw() {
    Image temp = new Image("/sprites/monster/TEMP.png");
    gc.drawImage(temp, 40, 40);
    gc.fillText("Hello Jadernak! Press ESCAPE to load dungeon!", 250, 200);
  }
}

