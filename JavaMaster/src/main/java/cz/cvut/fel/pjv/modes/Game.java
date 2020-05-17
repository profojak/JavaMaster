package cz.cvut.fel.pjv.modes;

import cz.cvut.fel.pjv.Const;
import cz.cvut.fel.pjv.Root;
import cz.cvut.fel.pjv.inventory.items.Weapon;
import cz.cvut.fel.pjv.menu.layouts.Layout;
import cz.cvut.fel.pjv.modes.draw.*;
import cz.cvut.fel.pjv.entities.*;
import cz.cvut.fel.pjv.room.Room;
import cz.cvut.fel.pjv.menu.layouts.Exit;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.logging.Level;

import javafx.scene.layout.StackPane;

/**
 * Implementation of Game mode: this class handles user input and controlls the flow of the game.
 *
 * <p>This mode is loaded when user wants to play.
 *
 * @see Mode
 */
public class Game implements Mode {
  private static final Logger logger = Logger.getLogger(Game.class.getName());
  private final Root root;
  private final Draw draw;
  private final Player player;

  private Room[] rooms = new Room[Const.NUMBER_OF_ROOMS];
  private Integer roomStartId, roomEndId, roomCurrentId;
  private Const.Direction direction = Const.Direction.NORTH;
  private Exit menu;
  private Const.State state = Const.State.DEFAULT;

  /**
   * @param stack - StackPane to draw images to
   * @param root - parent object
   */
  public Game(StackPane stack, Root root) {
    this.root = root;
    this.player = new Player();
    File saveFile = this.root.getFile();
    parseSaveFile(saveFile);
    this.draw = new GameDraw(stack, this);
  }

  /**
   * @deprecated use Game(String) constructor instead
   */
  @Deprecated
  public Game() {
    this.root = null;
    this.player = null;
    this.draw = null;
  }

  /**
   * Returns changed direction.
   *
   * @param directionChange - number representing to which side to turn
   * @return changed direction
   * @author povolji2
   */
  private Const.Direction changeDirection(int directionChange) {
    Const.Direction newDirection = null;
    try {
      int directionIndex = (direction.ordinal() + directionChange) % Const.NUMBER_OF_DIRECTIONS;
      newDirection = Const.Direction.values()[directionIndex];
    } catch (Exception exception) {
      logger.log(Level.SEVERE, Const.LOG_RED + ">>>  Error: Unexpected direction value: "
        + newDirection + Const.LOG_RESET, exception); // ERROR
      return null;
    }

    return newDirection;
  }

  /**
   * Turns the player.
   *
   * @param directionChange - number representing to which side to turn
   * @author povolji2
   */
  private void turnPlayer(int directionChange) {
    direction = changeDirection(directionChange);
    logger.info(Const.LOG_WHITE + ">>> Room index: " + roomCurrentId + ", direction: " + direction
      + Const.LOG_RESET); // DEBUG
  }

  // Key methods

  /**
   * Handles up key event.
   */
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
            if (rooms[roomCurrentId].hasStoryBefore()) {
              logger.info(Const.LOG_WHITE + ">>> Story before: " + getStoryBefore()
                + Const.LOG_RESET); // DEBUG
              state = Const.State.STORY_BEFORE;
              break;
            }

            if (checkForMonster()) {
              return;
            }

            if (checkForStoryAfter()) {
              break;
            }

