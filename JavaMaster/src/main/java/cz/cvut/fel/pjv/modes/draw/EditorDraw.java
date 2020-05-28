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

    // Inventory
    WEAPONS = "/sprites/inventory/weapons/",

    // Monster and loot
    MONSTER = "/sprites/monster/",
    BOMB = "/sprites/inventory/bomb.png", POTION = "/sprites/inventory/potion.png",

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

  private void drawMap() {
    this.roomId = parent.getRoomId();
    Image image = new Image(MAP_TILE);
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
    Integer row = parent.getRoomId() % Const.MAP_WIDTH, col = parent.getRoomId() / Const.MAP_WIDTH;
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
        drawMap();
        gc.setFill(Color.web(Const.COLOR_INVENTORY));
        gc.fillRect(375, 50, 525, 425);
        gc.fillRect(900, 0, 100, 525);
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
        image = new Image(WEAPONS + parent.getWeaponSprite());
        gc.drawImage(image, 910, 235);
        gc.strokeText(String.valueOf(parent.getWeaponDamage()), 947, 420);
        gc.fillText(String.valueOf(parent.getWeaponDamage()), 947, 420);
        gc.strokeText(String.valueOf(parent.getPlayerMaxHP()), 947, 65);
        gc.fillText(String.valueOf(parent.getPlayerMaxHP()), 947, 65);
        break;
      case LOOT:
        drawMap();
        gc.setFill(Color.web(Const.COLOR_INVENTORY));
        gc.fillRect(375, 50, 525, 425);
        gc.fillRect(900, 0, 100, 525);
        // Instructions
        gc.setFill(Color.web(Const.COLOR_FILL));
        image = new Image(KEY_LEFT);
        gc.drawImage(image, 385, 60);
        gc.strokeText("to edit room", 655, 107);
        gc.fillText("to edit room", 655, 107);
        image = new Image(KEY_RIGHT);
        gc.drawImage(image, 385, 135);
        gc.strokeText("to edit", 570, 182);
        gc.fillText("to edit", 570, 182);
        gc.strokeText("monster", 595, 232);
        gc.fillText("monster", 595, 232);
        gc.strokeText("and loot", 600, 282);
        gc.fillText("and loot", 600, 282);
        image = new Image(KEY_UP);
        gc.drawImage(image, 385, 310);
        gc.strokeText("set start", 610, 357);
        gc.fillText("set start", 610, 357);
        image = new Image(KEY_DOWN);
        gc.drawImage(image, 385, 385);
        gc.strokeText("set end", 580, 432);
        gc.fillText("set end", 580, 432);
        image = new Image(WEAPONS + parent.getWeaponSprite());
        gc.drawImage(image, 910, 235);
        image = new Image(KEY_ENTER);
        gc.drawImage(image, 915, 440);
        gc.strokeText(String.valueOf(parent.getWeaponDamage()), 947, 420);
        gc.fillText(String.valueOf(parent.getWeaponDamage()), 947, 420);
        image = new Image(KEY_DELETE);
        gc.drawImage(image, 915, 85);
        gc.strokeText(String.valueOf(parent.getPlayerMaxHP()), 947, 65);
        gc.fillText(String.valueOf(parent.getPlayerMaxHP()), 947, 65);
        break;
      case MONSTER:
        gc.setFill(Color.web(Const.COLOR_INVENTORY));
        gc.fillRect(375, 50, 525, 425);
        gc.fillRect(900, 0, 100, 525);
        gc.setFill(Color.web(Const.COLOR_FILL));
        // Instructions
        if (parent.getLoot() != null) {
          switch (parent.getLootType()) {
            case WEAPON:
              image = new Image(WEAPONS + parent.getLoot().getSprite());
              gc.drawImage(image, 910, 235);
              break;
            case BOMB:
              image = new Image(BOMB);
              gc.drawImage(image, 910, 310);
              break;
            case POTION:
              image = new Image(POTION);
              gc.drawImage(image, 910, 310);
              break;
          }
          gc.strokeText(String.valueOf(parent.getLootCount()), 947, 420);
          gc.fillText(String.valueOf(parent.getLootCount()), 947, 420);
        }
        if (parent.getMonsterSprite() != null) {
          image = new Image(MONSTER + parent.getMonsterSprite());
          gc.drawImage(image, 375, 50);
          image = new Image(KEY_LEFT);
          gc.drawImage(image, 915, 85);
          gc.strokeText(String.valueOf(parent.getMonsterDamage()), 947, 65);
          gc.fillText(String.valueOf(parent.getMonsterDamage()), 947, 65);
          image = new Image(KEY_RIGHT);
          gc.drawImage(image, 915, 225);
          gc.strokeText(String.valueOf(parent.getMonsterMaxHP()), 947, 205);
          gc.fillText(String.valueOf(parent.getMonsterMaxHP()), 947, 205);
        }
        image = new Image(KEY_UP);
        gc.drawImage(image, 915, 440);
        image = new Image(KEY_ENTER);
        gc.drawImage(image, 385, 60);
        gc.strokeText("to add or", 615, 107);
        gc.fillText("to add or", 615, 107);
        gc.strokeText("edit monster", 665, 157);
        gc.fillText("edit monster", 665, 157);
        image = new Image(KEY_DELETE);
        gc.drawImage(image, 385, 400);
        gc.strokeText("delete it", 595, 447);
        gc.fillText("delete it", 595, 447);
        break;
      case ROOM:
        image = new Image(ROOM_BG);
        gc.drawImage(image, 375, 50);
        gc.setFill(Color.web(Const.COLOR_INVENTORY));
        gc.fillRect(900, 0, 100, 525);
        gc.setFill(Color.web(Const.COLOR_FILL));
        // Room
        if (parent.getRoomSprite() == null) {
          image = new Image(ROOM_FRONT + ROOM_DEFAULT);
        } else {
          image = new Image(ROOM_FRONT + parent.getRoomSprite());
        }
        gc.drawImage(image, 450, 50);
        // Instructions
        image = new Image(KEY_LEFT);
        gc.drawImage(image, 385, 60);
        gc.strokeText("story before", 670, 107);
        gc.fillText("story before", 670, 107);
        image = new Image(KEY_RIGHT);
        gc.drawImage(image, 385, 135);
        gc.strokeText("story after", 650, 182);
        gc.fillText("story after", 650, 182);
        image = new Image(KEY_UP);
        gc.drawImage(image, 385, 400);
        gc.strokeText("edit texture", 655, 447);
        gc.fillText("edit texture", 655, 447);
        break;
      case MENU:
        gc.setFill(Color.web(Const.COLOR_INVENTORY));
        gc.fillRect(375, 50, 525, 425);
        gc.fillRect(900, 0, 100, 525);

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
        drawMap();
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

