package cz.cvut.fel.pjv.modes.draw;

import cz.cvut.fel.pjv.modes.Game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;

/** @see Draw.java */
public class GameDraw extends Draw {
  private Game parent;
  private Integer roomId;

  /**
   * @param gc - GraphicsContext to draw images to
   * @param mode - Mode from which to get informaiton to draw
   *
   */
  public GameDraw(GraphicsContext gc, Game parent) {
    super(gc);
    this.parent = parent;
    redraw("set");
  }

  /**
   * @deprecated use GameDraw(GraphicsContext, Mode) contstructor instead
   */
  @Deprecated
  public GameDraw() {
  }

  /**
   * Redraws game.
   *
   * @param part - which part of a window to redraw
   * @author profojak
   */
  public void redraw(String part) {
    Image image;
    switch (part) {
      /** Draw when GameDraw is created */
      case "set":
        // Map
        image = new Image("/sprites/map/visited_false.png");
        for (int i = 0; i < 5; i++) {
          for (int j = 0; j < 7; j++) {
            gc.drawImage(image, i*75, j*75);
          }
        }
        roomId = parent.getRoomId();
        image = new Image("/sprites/map/visited_true.png");
        gc.drawImage(image, (roomId % 5) * 75, (roomId / 5) * 75);
        image = new Image("/sprites/map/arrow_" + parent.getDirection() + ".png");
        gc.drawImage(image, (roomId % 5) * 75, (roomId / 5) * 75);
        // HP bars
        gc.setFill(Color.web("#504945"));
        gc.fillRect(375, 10, 525, 35);
        gc.fillRect(375, 480, 525, 35);
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
        // Inventory
        gc.setFill(Color.web("#665C54"));
        gc.fillRect(905, 10, 85, 505);
        image = new Image("/sprites/inventory/frame_item.png");
        gc.drawImage(image, 910, 15);
        gc.drawImage(image, 910, 125);
        image = new Image("/sprites/inventory/frame_weapon.png");
        gc.drawImage(image, 910, 235);
        break;
      /** Redraw room */
      case "room":
        // Map
        Integer roomIdOld = roomId;
        roomId = parent.getRoomId();
        image = new Image("/sprites/map/visited_true.png");
        gc.drawImage(image, (roomId % 5) * 75, (roomId / 5) * 75);
        gc.drawImage(image, (roomIdOld % 5) * 75, (roomIdOld / 5) * 75);
        image = new Image("/sprites/map/arrow_" + parent.getDirection() + ".png");
        gc.drawImage(image, (roomId % 5) * 75, (roomId / 5) * 75);
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
        break;
      /** Redraw inventory */
      case "inventory":
        break;
    }
    // Overlay
    image = new Image("/sprites/overlay/game.png");
    gc.drawImage(image, 0, 0);
  }
}

