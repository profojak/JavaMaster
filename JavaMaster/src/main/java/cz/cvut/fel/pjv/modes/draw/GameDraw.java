package cz.cvut.fel.pjv.modes.draw;

import cz.cvut.fel.pjv.modes.Game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/** @see Draw.java */
public class GameDraw extends Draw {
  private Game parent;

  /**
   * @param gc - GraphicsContext to draw images to
   * @param mode - Mode from which to get informaiton to draw
   *
   */
  public GameDraw(GraphicsContext gc, Game parent) {
    super(gc);
    this.parent = parent;
    redraw();
  }

  /**
   * @deprecated use GameDraw(GraphicsContext, Mode) contstructor instead
   */
  @Deprecated
  public GameDraw() {
  }

  /**
   * Redraws game.
   */
  public void redraw() {
    gc.clearRect(0, 0, 1000, 525);
    // Map
    Image image = new Image("/sprites/map/bg.png");
    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 7; j++) {
        gc.drawImage(image, i*75, j*75);
      }
    }
    // HP bars
    image = new Image("/sprites/bar/bg.png");
    gc.drawImage(image, 375, 0);
    gc.drawImage(image, 375, 475);
    // Room
    image = new Image("/sprites/room/default_bg.png");
    gc.drawImage(image, 375, 50);
    if (!parent.hasRoomFront()) {
      image = new Image("/sprites/room/default_front.png");
      gc.drawImage(image, 450, 50);
    }
    if (!parent.hasRoomLeft()) {
      image = new Image("/sprites/room/default_left.png");
      gc.drawImage(image, 375, 50);
    }
    if (!parent.hasRoomRight()) {
      image = new Image("/sprites/room/default_right.png");
      gc.drawImage(image, 825, 50);
    }
    // Monster
    //image = new Image("/sprites/monster/TEMP.png");
    //gc.drawImage(image, 550, 160);
    // Inventory
    image = new Image("/sprites/inventory/loot_bg.png");
    gc.drawImage(image, 900, 0);
    gc.drawImage(image, 900, 100);
    image = new Image("/sprites/inventory/wep_bg.png");
    gc.drawImage(image, 900, 200);
    image = new Image("/sprites/inventory/dmg_bg.png");
    gc.drawImage(image, 900, 475);
  }
}

