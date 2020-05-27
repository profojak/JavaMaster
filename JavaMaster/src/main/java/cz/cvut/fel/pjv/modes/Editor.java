package cz.cvut.fel.pjv.modes;

import cz.cvut.fel.pjv.Const;
import cz.cvut.fel.pjv.Root;
import cz.cvut.fel.pjv.inventory.items.*;
import cz.cvut.fel.pjv.modes.draw.*;
import cz.cvut.fel.pjv.entities.*;
import cz.cvut.fel.pjv.room.Room;
import cz.cvut.fel.pjv.menu.layouts.*;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.logging.Logger;
import java.util.logging.Level;

import javafx.scene.layout.StackPane;

/**
 * Implementation of Editor mode: this class handles user input and controlls dungeon editing.
 *
 * @see Mode
 */
public class Editor implements Mode {
  private static final Logger logger = Logger.getLogger(Editor.class.getName());
  private final Root root;
  private final Draw draw;

  private Player player;
  private Room[] rooms = new Room[Const.NUMBER_OF_ROOMS];
  private Integer roomStartId = 0, roomEndId = Const.NUMBER_OF_ROOMS - 1, roomCurrentId = 0;
  private File saveFile = null, nextMap = null;
  private Layout menu;
  private Const.State state = Const.State.LOAD;

  public Editor(StackPane stack, Root root) {
    this.root = root;
    this.player = new Player();
    this.draw = new EditorDraw(stack, this);
    this.draw.redraw(state);
  }

  @Deprecated
  public Editor() {
    this.root = null;
    this.player = null;
    this.draw = null;
  }

  public void close() {
    this.draw.close();
  }

  public void keyUp() {
    switch (state) {
      case DEFAULT:
        roomCurrentId += Const.GO_NORTH;
        if (roomCurrentId < 0) {
          roomCurrentId -= Const.GO_NORTH;
        }
        break;
      case MENU:
        this.menu.buttonPrevious();
        break;
    }
    this.draw.redraw(state);
  }

  public void keyDown() {
    switch (state) {
      case DEFAULT:
        roomCurrentId += Const.GO_SOUTH;
        if (roomCurrentId >= Const.NUMBER_OF_ROOMS) {
          roomCurrentId -= Const.GO_SOUTH;
        }
        break;
      case MENU:
        this.menu.buttonNext();
        break;
    }
    this.draw.redraw(state);
  }

  public void keyLeft() {
    switch (state) {
      case LOAD:
        this.draw.redraw(Const.State.SET);
        state = Const.State.DEFAULT;
        break;
      case DEFAULT:
        roomCurrentId += Const.GO_WEST;
        if (roomCurrentId < 0) {
          roomCurrentId -= Const.GO_WEST;
        }
        break;
      case LOOT:
        state = Const.State.ROOM;
        break;
      case MENU:
        this.menu.buttonPrevious();
        break;
    }
    this.draw.redraw(state);
  }

  public void keyRight() {
    switch (state) {
      case LOAD:
        this.saveFile = this.root.getFile();
        parseSaveFile(saveFile);
        this.draw.redraw(Const.State.SET);
        state = Const.State.DEFAULT;
        break;
      case DEFAULT:
        roomCurrentId += Const.GO_EAST;
        if (roomCurrentId >= Const.NUMBER_OF_ROOMS) {
          roomCurrentId -= Const.GO_EAST;
        }
        break;
      case MENU:
        this.menu.buttonNext();
        break;
    }
    this.draw.redraw(state);
  }

  public void keyEnter() {
    switch (state) {
      case DEFAULT:
        if (!hasRoom(roomCurrentId)) {
          rooms[roomCurrentId] = new Room();
        }
        state = Const.State.LOOT;
        break;
      case MENU:
        // Cancel
        if (this.menu.getAction(this.menu.getActive()).equals(Const.MENU_CANCEL)) {
          this.menu = null;
          state = Const.State.DEFAULT;
        // Save
        } else if (this.menu.getAction(this.menu.getActive()).equals(Const.MENU_SAVE)) {
        // Exit
        } else if (this.menu.getAction(this.menu.getActive()).equals(Const.MENU_EXIT)) {
          this.draw.close();
          this.root.switchMode(Const.MENU_MAINMENU);
          return;
        }
        break;
    }
    this.draw.redraw(state);
  }