            // TODO Better message
            /*if (rooms[roomCurrentId].hasLoot()) {
              Loot loot = rooms[roomCurrentId].getLoot();
              logger.info(WHITE + ">>> You have found loot: " + loot.getCount() + " instance/s of " +
                loot.getSprite() + RESET); // DEBUG
              player.takeLoot(loot);
            }*/
          }

          // Last room
          if (roomCurrentId.equals(roomEndId)) {
            logger.info(Const.LOG_WHITE + ">>> You have entered the End room on this floor"
              + Const.LOG_RESET); // DEBUG
          }
        } else {
          logger.warning(Const.LOG_YELLOW + ">>>  You can't go there." + Const.LOG_RESET); // DEBUG
        }
        break;
      case COMBAT:
        itemPrevious();
        this.draw.redraw(Const.State.INVENTORY);
        return;
      case STORY_BEFORE:
        draw.close();

        if (checkForMonster()) {
          return;
        }

        if (checkForStoryAfter()) {
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

  /**
   * Handles down key event.
   */
  public void keyDown() {
    switch (state) {
      case STORY_BEFORE:
        draw.close();

        if (checkForMonster()) {
          return;
        }

        if (checkForStoryAfter()) {
          break;
        }

        state = Const.State.DEFAULT;
        break;
      case COMBAT:
        itemNext();
        this.draw.redraw(Const.State.INVENTORY);
        return;
      case MENU:
        this.menu.buttonNext();
        break;
    }
    this.draw.redraw(state);
  }

  /**
   * Handles left key event.
   */
  public void keyLeft() {
    switch (state) {
      case DEFAULT:
        // Turns player to the left
        turnPlayer(Const.TURN_LEFT);
        break;
      case STORY_BEFORE:
        draw.close();

        if (checkForMonster()) {
          return;
        }

        if (checkForStoryAfter()) {
          break;
        }

        state = Const.State.DEFAULT;
        break;
      case COMBAT:
        itemPrevious();
        this.draw.redraw(Const.State.INVENTORY);
        return;
      case MENU:
        this.menu.buttonPrevious();
        break;
    }
    this.draw.redraw(state);
  }

  /**
   * Handles right key event.
   */
  public void keyRight() {
    switch (state) {
      case DEFAULT:
        // Turns player to the right
        turnPlayer(Const.TURN_RIGHT);
        break;
      case STORY_BEFORE:
        draw.close();

        if (checkForMonster()) {
          return;
        }

        if (checkForStoryAfter()) {
          break;
        }

        state = Const.State.DEFAULT;
        break;
      case COMBAT:
        itemNext();
        this.draw.redraw(Const.State.INVENTORY);
        return;
      case MENU:
        this.menu.buttonNext();
        break;
    }
    this.draw.redraw(state);
  }

  /**
   * Handles escape key event.
   */
  public void keyEscape() {
    switch (state) {
      case DEFAULT:
        // Opens menu
        this.menu = new Exit();
        state = Const.State.MENU;
        break;
      case STORY_BEFORE:
        draw.close();

        if (checkForMonster()) {
          return;
        }

        if (checkForStoryAfter()) {
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

  /**
   * Handles enter key event.
   */
  public void keyEnter() {
    switch (state) {
      case STORY_BEFORE:
        draw.close();

        if (checkForMonster()) {
          return;
        }

        if (checkForStoryAfter()) {
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
        }
        break;
    }
    this.draw.redraw(state);
  }

  /**
   * Handles delete key event.
   */
  public void keyDelete() {
    switch (state) {
      case STORY_BEFORE:
        draw.close();

        if (checkForMonster()) {
          return;
        }

        if (checkForStoryAfter()) {
          break;
        }

        state = Const.State.DEFAULT;
        break;
    }
    this.draw.redraw(state);
  }

  // Boolean methods

  /**
   * Sets state if room has monster.
   *
   * @return whether room has monster
   */
  private Boolean checkForMonster() {
    if (rooms[roomCurrentId].hasMonster()) {
      state = Const.State.COMBAT;
      this.draw.redraw(Const.State.MONSTER);
      return true;
    }
    return false;
  }

  /**
   * Sets state if room has story before monster combat.
   *
   * @return whether room has story before monster combat
   */
  private Boolean checkForStoryBefore() {
    if (rooms[roomCurrentId].hasStoryBefore()) {
      logger.info(Const.LOG_WHITE + ">>> Story before: " + getStoryBefore()
        + Const.LOG_RESET); // DEBUG
      state = Const.State.STORY_BEFORE;
      return true;
    }
    return false;
  }

  /**
   * Sets state if room has story after monster combat.
   *
   * @return whether room has story after monster combat
   */
  private Boolean checkForStoryAfter() {
    if (rooms[roomCurrentId].hasStoryAfter()) {
      logger.info(Const.LOG_WHITE + ">>> Story after: " + getStoryAfter()
        + Const.LOG_RESET); // DEBUG
      state = Const.State.STORY_AFTER;
      return true;
    }
    return false;
  }

  /**
   * Returns whether there is a room in specified direction.
   *
   * @param directionChange - number representing which side newDirection will point to
   * @return whether there is a room
   * @author povolji2
   */
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

  /**
   * @return whether there is a room to the left of the player
   */
  public Boolean hasRoomLeft() {
    return hasRoom(Const.TURN_LEFT);
  }

  /**
   * @return whether there is a room to the right of the player
   */
  public Boolean hasRoomRight() {
    return hasRoom(Const.TURN_RIGHT);
  }

  /**
   * @return whether there is a room in front of the player
   */
  public Boolean hasRoomFront() {
    return hasRoom(Const.DONT_TURN);
  }

  // Loading files

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
            //Weapon weapon = new Weapon(line[1], line[1].substring(0, line[1].lastIndexOf('.')), Integer.parseInt(line[3]));
            //player.takeLoot(weapon);
            //logger.info(Const.LOG_WHITE + ">>> player.getDamage = " + player.getDamage() + Const.LOG_RESET); // DEBUG
            //logger.info(Const.LOG_WHITE + ">>> player.getSprite = " + player.getSprite() + Const.LOG_RESET); // DEBUG
            break;
          // Dungeon rooms
          case ID:
            roomCurrentId = Integer.parseInt(line[1]);
            logger.info(Const.LOG_WHITE + ">>> roomCurrentId = " + roomCurrentId
              + Const.LOG_RESET); // DEBUG
            rooms[roomCurrentId] = new Room();
            logger.info(Const.LOG_WHITE + ">>> isVisited = " + isRoomVisited(roomCurrentId)
              + Const.LOG_RESET); // DEBUG
            break;
          // Story of current room
          case STORY:
            rooms[roomCurrentId].setStoryBefore(line[1].replaceAll("_", " "));
            logger.info(Const.LOG_WHITE + ">>> storyBefore = "
              + rooms[roomCurrentId].getStoryBefore() + Const.LOG_RESET); // DEBUG
            rooms[roomCurrentId].setStoryAfter(line[2].replaceAll("_", " "));
            logger.info(Const.LOG_WHITE + ">>> storyAfter = "
              + rooms[roomCurrentId].getStoryAfter() + Const.LOG_RESET); // DEBUG
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
            //rooms[roomCurrentId].setLoot(line[1], Integer.parseInt(line[2]), player.getHP());
            //logger.info(Const.LOG_WHITE + ">>> room.getLootSprite = " +
              //rooms[roomCurrentId].getLoot().getSprite() + Const.LOG_RESET); // DEBUG
            break;
          // Texture of current room
          case WALL:
            rooms[roomCurrentId].setSprite(line[1]);
            logger.info(Const.LOG_WHITE + ">>> room.sprite = " + rooms[roomCurrentId].getSprite()
              + Const.LOG_RESET); // DEBUG
            break;
          // Current room
          case END:
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

  // GUI

  /**
   * Returns whether room specified by index is visited.
   *
   * @param index - index of room to check
   * @return whther room specified by index is visited
   */
  public Boolean isRoomVisited(Integer index) {
    return rooms[index].isVisited();
  }

  /**
   * Returns index of current room.
   *
   * @return index of current room
   */
  public Integer getRoomId() {
    return roomCurrentId;
  }

  /**
   * Returns sprite of current room.
   *
   * @return current room sprite
   */
  public String getRoomSprite() {
    return rooms[roomCurrentId].getSprite();
  }

  /**
   * Gets story before entering a room.
   *
   * @return story string
   */
  public String getStoryBefore() {
    return rooms[roomCurrentId].getStoryBefore();
  }

  /**
   * Gets story after killing a monster.
   *
   * @return story string
   */
  public String getStoryAfter() {
    return rooms[roomCurrentId].getStoryAfter();
  }

  /**
   * Gets monster instance.
   *
   * @return monster instance
   */
  public Monster getMonster() {
    return rooms[roomCurrentId].getMonster();
  }

  /**
   * Returns current direction converted to String.
   *
   * @return current direction converted to String
   */
  public String getDirection() {
    return direction.toString();
  }

  /**
   * Returns left direction relative to current direction converted to String.
   *
   * @return left direction relative to current direction converted to String
   */
  public String getLeftDirection() {
    return changeDirection(Const.TURN_LEFT).toString();
  }

  /**
   * Returns right direction relative to current direction converted to String.
   *
   * @return right direction relative to current direction converted to String
   */
  public String getRightDirection() {
    return changeDirection(Const.TURN_RIGHT).toString();
  }

  /**
   * Following methods are connecting Exit menu with GameDraw object.
   */

  /** @see Layout */
  public String getMenuAction(Integer index) {
    return this.menu.getAction(index);
  }

  /** @see Layout */
  public Integer getMenuActive() {
    return this.menu.getActive();
  }

  /** @see Layout */
  public Integer getMenuCount() {
    return this.menu.getCount();
  }

  /**
   * Following methods are described in Entity, Player and Monster classes.
   */

  /** @see Player */
  public Const.ItemType getActiveItem() {
    return player.getActiveItem();
  }

  /** @see Player */
  public void itemNext() {
    player.itemNext();
  }

  /** @see Player */
  public void itemPrevious() {
    player.itemPrevious();
  }

  /** @see Entity */
  public Integer getPlayerHP() {
    return player.getHP();
  }

  /** @see Entity */
  public Integer getPlayerMaxHP() {
    return player.getMaxHP();
  }

  /** @see Player */
  public Integer getWeaponDamage() {
    return player.getDamage();
  }

  /** @see Player */
  public Integer getBombCount() {
    return player.getBombCount();
  }

  /** @see Player */
  public Integer getPotionCount() {
    return player.getPotionCount();
  }

  /** @see Player */
  public String getWeaponSprite() {
    return player.getSprite();
  }

  /** @see Entity */
  public Integer getMonsterHP() {
    return getMonster().getHP();
  }

  /** @see Entity */
  public Integer getMonsterMaxHP() {
    return getMonster().getMaxHP();
  }

  /** @see Monster */
  public Integer getMonsterDamage() {
    return getMonster().getDamage();
  }

  /** @see Monster */
  public String getMonsterSprite() {
    return getMonster().getSprite();
  }
}

