package cz.cvut.fel.pjv.modes.draw;

import cz.cvut.fel.pjv.Const;
import cz.cvut.fel.pjv.modes.Editor;

import javafx.scene.layout.StackPane;
import javafx.scene.canvas.*;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;

import java.util.logging.Level;
import java.util.logging.Logger;

public class EditorDraw extends Draw {
  private static final Logger logger = Logger.getLogger(EditorDraw.class.getName());
  private final Editor parent;
  private final Integer MENU_X = 500, MENU_Y = 125;
  private final String
    // Map
    MAP_TILE = "/sprites/map/tile.png",
    MAP_ARROW = "/sprites/map/arrow_NORTH.png", MAP_WALL = "/sprites/map/wall_",

    OVERLAY = "/sprites/overlay/game.png",

    // Keys
    KEY_UP = "/sprites/editor/key_up.png", KEY_DOWN = "/sprites/editor/key_down.png",
    KEY_LEFT = "/sprites/editor/key_left.png", KEY_RIGHT = "/sprites/editor/key_right.png",
    KEY_ENTER = "/sprites/editor/key_enter.png", KEY_ESCAPE = "/sprites/editor/key_escape.png",
    KEY_DELETE = "/sprites/editor/key_delete.png",

    // Room
    ROOM_BG = "/sprites/room/bg.png", ROOM_FRONT = "/sprites/room/front/",
    ROOM_DEFAULT = "default.png";

  private Integer roomId;

  public EditorDraw(StackPane stack, Editor parent) {
    super(stack);
    this.parent = parent;

    // GUI setup
    this.stack.getChildren().clear();
    Canvas canvas = new Canvas(Const.WINDOW_WIDTH, Const.WINDOW_HEIGHT);
    this.gc = canvas.getGraphicsContext2D();
    this.stack.getChildren().add(canvas);
    setGC();
  }

  @Deprecated
  public EditorDraw() {
    this.parent = null;
  }

  private void drawMap(Boolean drawUndiscoveredMap) {
    Image image;

    if (drawUndiscoveredMap) {
      this.roomId = parent.getRoomId();
      image = new Image(MAP_TILE);
      for (int i = 0; i < Const.MAP_WIDTH; i++) {
        for (int j = 0; j < Const.MAP_LENGTH; j++) {
          gc.drawImage(image, i * Const.MAP_OFFSET, j * Const.MAP_OFFSET);
          if (parent.hasRoom(i + j * Const.MAP_WIDTH)) {
            if (parent.isStartRoom(i + j * Const.MAP_WIDTH)) {
              gc.setFill(Color.web(Const.COLOR_START));
            } else if (parent.isEndRoom(i + j * Const.MAP_WIDTH)) {
              gc.setFill(Color.web(Const.COLOR_END));
            } else {
              gc.setFill(Color.web(Const.COLOR_BAR));
            }
            gc.fillRect(i * Const.MAP_OFFSET, j * Const.MAP_OFFSET, 75, 75);
          }
        }
      }
      return;
    }

    Integer row = parent.getRoomId() % Const.MAP_WIDTH, col = parent.getRoomId() / Const.MAP_WIDTH;
    // Tile player was in before
    gc.setFill(Color.web(Const.COLOR_BAR));
    if (parent.hasRoom(roomId)) {
      if (parent.isStartRoom(roomId)) {
        gc.setFill(Color.web(Const.COLOR_START));
      } else if (parent.isEndRoom(roomId)) {
        gc.setFill(Color.web(Const.COLOR_END));
      } else {
        gc.setFill(Color.web(Const.COLOR_BAR));
      }
      gc.fillRect((roomId % Const.MAP_WIDTH) * Const.MAP_OFFSET,
        (roomId / Const.MAP_WIDTH) * Const.MAP_OFFSET,
        Const.MAP_OFFSET, Const.MAP_OFFSET);
      gc.setFill(Color.web(Const.COLOR_BAR));
    } else {
      image = new Image(MAP_TILE);
      gc.drawImage(image, (roomId % Const.MAP_WIDTH) * Const.MAP_OFFSET,
        (roomId / Const.MAP_WIDTH) * Const.MAP_OFFSET);
    }
    // Current tile
    if (parent.hasRoom(parent.getRoomId())) {
      if (parent.isStartRoom(parent.getRoomId())) {
        gc.setFill(Color.web(Const.COLOR_START));
      } else if (parent.isEndRoom(parent.getRoomId())) {
        gc.setFill(Color.web(Const.COLOR_END));
      } else {
        gc.setFill(Color.web(Const.COLOR_BAR));
      }
      gc.fillRect(row * Const.MAP_OFFSET, col * Const.MAP_OFFSET,
        Const.MAP_OFFSET, Const.MAP_OFFSET);
      gc.setFill(Color.web(Const.COLOR_BAR));
    } else {
      image = new Image(MAP_TILE);
      gc.drawImage(image, row * Const.MAP_OFFSET, col * Const.MAP_OFFSET);
    }
    image = new Image(MAP_ARROW);
    gc.drawImage(image, row * Const.MAP_OFFSET, col * Const.MAP_OFFSET);
  }