  public void keyEscape() {
    switch (state) {
      case DEFAULT:
        this.menu = new Save();
        state = Const.State.MENU;
        break;
      case LOOT:
        state = Const.State.DEFAULT;
        break;
      case ROOM:
        state = Const.State.LOOT;
        break;
      case MENU:
        this.menu = null;
        state = Const.State.DEFAULT;
        break;
    }
    this.draw.redraw(state);
  }

  public void keyDelete() {
    switch (state) {
      case DEFAULT:
        rooms[roomCurrentId] = null;
        break;
    }
    this.draw.redraw(state);
  }

  public Boolean hasRoom(Integer index) {
    return rooms[index] != null;
  }

  public Boolean isStartRoom(Integer index) {
    return index == roomStartId;
  }

  public Boolean isEndRoom(Integer index) {
    return index == roomEndId;
  }

  // Getters

  public Integer getRoomId() {
    return roomCurrentId;
  }

  public String getRoomSprite() {
    return rooms[roomCurrentId].getSprite();
  }

  // Save files

  /**
   * Parses save file and prepares the dungeon.
   *
   * <p>This method reads .dung text file and creates instances of described classes. After this
   * method is finished, Game object is ready for playthrough.
   *
   * @param saveFile - dungeon to be parsed, saved in .dung file
   * @return whether parsing was successful
   * @author profojak
   */
  private Boolean parseSaveFile(File saveFile) { 
    try {
      BufferedReader saveReader = new BufferedReader(new FileReader(saveFile));
      while (saveReader.ready()) {
        String[] line = saveReader.readLine().split(" ");
        Const.LoadPart load = Const.LoadPart.valueOf(line[0].toUpperCase());

        switch (load) {
          // Room id where the dungeon starts and ends
          case START:
            roomStartId = Integer.parseInt(line[1]);
            logger.info(Const.LOG_WHITE + ">>> roomStartId = " + roomStartId
              + Const.LOG_RESET); // DEBUG
            roomEndId = Integer.parseInt(line[2]);
            logger.info(Const.LOG_WHITE + ">>> roomEndId = " + roomEndId
              + Const.LOG_RESET); // DEBUG
            break;
          // Player variables
          case PLAYER:
            player.setHp(Integer.parseInt(line[2]));
            logger.info(Const.LOG_WHITE + ">>> player = " + player.getHP()
              + Const.LOG_RESET); // DEBUG
            Weapon weapon = new Weapon(line[1], line[1].substring(0, line[1].lastIndexOf('.')),
              Integer.parseInt(line[3]));
            player.takeLoot(weapon);
            logger.info(Const.LOG_WHITE + ">>> player.getDamage = " + player.getDamage()
              + Const.LOG_RESET); // DEBUG
            logger.info(Const.LOG_WHITE + ">>> player.getSprite = " + player.getSprite()
              + Const.LOG_RESET); // DEBUG
            break;
          // Dungeon rooms
          case ID:
            roomCurrentId = Integer.parseInt(line[1]);
            logger.info(Const.LOG_WHITE + ">>> roomCurrentId = " + roomCurrentId
              + Const.LOG_RESET); // DEBUG
            rooms[roomCurrentId] = new Room();
            break;
          // Story of current room
          case STORY:
            rooms[roomCurrentId].setStoryBefore(line[1].replaceAll("_", " "));
            logger.info(Const.LOG_WHITE + ">>> storyBefore = "
              + rooms[roomCurrentId].getStoryBefore() + Const.LOG_RESET); // DEBUG
            if (line.length == 3) {
              rooms[roomCurrentId].setStoryAfter(line[2].replaceAll("_", " "));
              logger.info(Const.LOG_WHITE + ">>> storyAfter = "
                + rooms[roomCurrentId].getStoryAfter() + Const.LOG_RESET); // DEBUG
            }
            break;
          // Monster in current room
          case MONSTER:
            rooms[roomCurrentId].setMonster(line[1],
              Integer.parseInt(line[2]), Integer.parseInt(line[3]));
            logger.info(Const.LOG_WHITE + ">>> room.getMonster = " +
              rooms[roomCurrentId].getMonster() + Const.LOG_RESET); // DEBUG
            break;
          // Loot in current room
          case LOOT:
            rooms[roomCurrentId].setLoot(line[1], Integer.parseInt(line[2]), player.getHP());
            logger.info(Const.LOG_WHITE + ">>> room.getLootSprite = " +
              rooms[roomCurrentId].getLoot().getSprite() + Const.LOG_RESET); // DEBUG
            break;
          // Texture of current room
          case WALL:
            rooms[roomCurrentId].setSprite(line[1]);
            logger.info(Const.LOG_WHITE + ">>> room.sprite = " + rooms[roomCurrentId].getSprite()
              + Const.LOG_RESET); // DEBUG
            break;
          // Current room
          case END:
            if (line.length == 2) {
              String nextMapPath = Const.SAVE_PATH + line[1];
              if (new File(nextMapPath).canRead()) {
                logger.info(Const.LOG_WHITE + ">>> nextMapPath = " + nextMapPath
                  + Const.LOG_RESET); // DEBUG
                nextMap = new File(nextMapPath);
              } else {
                nextMap = null;
              }
            }
            roomCurrentId = roomStartId;
            break;
          // Wrong file format!
          default:
            return false;
        }
      }
    } catch (Exception exception) {
      logger.log(Level.SEVERE, Const.LOG_RED + "File could not be loaded."
        + Const.LOG_RESET, exception); // ERROR
      return false; // File could not be loaded
    }
    return true;
  }

