package cz.cvut.fel.pjv.modes.draw;

import cz.cvut.fel.pjv.modes.Game;

import javafx.scene.layout.StackPane;
import javafx.scene.canvas.*;
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
  private final int MAP_WIDTH = 5, MAP_LENGTH = 7;
  private final String MAP_VISITED_FALSE = "/sprites/map/visited_false.png",
    MAP_VISITED_TRUE = "/sprites/map/visited_true.png", MAP_ARROW = "/sprites/map/arrow_",
    INVENTORY_FRAME_ITEM = "/sprites/inventory/frame_item.png",
    INVENTORY_FRAME_WEAPON = "/sprites/inventory/frame_weapon.png",
    MONSTER = "/sprites/monster/", OVERLAY = "/sprites/overlay/game.png",
    PNG_EXTENSION = ".png", ROOM_RIGHT = "/sprites/room/right.png",
    ROOM_BG = "/sprites/room/bg.png", ROOM_LEFT = "/sprites/room/left.png",
    ROOM_FRONT = "/sprites/room/front/", RED = "\u001B[31m", RESET = "\u001B[0m";
  private static final Logger logger = Logger.getLogger(GameDraw.class.getName());
  private final Game parent;

  private Integer roomId;
  private GraphicsContext gc;


  /**
   * @param stack - StackPane to draw images to
   * @param parent - Mode from which to get information to draw
   *
   */
  public GameDraw(StackPane stack, Game parent) {
    super(stack);
    this.parent = parent;

    // GUI setup
    this.stack.getChildren().clear();
    Canvas canvas = new Canvas(1000, 525);
    this.gc = canvas.getGraphicsContext2D();
    this.stack.getChildren().add(canvas);

    // Undiscovered map
    this.roomId = parent.getRoomId();
    Image image = new Image(MAP_VISITED_FALSE);
    for (int i = 0; i < MAP_WIDTH; i++) {
      for (int j = 0; j < MAP_LENGTH; j++) {
        gc.drawImage(image, i*75, j*75);
      }
    }

    redraw(State.DEFAULT);
  }

  /**
   * @deprecated use GameDraw(GraphicsContext, Mode) constructor instead
   */
  @Deprecated
  public GameDraw() {
    this.parent = null;
  }

  /**
   * @see Draw
   * @author profojak
   */
  // TODO
  public void redraw(State state) {
    Image image;
    /*try {
      part = Part.valueOf(partString);
    } catch (Exception exception) {
      logger.log(Level.SEVERE, RED + ">>>  Error: Unexpected part value: " + part + RESET,
        exception); // ERROR
      return;
    }*/
    switch (state) {
      /* Draw when GameDraw is created */
      case DEFAULT:
        /* Map */
        // Tile player was in before
        image = new Image(MAP_VISITED_TRUE);
        gc.drawImage(image, (roomId % 5) * 75, (roomId / 5) * 75);
        // Current tile
        roomId = parent.getRoomId();
        gc.drawImage(image, (roomId % 5) * 75, (roomId / 5) * 75);
        image = new Image(MAP_ARROW + parent.getDirection() + PNG_EXTENSION);
        gc.drawImage(image, (roomId % 5) * 75, (roomId / 5) * 75);

        /* HP bars */
        gc.setFill(Color.web("#504945"));
        gc.fillRect(375, 10, 525, 35);
        gc.fillRect(375, 480, 525, 35);

        /* Room */
        // Background
        image = new Image(ROOM_BG);
        gc.drawImage(image, 375, 50);
        // Front wall
        if (!parent.hasRoomFront()) {
          if (parent.getRoomSprite() != null) {
            image = new Image(ROOM_FRONT);
          } else {
            image = new Image(ROOM_FRONT + "default.png");
          }
          gc.drawImage(image, 450, 50);
        }
        // Left wall
        if (!parent.hasRoomLeft()) {
          image = new Image(ROOM_LEFT);
          gc.drawImage(image, 375, 50);
        }
        // Right wall
        if (!parent.hasRoomRight()) {
          image = new Image(ROOM_RIGHT);
          gc.drawImage(image, 825, 50);
        }

        /* Inventory */
        gc.setFill(Color.web("#665C54"));
        gc.fillRect(905, 10, 85, 505);
        // Item frame
        image = new Image(INVENTORY_FRAME_ITEM);
        gc.drawImage(image, 910, 15);
        gc.drawImage(image, 910, 125);
        // Weapon frame
        image = new Image(INVENTORY_FRAME_WEAPON);
        gc.drawImage(image, 910, 235);
        break;
      /*case ROOM:
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
      case INVENTORY:
        break;
      case MONSTER:
        image = new Image(MONSTER_TEMP);
        gc.drawImage(image, 600, 150);
        break;*/
    }
    // Overlay
    image = new Image(OVERLAY);
    gc.drawImage(image, 0, 0);
  }

  /** @see Draw */
  public void close() {
  }
}

