package cz.cvut.fel.pjv.modes.draw;

import cz.cvut.fel.pjv.modes.MainMenu;
import cz.cvut.fel.pjv.menu.Layout;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/** @see Draw.java */
public class MainMenuDraw extends Draw {
  private MainMenu parent;

  public MainMenuDraw(GraphicsContext gc, MainMenu parent) {
    super(gc);
    this.parent = parent;
    redraw();
  }

  public void redraw() {
    gc.clearRect(0, 0, 1000, 525);
    Image temp = new Image("/sprites/monster/TEMP.png");
    gc.drawImage(temp, 600, 150);
    gc.fillText("Hello Jadernak!", 600, 100);
    // Menu
    Integer active = this.parent.getMenuActive();
    for (int i = 0; i < this.parent.getMenuCount(); i++) {
      if (active == i) {
        gc.fillText(">", 25, 40 + i*40);
      }
      gc.fillText(this.parent.getMenuAction(i), 40, 40 + i*40);
    }
  }
}

