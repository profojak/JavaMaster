package cz.cvut.fel.pjv.modes.draw;

import cz.cvut.fel.pjv.modes.Game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of GameDraw: Draw object that handles drawing of Game mode.
 *
 * @see Draw
 */
public class GameDraw extends Draw {
  public enum Part {
    SET,
    ROOM,
    INVENTORY,
    MONSTER
  }

  private final int MAP_WIDTH = 5, MAP_LENGTH = 7;
  private final String MAP_VISITED_FALSE = "/sprites/map/visited_false.png",
    MAP_VISITED_TRUE = "/sprites/map/visited_true.png", MAP_ARROW = "/sprites/map/arrow_",
    PNG_EXTENSION = ".png", ROOM_BACKGROUND = "/sprites/room/default_bg.png",
    ROOM_FRONT_WALL = "/sprites/room/default_front.png",
    ROOM_LEFT_WALL = "/sprites/room/default_left.png",
    ROOM_RIGHT_WALL = "/sprites/room/default_right.png",
    INVENTORY_FRAME_ITEM = "/sprites/inventory/frame_item.png",
    INVENTORY_FRAME_WEAPON = "/sprites/inventory/frame_weapon.png",
    MONSTER_TEMP = "/sprites/monster/TEMP.png", OVERLAY_GAME = "/sprites/overlay/game.png",
    PART_SET = "SET", RED = "\u001B[31m", RESET = "\u001B[0m";;
  private static final Logger logger = Logger.getLogger(GameDraw.class.getName());
  private final Game parent;

  private Integer roomId;
  private Part part;


  /**
   * @param gc - GraphicsContext to draw images to
   * @param parent - Mode from which to get information to draw
   *
   */
  public GameDraw(GraphicsContext gc, Game parent) {
    super(gc);
    this.parent = parent;
    redraw(PART_SET);
  }

  /**
   * @deprecated use GameDraw(GraphicsContext, Mode) constructor instead
   */
  @Deprecated
  public GameDraw() {
  }

  /**
   * Redraws game.
   *
   * @param partString - which part of a window to redraw
   * @author profojak
   */
  // TODO
  @Override
  public void redraw(String partString) {
    Image image;
    try {
      part = Part.valueOf(partString);
    } catch (Exception exception) {
      logger.log(Level.SEVERE, RED + ">>>  Error: Unexpected part value: " + part + RESET,
        exception); // ERROR
      return;
    }
    switch (part) {
      /* Draw when GameDraw is created */
      case SET:
        // Map
        image = new Image(MAP_VISITED_FALSE);
        for (int i = 0; i < MAP_WIDTH; i++) {
          for (int j = 0; j < MAP_LENGTH; j++) {
            gc.drawImage(image, i*75, j*75);
          }
        }
        roomId = parent.getRoomId();
        image = new Image(MAP_VISITED_TRUE);
        gc.drawImage(image, (roomId % 5) * 75, (roomId / 5) * 75);
        image = new Image(MAP_ARROW + parent.getDirection() + PNG_EXTENSION);
        gc.drawImage(image, (roomId % 5) * 75, (roomId / 5) * 75);
        // HP bars
        gc.setFill(Color.web("#504945"));
        gc.fillRect(375, 10, 525, 35);
        gc.fillRect(375, 480, 525, 35);
        // Room
        image = new Image(ROOM_BACKGROUND);
        gc.drawImage(image, 375, 50);
        if (!parent.hasRoomFront()) {
          image = new Image(ROOM_FRONT_WALL);
          gc.drawImage(image, 450, 50);
        }
        if (!parent.hasRoomLeft()) {
          image = new Image(ROOM_LEFT_WALL);
          gc.drawImage(image, 375, 50);
        }
        if (!parent.hasRoomRight()) {
          image = new Image(ROOM_RIGHT_WALL);
          gc.drawImage(image, 825, 50);
        }
        // Inventory
        gc.setFill(Color.web("#665C54"));
        gc.fillRect(905, 10, 85, 505);
        image = new Image(INVENTORY_FRAME_ITEM);
        gc.drawImage(image, 910, 15);
        gc.drawImage(image, 910, 125);
        image = new Image(INVENTORY_FRAME_WEAPON);
        gc.drawImage(image, 910, 235);
        break;
      /* Redraw room */
      case ROOM:
        // Map
        Integer roomIdOld = roomId;
        roomId = parent.getRoomId();
        image = new Image(MAP_VISITED_TRUE);
        gc.drawImage(image, (roomId % 5) * 75, (roomId / 5) * 75);
        gc.drawImage(image, (roomIdOld % 5) * 75, (roomIdOld / 5) * 75);
        image = new Image(MAP_ARROW + parent.getDirection() + PNG_EXTENSION);
        gc.drawImage(image, (roomId % 5) * 75, (roomId / 5) * 75);
        // Room
        image = new Image(ROOM_BACKGROUND);
        gc.drawImage(image, 375, 50);
        if (!parent.hasRoomFront()) {
          image = new Image(ROOM_FRONT_WALL);
          gc.drawImage(image, 450, 50);
        }
        if (!parent.hasRoomLeft()) {
          image = new Image(ROOM_LEFT_WALL);
          gc.drawImage(image, 375, 50);
        }
        if (!parent.hasRoomRight()) {
          image = new Image(ROOM_RIGHT_WALL);
          gc.drawImage(image, 825, 50);
        }
        break;
      /* Redraw inventory */
      case INVENTORY:
        break;
      /* Redraw monster */
      case MONSTER:
        image = new Image(MONSTER_TEMP);
        gc.drawImage(image, 600, 150);
        break;
    }
    // Overlay
    image = new Image(OVERLAY_GAME);
    gc.drawImage(image, 0, 0);
  }
}