  public String getMenuAction(Integer index) {
    return this.menu.getAction(index);
  }

  public Integer getMenuActive() {
    return this.menu.getActive();
  }

  public Integer getMenuCount() {
    return this.menu.getCount();
  }
}

/*
  public void keyUp() {
    switch (state) {
      case DEFAULT:
        // Go to the next room
        if (hasRoomFront()) {
          switch (direction) {
            case NORTH:
              roomCurrentId += Const.GO_NORTH;
              break;
            case EAST:
              roomCurrentId += Const.GO_EAST;
              break;
            case SOUTH:
              roomCurrentId += Const.GO_SOUTH;
              break;
            case WEST:
              roomCurrentId += Const.GO_WEST;
              break;
            default:
              logger.severe(Const.LOG_RED + ">>>  Error: Unexpected direction value: " + direction
                + Const.LOG_RESET); // ERROR
              return;
          }

          logger.info(Const.LOG_WHITE + ">>> Room index: " + roomCurrentId + ", direction: "
            + direction + Const.LOG_RESET); // DEBUG

          // Story and Monster
          if (!isRoomVisited(roomCurrentId)) {
            this.draw.redraw(Const.State.DEFAULT);
            rooms[roomCurrentId].setVisited();
            // Story before
            if (checkForStoryBefore()) {
              break;
            }

            if (checkForMonster()) {
              return;
            }

            if (checkForStoryAfter()) {
              break;
            }

            if (checkForLoot()) {
              break;
            }
          }
          if (checkForFloorEnd()) {
            break;
          }
        } else {
          logger.warning(Const.LOG_YELLOW + ">>>  You can't go there." + Const.LOG_RESET); // DEBUG
        }
        break;
      case COMBAT:
        itemPrevious();
        this.draw.redraw(Const.State.INVENTORY);
        return;
      case LOOT:
        state = Const.State.DEFAULT;
        break;
      case STORY_BEFORE:
        draw.close();

        if (checkForMonster()) {
          return;
        }

        if (checkForStoryAfter()) {
          break;
        }

        if (checkForLoot()) {
          break;
        }

        if (checkForFloorEnd()) {
          break;
        }

        state = Const.State.DEFAULT;
        break;
      case STORY_AFTER:
        draw.close();

        if (checkForLoot()) {
          break;
        }

        if (checkForFloorEnd()) {
          break;
        }

        state = Const.State.DEFAULT;
        break;
      case MENU:
        this.menu.buttonPrevious();
        break;
    }
    this.draw.redraw(state);
  }

  public void keyDown() {
    switch (state) {
      case COMBAT:
        itemNext();
        this.draw.redraw(Const.State.INVENTORY);
        return;
      case LOOT:
        state = Const.State.DEFAULT;
        break;
      case STORY_BEFORE:
        draw.close();

        if (checkForMonster()) {
          return;
        }

        if (checkForStoryAfter()) {
          break;
        }

        if (checkForLoot()) {
          break;
        }

        if (checkForFloorEnd()) {
          break;
        }

        state = Const.State.DEFAULT;
        break;
      case STORY_AFTER:
        draw.close();

        if (checkForLoot()) {
          break;
        }

        if (checkForFloorEnd()) {
          break;
        }

        state = Const.State.DEFAULT;
        break;
      case MENU:
        this.menu.buttonNext();
        break;
    }
    this.draw.redraw(state);
  }

  public void keyLeft() {
    switch (state) {
      case DEFAULT:
        // Turns player to the left
        turnPlayer(Const.TURN_LEFT);
        break;
      case COMBAT:
        itemPrevious();
        this.draw.redraw(Const.State.INVENTORY);
        return;
      case LOOT:
        state = Const.State.DEFAULT;
        break;
      case STORY_BEFORE:
        draw.close();

        if (checkForMonster()) {
          return;
        }

        if (checkForStoryAfter()) {
          break;
        }

        if (checkForLoot()) {
          break;
        }

        if (checkForFloorEnd()) {
          break;
        }

        state = Const.State.DEFAULT;
        break;
      case STORY_AFTER:
        draw.close();

        if (checkForLoot()) {
          break;
        }

        if (checkForFloorEnd()) {
          break;
        }

        state = Const.State.DEFAULT;
        break;
      case MENU:
        this.menu.buttonPrevious();
        break;
    }
    this.draw.redraw(state);
  }

  public void keyRight() {
    switch (state) {
      case DEFAULT:
        // Turns player to the right
        turnPlayer(Const.TURN_RIGHT);
        break;
      case COMBAT:
        itemNext();
        this.draw.redraw(Const.State.INVENTORY);
        return;
      case LOOT:
        state = Const.State.DEFAULT;
        break;
      case STORY_BEFORE:
        draw.close();

        if (checkForMonster()) {
          return;
        }

        if (checkForStoryAfter()) {
          break;
        }

        if (checkForLoot()) {
          break;
        }

        if (checkForFloorEnd()) {
          break;
        }

        state = Const.State.DEFAULT;
        break;
      case STORY_AFTER:
        draw.close();

        if (checkForLoot()) {
          break;
        }

        if (checkForFloorEnd()) {
          break;
        }

        state = Const.State.DEFAULT;
        break;
      case MENU:
        this.menu.buttonNext();
        break;
    }
    this.draw.redraw(state);
  }

  public void keyEscape() {
    switch (state) {
      case DEFAULT:
        // Opens menu
        this.menu = new Exit();
        state = Const.State.MENU;
        break;
      case COMBAT:
        return;
      case LOOT:
        state = Const.State.DEFAULT;
        break;
      case STORY_BEFORE:
        draw.close();

        if (checkForMonster()) {
          return;
        }

        if (checkForStoryAfter()) {
          break;
        }

        if (checkForLoot()) {
          break;
        }

        if (checkForFloorEnd()) {
          break;
        }

        state = Const.State.DEFAULT;
        break;
      case STORY_AFTER:
        draw.close();

        if (checkForLoot()) {
          break;
        }

        if (checkForFloorEnd()) {
          break;
        }

        state = Const.State.DEFAULT;
        break;
      case MENU:
        // Closes menu
        this.menu = null;
        state = Const.State.DEFAULT;
        break;
    }
    this.draw.redraw(state);
  }

  public void keyEnter() {
    switch (state) {
      case VICTORY:
        this.draw.close();
        this.root.switchMode(Const.MENU_MAINMENU);
        break;
      case DEATH:
        this.draw.close();
        changeLevel(saveFile);
        break;
      case COMBAT:
        // Player takes damage
        switch (player.getActiveItem()) {
          case WEAPON:
            // Monster takes damage
            getMonster().takeDamage(getWeaponDamage());
            player.takeDamage(getMonsterDamage());
            break;
          case BOMB:
            // If player has bomb, player should not lose health
            Integer bombDamage = player.useBomb();
            if (bombDamage > 0) {
              getMonster().takeDamage(bombDamage);
            }
            player.takeDamage(getMonsterDamage());
            break;
          case POTION:
            // If player has no potion, takes damage
            if (!player.usePotion()) {
              player.takeDamage(getMonsterDamage());
            }
            break;
        }
        // Monster is dead
        if (getMonsterHP() <= 0) {
          this.draw.redraw(state);

          if (checkForStoryAfter()) {
            break;
          }

          if (checkForLoot()) {
            break;
          }
        }
        // Player is dead
        if (getPlayerHP() <= 0) {
          state = Const.State.DEATH;
        }
        break;
      case LOOT:
        state = Const.State.DEFAULT;
        break;
      case STORY_BEFORE:
        draw.close();

        if (checkForMonster()) {
          return;
        }

        if (checkForStoryAfter()) {
          break;
        }

        if (checkForLoot()) {
          break;
        }

        if (checkForFloorEnd()) {
          break;
        }

        state = Const.State.DEFAULT;
        break;
      case STORY_AFTER:
        draw.close();

        if (checkForLoot()) {
          break;
        }

        if (checkForFloorEnd()) {
          break;
        }

        state = Const.State.DEFAULT;
        break;
      case MENU:
        // Cancel
        if (this.menu.getAction(this.menu.getActive()).equals(Const.MENU_CANCEL)) {
          this.menu = null;
          state = Const.State.DEFAULT;
        // Exit
        } else if (this.menu.getAction(this.menu.getActive()).equals(Const.MENU_EXIT)) {
          this.draw.close();
          this.root.switchMode(Const.MENU_MAINMENU);
        // Descend
        } else if (this.menu.getAction(this.menu.getActive()).equals(Const.MENU_DESCEND)) {
          changeLevel(nextMap);
          return;
        // Not yet
        } else if (this.menu.getAction(this.menu.getActive()).equals(Const.MENU_NOT_YET)) {
          this.menu = null;
          state = Const.State.DEFAULT;
        }
        break;
    }
    this.draw.redraw(state);
  }

  public void keyDelete() {
    switch (state) {
      case COMBAT:
        return;
      case LOOT:
        state = Const.State.DEFAULT;
        break;
      case STORY_BEFORE:
        draw.close();

        if (checkForMonster()) {
          return;
        }

        if (checkForStoryAfter()) {
          break;
        }

        if (checkForLoot()) {
          break;
        }

        if (checkForFloorEnd()) {
          break;
        }

        state = Const.State.DEFAULT;
        break;
      case STORY_AFTER:
        draw.close();

        if (checkForLoot()) {
          break;
        }

        if (checkForFloorEnd()) {
          break;
        }

        state = Const.State.DEFAULT;
        break;
    }
    this.draw.redraw(state);
  }

  private Boolean checkForMonster() {
    if (rooms[roomCurrentId].hasMonster()) {
      state = Const.State.COMBAT;
      this.draw.redraw(Const.State.MONSTER);
      return true;
    }
    return false;
  }

  private Boolean checkForFloorEnd() {
    if (roomCurrentId.equals(roomEndId)) {
      logger.info(Const.LOG_WHITE + ">>> You have entered the End room on this floor"
        + Const.LOG_RESET); // DEBUG
      this.menu = new Continue();
      state = Const.State.MENU;
      return true;
    }
    return false;
  }

  private Boolean checkForStoryBefore() {
    if (rooms[roomCurrentId].hasStoryBefore()) {
      logger.info(Const.LOG_WHITE + ">>> Story before: " + getStoryBefore()
        + Const.LOG_RESET); // DEBUG
      state = Const.State.STORY_BEFORE;
      return true;
    }
    return false;
  }

  private Boolean checkForStoryAfter() {
    if (rooms[roomCurrentId].hasStoryAfter()) {
      logger.info(Const.LOG_WHITE + ">>> Story after: " + getStoryAfter()
        + Const.LOG_RESET); // DEBUG
      state = Const.State.STORY_AFTER;
      return true;
    }
    return false;
  }

  private Boolean checkForLoot() {
    if (rooms[roomCurrentId].hasLoot()) {
      logger.info(Const.LOG_WHITE + ">>> Room has Loot!"); // DEBUG
      takeLoot();
      state = Const.State.LOOT;
      return true;
    }
    return false;
  }

  public Boolean hasRoom(int directionChange) {
    Const.Direction newDirection = direction;
    if (directionChange != 0) {
      newDirection = changeDirection(directionChange);
    }

    try {
      switch (Objects.requireNonNull(newDirection)) {
        case NORTH:
          return roomCurrentId > Const.NORTHERN_BORDER &&
            rooms[roomCurrentId + Const.GO_NORTH] != null;
        case EAST:
          return roomCurrentId % Const.MAP_WIDTH != Const.EASTERN_BORDER &&
            rooms[roomCurrentId + Const.GO_EAST] != null;
        case SOUTH:
          return roomCurrentId < Const.SOUTHERN_BORDER &&
            rooms[roomCurrentId + Const.GO_SOUTH] != null;
        case WEST:
          return roomCurrentId % Const.MAP_WIDTH != Const.WESTERN_BORDER &&
            rooms[roomCurrentId + Const.GO_WEST] != null;
      }
    } catch (Exception exception) {
      logger.log(Level.SEVERE, Const.LOG_RED + ">>>  Error: Unexpected direction value: "
        + newDirection + Const.LOG_RESET, exception); // ERROR
      return null;
    }
    return null;
  }

  public String getStoryBefore() {
    return rooms[roomCurrentId].getStoryBefore();
  }

  public String getStoryAfter() {
    return rooms[roomCurrentId].getStoryAfter();
  }

  public Monster getMonster() {
    return rooms[roomCurrentId].getMonster();
  }

  public Const.ItemType getLootType() {
    if (rooms[roomCurrentId].getLoot() instanceof Weapon) {
      return Const.ItemType.WEAPON;
    } else if (rooms[roomCurrentId].getLoot() instanceof Bomb) {
      return Const.ItemType.BOMB;
    } else if (rooms[roomCurrentId].getLoot() instanceof Potion) {
      return Const.ItemType.POTION;
    }
    return null;
  }

  public Integer getLootCount() {
    if (rooms[roomCurrentId].getLoot() instanceof Weapon) {
      return ((Weapon)rooms[roomCurrentId].getLoot()).getWeaponDamage();
    } else if (rooms[roomCurrentId].getLoot() instanceof Bomb) {
      return ((Bomb)rooms[roomCurrentId].getLoot()).getBombCount();
    } else if (rooms[roomCurrentId].getLoot() instanceof Potion) {
      return ((Potion)rooms[roomCurrentId].getLoot()).getPotionCount();
    }
    return null;
  }

  public String getDirection() {
    return direction.toString();
  }

  public String getLeftDirection() {
    return changeDirection(Const.TURN_LEFT).toString();
  }

  public String getRightDirection() {
    return changeDirection(Const.TURN_RIGHT).toString();
  }

  public void itemNext() {
    player.itemNext();
  }

  public void itemPrevious() {
    player.itemPrevious();
  }

  public Integer getBombDamage() {
    return player.getBombDamage();
  }

  public Integer getPotionHeal() {
    return player.getPotionHeal();
  }

  public Integer getPlayerHP() {
    return player.getHP();
  }

  public Integer getPlayerMaxHP() {
    return player.getMaxHP();
  }

  public Integer getWeaponDamage() {
    return player.getDamage();
  }

  public Integer getBombCount() {
    return player.getBombCount();
  }

  public Integer getPotionCount() {
    return player.getPotionCount();
  }

  public String getWeaponSprite() {
    return player.getSprite();
  }

  public Integer getMonsterHP() {
    return getMonster().getHP();
  }

  public Integer getMonsterMaxHP() {
    return getMonster().getMaxHP();
  }

  public Integer getMonsterDamage() {
    return getMonster().getDamage();
  }

  public String getMonsterSprite() {
    return getMonster().getSprite();
  }
}
*/