  public void redraw(Const.State state) {
    Image image;
    switch (state) {
      case LOAD:
        gc.setFill(Color.web(Const.COLOR_INVENTORY));
        gc.fillRect(0, 0, 375, 525);
        gc.fillRect(375, 50, 525, 425);
        gc.fillRect(900, 0, 100, 525);
        gc.setFill(Color.web(Const.COLOR_BAR));
        gc.fillRect(375, 0, 525, 50);
        gc.fillRect(375, 475, 525, 50);
        gc.setFill(Color.web(Const.COLOR_FILL));
        /* Text */
        // Greeter
        gc.strokeText("Welcome to", 190, 80);
        gc.fillText("Welcome to", 190, 80);
        gc.strokeText("the Editor!", 190, 130);
        gc.fillText("the Editor!", 190, 130);
        // Instructions
        image = new Image(KEY_LEFT);
        gc.drawImage(image, 385, 60);
        gc.strokeText("to create a", 640, 107);
        gc.fillText("to create a", 640, 107);
        gc.strokeText("new dungeon", 665, 157);
        gc.fillText("new dungeon", 665, 157);
        image = new Image(KEY_RIGHT);
        gc.drawImage(image, 385, 300);
        gc.strokeText("to load and", 650, 347);
        gc.fillText("to load and", 650, 347);
        gc.strokeText("edit existing", 660, 397);
        gc.fillText("edit existing", 660, 397);
        gc.strokeText("dungeon file", 660, 447);
        gc.fillText("dungeon file", 660, 447);
        break;
      case DEFAULT:
        drawMap(false);
        gc.setFill(Color.web(Const.COLOR_INVENTORY));
        gc.fillRect(375, 50, 525, 425);
        // Instructions
        gc.setFill(Color.web(Const.COLOR_FILL));
        image = new Image(KEY_ENTER);
        gc.drawImage(image, 385, 60);
        gc.strokeText("to activate", 640, 107);
        gc.fillText("to activate", 640, 107);
        gc.strokeText("selected", 595, 157);
        gc.fillText("selected", 595, 157);
        gc.strokeText("room", 540, 207);
        gc.fillText("room", 540, 207);
        image = new Image(KEY_DELETE);
        gc.drawImage(image, 385, 300);
        gc.strokeText("to delete", 605, 347);
        gc.fillText("to delete", 605, 347);
        gc.strokeText("selected", 595, 397);
        gc.fillText("selected", 595, 397);
        gc.strokeText("room", 540, 447);
        gc.fillText("room", 540, 447);
        break;
      case LOOT:
        drawMap(false);
        gc.setFill(Color.web(Const.COLOR_INVENTORY));
        gc.fillRect(375, 50, 525, 425);
        // Instructions
        gc.setFill(Color.web(Const.COLOR_FILL));
        image = new Image(KEY_LEFT);
        gc.drawImage(image, 385, 60);
        gc.strokeText("to edit room", 655, 107);
        gc.fillText("to edit room", 655, 107);
        image = new Image(KEY_RIGHT);
        gc.drawImage(image, 385, 300);
        gc.strokeText("to edit", 570, 347);
        gc.fillText("to edit", 570, 347);
        gc.strokeText("monster", 595, 397);
        gc.fillText("monster", 595, 397);
        gc.strokeText("and loot", 600, 447);
        gc.fillText("and loot", 600, 447);
        break;
      case ROOM:
        image = new Image(ROOM_BG);
        gc.drawImage(image, 375, 50);
        // Room
        if (parent.getRoomSprite() == null) {
          image = new Image(ROOM_FRONT + ROOM_DEFAULT);
        } else {
          image = new Image(ROOM_FRONT + parent.getRoomSprite());
        }
        gc.drawImage(image, 450, 50);
        break;
      case MENU:
        gc.setFill(Color.web(Const.COLOR_INVENTORY));
        gc.fillRect(375, 50, 525, 425);

        this.gc.setFill(Color.web(Const.COLOR_FILL));
        Integer active = this.parent.getMenuActive();
        for (int i = 0; i < this.parent.getMenuCount(); i++) {
          this.gc.drawImage(IMAGE_BUTTON, MENU_X, MENU_Y + i * Const.BUTTON_HEIGHT);
          this.gc.strokeText(this.parent.getMenuAction(i), MENU_X + Const.TEXT_X_OFFSET,
            MENU_Y + i * Const.BUTTON_HEIGHT + Const.TEXT_Y_OFFSET);
          this.gc.fillText(this.parent.getMenuAction(i), MENU_X + Const.TEXT_X_OFFSET,
            MENU_Y + i * Const.BUTTON_HEIGHT + Const.TEXT_Y_OFFSET);
          if (i == active) {
            this.gc.drawImage(IMAGE_BUTTON_ACTIVE, MENU_X, MENU_Y + i * Const.BUTTON_HEIGHT);
          }
        }
        break;
      case SET:
        drawMap(true);
        break;
    }
    // Overlay
    image = new Image(OVERLAY);
    gc.drawImage(image, 0, 0);
    // Current room
    roomId = parent.getRoomId();
  }

  public void close() {
  }
}

