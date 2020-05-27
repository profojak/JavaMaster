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
  private final Integer MENU_X = 500, MENU_Y = 170;
  private final String
    // Map
    MAP_TILE = "/sprites/map/tile.png",
    MAP_ARROW = "/sprites/map/arrow_NORTH.png", MAP_WALL = "/sprites/map/wall_",

    OVERLAY = "/sprites/overlay/game.png",

    // Keys
    KEY_UP = "/sprites/editor/key_up.png", KEY_DOWN = "/sprites/editor/key_down.png",
    KEY_LEFT = "/sprites/editor/key_left.png", KEY_RIGHT = "/sprites/editor/key_right.png",
    KEY_ENTER = "/sprites/editor/key_etner.png", KEY_ESCAPE = "/sprites/editor/key_escape.png",
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

    gc.setFill(Color.web(Const.COLOR_BAR));
    if (drawUndiscoveredMap) {
      this.roomId = parent.getRoomId();
      image = new Image(MAP_TILE);
      for (int i = 0; i < Const.MAP_WIDTH; i++) {
        for (int j = 0; j < Const.MAP_LENGTH; j++) {
          gc.drawImage(image, i * Const.MAP_OFFSET, j * Const.MAP_OFFSET);
          if (parent.hasRoom(i + j * Const.MAP_WIDTH)) {
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
      gc.fillRect((roomId % Const.MAP_WIDTH) * Const.MAP_OFFSET,
        (roomId / Const.MAP_WIDTH) * Const.MAP_OFFSET,
        Const.MAP_OFFSET, Const.MAP_OFFSET);
    } else {
      image = new Image(MAP_TILE);
      gc.drawImage(image, (roomId % Const.MAP_WIDTH) * Const.MAP_OFFSET,
        (roomId / Const.MAP_WIDTH) * Const.MAP_OFFSET);
    }
    // Current tile
    if (parent.hasRoom(parent.getRoomId())) {
      gc.fillRect(row * Const.MAP_OFFSET, col * Const.MAP_OFFSET,
        Const.MAP_OFFSET, Const.MAP_OFFSET);
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
        gc.strokeText("new dungeon.", 670, 157);
        gc.fillText("new dungeon.", 670, 157);
        image = new Image(KEY_RIGHT);
        gc.drawImage(image, 385, 300);
        gc.strokeText("to load and", 650, 347);
        gc.fillText("to load and", 650, 347);
        gc.strokeText("edit existing", 665, 397);
        gc.fillText("edit existing", 665, 397);
        gc.strokeText("dungeon file.", 670, 447);
        gc.fillText("dungeon file.", 670, 447);
        break;
      case DEFAULT:
        drawMap(false);
        gc.setFill(Color.web(Const.COLOR_INVENTORY));
        gc.fillRect(375, 50, 525, 425);
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

/*
  private void drawInventory(Boolean drawActiveItem) {
    gc.setFill(Color.web(Const.COLOR_INVENTORY));
    gc.fillRect(905, 10, 85, 505);
    // Item frame
    Image image = new Image(INVENTORY_FRAME_ITEM);
    gc.drawImage(image, 910, 15);
    gc.drawImage(image, 910, 125);
    // Weapon frame
    image = new Image(INVENTORY_FRAME_WEAPON);
    gc.drawImage(image, 910, 235);
    // Items
    image = new Image(BOMB);
    gc.drawImage(image, 910, 15);
    image = new Image(POTION);
    gc.drawImage(image, 910, 125);
    image = new Image(WEAPONS + parent.getWeaponSprite());
    gc.drawImage(image, 910, 235);
    // If in combat, show selected item
    if (drawActiveItem) {
      switch (parent.getActiveItem()) {
        case BOMB:
          image = new Image(INVENTORY_FRAME_ITEM_ACTIVE);
          gc.drawImage(image, 910, 15);
          break;
        case POTION:
          image = new Image(INVENTORY_FRAME_ITEM_ACTIVE);
          gc.drawImage(image, 910, 125);
          break;
        case WEAPON:
          image = new Image(INVENTORY_FRAME_WEAPON_ACTIVE);
          gc.drawImage(image, 910, 235);
          break;
      }
    }
    // Text
    gc.setFill(Color.web(Const.COLOR_FILL));
    gc.strokeText(String.valueOf(parent.getBombCount()), 948, 125);
    gc.fillText(String.valueOf(parent.getBombCount()), 948, 125);
    gc.strokeText(String.valueOf(parent.getPotionCount()), 948, 235);
    gc.fillText(String.valueOf(parent.getPotionCount()), 948, 235);
    gc.strokeText(String.valueOf(parent.getWeaponDamage()), 948, 505);
    gc.fillText(String.valueOf(parent.getWeaponDamage()), 948, 505);
  }

  private void drawBars(Boolean drawBothBars) {
    gc.setFill(Color.web(Const.COLOR_BAR));
    gc.fillRect(375, 10, BAR_WIDTH + 10, BAR_HEIGHT + 10);
    gc.fillRect(375, 480, BAR_WIDTH + 10, BAR_HEIGHT + 10);
    // Player HP bar
    Integer playerBarPart = (parent.getPlayerHP() == parent.getPlayerMaxHP()) ?
      (parent.getPlayerHP() == 0) ? 0 : BAR_WIDTH : Math.round(((float)BAR_WIDTH /
      (float)parent.getPlayerMaxHP()) * (float)parent.getPlayerHP());
    gc.setFill(Color.web(Const.COLOR_BAR_PLAYER_BG));
    gc.fillRect(375, 480, playerBarPart, BAR_HEIGHT);
    gc.setFill(Color.web(Const.COLOR_BAR_PLAYER_FG));
    gc.fillRect(380, 485, playerBarPart - 10, BAR_HEIGHT - 10);
    if (!drawBothBars) {
      return;
    }
    // Monster HP bar
    Integer monsterBarPart = (parent.getMonsterHP() == parent.getMonsterMaxHP()) ?
      (parent.getMonsterHP() == 0) ? 0 : BAR_WIDTH : Math.round(((float)BAR_WIDTH /
      (float)parent.getMonsterMaxHP()) * (float)parent.getMonsterHP());
    if (parent.getMonsterHP() > 0) {
      gc.setFill(Color.web(Const.COLOR_BAR_MONSTER_BG));
      gc.fillRect(375, 10, monsterBarPart, BAR_HEIGHT);
      gc.setFill(Color.web(Const.COLOR_BAR_MONSTER_FG));
      gc.fillRect(380, 15, monsterBarPart - 10, BAR_HEIGHT - 10);
    }
    // Item bar helpers
    switch (parent.getActiveItem()) {
      case BOMB:
        // Player has no bomb and takes damage
        if (parent.getBombCount() == 0) {
          Integer bombPlayerBarDamage = (BAR_WIDTH / parent.getPlayerMaxHP())
            * parent.getMonsterDamage();
          Integer bombPlayerBarRight = bombPlayerBarDamage;
          Integer bombPlayerBarLeft = playerBarPart - bombPlayerBarDamage;
          if (bombPlayerBarLeft < 0) {
            bombPlayerBarRight += bombPlayerBarLeft;
            bombPlayerBarLeft = 0;
          }
          gc.setFill(Color.web(Const.COLOR_FILL));
          gc.fillRect(375 + bombPlayerBarLeft, 480, bombPlayerBarRight, BAR_HEIGHT);
        } else {
          // Bomb oneshots monster
          if (parent.getBombDamage() >= parent.getMonsterMaxHP()) {
            gc.setFill(Color.web(Const.COLOR_FILL));
            gc.fillRect(375, 10, monsterBarPart, BAR_HEIGHT);
          } else {
          // Bomb hurts or finishes monster
            Integer bombBarDamage = (BAR_WIDTH / parent.getMonsterMaxHP())
              * parent.getBombDamage();
            Integer bombBarRight = bombBarDamage;
            Integer bombBarLeft = monsterBarPart - bombBarDamage;
            if (bombBarLeft < 0) {
              bombBarRight += bombBarLeft;
              bombBarLeft = 0;
            }
            gc.setFill(Color.web(Const.COLOR_FILL));
            gc.fillRect(375 + bombBarLeft, 10, bombBarRight, BAR_HEIGHT);
          }
        }
        break;
      case POTION:
        // Player has no potion and takes damage
        if (parent.getPotionCount() == 0) {
          Integer bombPlayerBarDamage = (BAR_WIDTH / parent.getPlayerMaxHP())
            * parent.getMonsterDamage();
          Integer bombPlayerBarRight = bombPlayerBarDamage;
          Integer bombPlayerBarLeft = playerBarPart - bombPlayerBarDamage;
          if (bombPlayerBarLeft < 0) {
            bombPlayerBarRight += bombPlayerBarLeft;
            bombPlayerBarLeft = 0;
          }
          gc.setFill(Color.web(Const.COLOR_FILL));
          gc.fillRect(375 + bombPlayerBarLeft, 480, bombPlayerBarRight, BAR_HEIGHT);
        // Part of health bar which will be healed
        } else {
          Integer potionBarRight = BAR_WIDTH / 2;
          if (playerBarPart + potionBarRight > BAR_WIDTH) {
            potionBarRight = BAR_WIDTH - playerBarPart;
          }
          gc.setFill(Color.web(Const.COLOR_FILL));
          gc.fillRect(375 + playerBarPart, 480, potionBarRight, BAR_HEIGHT);
        }
        break;
      case WEAPON:
        // Weapon oneshots monster
        if (parent.getWeaponDamage() >= parent.getMonsterMaxHP()) {
          gc.setFill(Color.web(Const.COLOR_FILL));
          gc.fillRect(375, 10, monsterBarPart, BAR_HEIGHT);
        } else {
        // Weapon hurts or finishes monster
          Integer weaponBarDamage = (BAR_WIDTH / parent.getMonsterMaxHP())
            * parent.getWeaponDamage();
          Integer weaponBarRight = weaponBarDamage;
          Integer weaponBarLeft = monsterBarPart - weaponBarDamage;
          if (weaponBarLeft < 0) {
            weaponBarRight += weaponBarLeft;
            weaponBarLeft = 0;
          }
          gc.setFill(Color.web(Const.COLOR_FILL));
          gc.fillRect(375 + weaponBarLeft, 10, weaponBarRight, BAR_HEIGHT);
        }

        if (parent.getMonsterDamage() != 0) {
          Integer monsterBarDamage = (BAR_WIDTH / parent.getPlayerMaxHP())
            * parent.getMonsterDamage();
          Integer monsterBarRight = monsterBarDamage;
          Integer monsterBarLeft = playerBarPart - monsterBarDamage;
          if (monsterBarLeft < 0) {
            monsterBarRight += monsterBarLeft;
            monsterBarLeft = 0;
          }
          gc.setFill(Color.web(Const.COLOR_FILL));
          gc.fillRect(375 + monsterBarLeft, 480, monsterBarRight, BAR_HEIGHT);
        }
        break;
    }
  }

  private void drawRoom() {
    // Background
    Image image = new Image(ROOM_BG);
    gc.drawImage(image, 375, 50);
    // Front wall
    if (!parent.hasRoomFront()) {
      if (parent.getRoomSprite() != null) {
        image = new Image(ROOM_FRONT + parent.getRoomSprite());
      } else {
        image = new Image(ROOM_FRONT + ROOM_DEFAULT);
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
  }

  public void redraw(Const.State state) {
    switch (state) {
      case LOAD:
        drawMap(true);
        drawInventory(false);
        drawBars(false);
        redraw(Const.State.DEFAULT);
        break;
      case VICTORY:
        image = new Image(VICTORY);
        gc.drawImage(image, 375, 50);
        image = new Image(Const.BUTTON);
        gc.drawImage(image, 495, 352);
        image = new Image(Const.BUTTON_ACTIVE);
        gc.drawImage(image, 495, 352);
        gc.setFill(Color.web(Const.COLOR_FILL));
        gc.strokeText("Victory!", 495 + Const.TEXT_X_OFFSET, 72 + Const.TEXT_Y_OFFSET);
        gc.fillText("Victory!", 495 + Const.TEXT_X_OFFSET, 72 + Const.TEXT_Y_OFFSET);
        gc.strokeText("Quit", 495 + Const.TEXT_X_OFFSET, 352 + Const.TEXT_Y_OFFSET);
        gc.fillText("Quit", 495 + Const.TEXT_X_OFFSET, 352 + Const.TEXT_Y_OFFSET);
        break;
      case DEATH:
        drawBars(false);
        this.stack.getChildren().remove(monster);
        image = new Image(DEATH);
        gc.drawImage(image, 375, 50);
        image = new Image(Const.BUTTON);
        gc.drawImage(image, 495, 352);
        image = new Image(Const.BUTTON_ACTIVE);
        gc.drawImage(image, 495, 352);
        gc.setFill(Color.web(Const.COLOR_FILL));
        gc.strokeText("You are dead...", 495 + Const.TEXT_X_OFFSET, 72 + Const.TEXT_Y_OFFSET);
        gc.fillText("You are dead...", 495 + Const.TEXT_X_OFFSET, 72 + Const.TEXT_Y_OFFSET);
        gc.strokeText("Restart", 495 + Const.TEXT_X_OFFSET, 352 + Const.TEXT_Y_OFFSET);
        gc.fillText("Restart", 495 + Const.TEXT_X_OFFSET, 352 + Const.TEXT_Y_OFFSET);
        break;
      case DEFAULT:
        drawMap(false);
        drawRoom();
        break;
      case INVENTORY:
        drawInventory(true);
        drawBars(true);
        break;
      case MONSTER:
        drawMap(false);
        drawInventory(true);
        drawBars(true);
        drawRoom();

        // Monster
        this.monster = new ImageView(new Image(MONSTER + parent.getMonster().getSprite()));
        this.monster.setPreserveRatio(true);
        this.monster.setCache(true);
        this.monster.setSmooth(false);
        this.stack.getChildren().add(monster);
        this.stack.setMargin(monster, new Insets(0, 0, 0, 275));

        // Effect
        this.effect.setPreserveRatio(true);
        this.effect.setCache(true);
        this.effect.setSmooth(false);
        this.effect.setOpacity(0);
        this.stack.getChildren().add(effect);
        this.stack.setMargin(effect, new Insets(0, 0, 0, 275));
        break;
      case COMBAT:
        // Monster is dead
        if (parent.getMonsterHP() <= 0) {
          this.stack.getChildren().remove(monster);
          drawInventory(false);
          drawBars(false);
          return;
        }
        drawInventory(true);
        drawBars(true);

        if (thread.isAlive()) {
          thread.interrupt();
          this.effect.setOpacity(0);
          this.monster.setFitWidth(Const.WINDOW_HEIGHT);
        }
        thread = new Thread(new GameDrawCombatRunnable(monster, effect));
        thread.start();
        break;
      case STORY_BEFORE:
        drawMap(false);

        thread = new Thread(new GameDrawStoryRunnable(gc, parent.getStoryBefore()));
        thread.start();
        break;
      case STORY_AFTER:
        drawMap(false);
        drawRoom();

        thread = new Thread(new GameDrawStoryRunnable(gc, parent.getStoryAfter()));
        thread.start();
        break;
      case MENU:
        drawMap(false);
        drawRoom();

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
      case LOOT:
        drawInventory(false);
        drawRoom();

        switch (parent.getLootType()) {
          case WEAPON:
            image = new Image(WEAPONS + parent.getWeaponSprite());
            this.gc.drawImage(image, 560, 125);
            break;
          case BOMB:
            image = new Image(BOMB);
            this.gc.drawImage(image, 560, 200);
            break;
          case POTION:
            image = new Image(POTION);
            this.gc.drawImage(image, 560, 200);
            break;
        }
        gc.setFill(Color.web(Const.COLOR_FILL));
        gc.strokeText(String.valueOf(parent.getLootCount()), 685, 280);
        gc.fillText(String.valueOf(parent.getLootCount()), 685, 280);
        break;
    }
  }
}*/

