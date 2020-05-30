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
import java.util.Objects;
import java.util.logging.Logger;
import java.util.logging.Level;

import javafx.scene.layout.StackPane;

/**
 * Class implementing Game.
 *
 * <p>This mode is loaded when user wants to play.
 *
 * @see Mode
 */
public class Game implements Mode {
  private static final Logger logger = Logger.getLogger(Game.class.getName());
  private final Root root;
  private final Draw draw;

  private Player player;
  private Room[] rooms = new Room[Const.NUMBER_OF_ROOMS];
  private Integer roomStartId, roomEndId, roomCurrentId;
  private File saveFile, nextMap;
  private Const.Direction direction = Const.Direction.NORTH;
  private Layout menu;
  private Const.State state = Const.State.DEFAULT;

  /**
   * @param stack - StackPane to draw images to
   * @param root - parent object
   * @author profojak
   */
  public Game(StackPane stack, Root root, File saveFile) {
    this.root = root;
    this.player = new Player();
    this.saveFile = saveFile;

    parseSaveFile(saveFile);
    this.draw = new GameDraw(stack, this);
  }

  /**
   * Used in UnitTests.
   * @author povolji2
   */  
  public Game(File saveFile) {
    this.root = null;
    this.player = new Player();
    this.draw = null;
    if (saveFile != null && saveFile.canRead()) {
      this.saveFile = saveFile;
      parseSaveFile(saveFile);
    }
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

  private void redraw(Const.State state) {
    if (this.draw != null) {
      this.draw.redraw(state);
    }
  }

  /**
   * Called when moving to new level.
   *
   * @author profojak
   */
  private void changeLevel(File nextMap) {
    close();

    player = null;
    menu = null;
    rooms = null;

    rooms = new Room[Const.NUMBER_OF_ROOMS];
    player = new Player();
    roomStartId = 0;
    roomEndId = 0;
    roomCurrentId = 0;
    direction = Const.Direction.NORTH;
    state = Const.State.DEFAULT;
    parseSaveFile(nextMap);
    redraw(Const.State.LOAD);
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

  /**
   * Takes loot.
   *
   * @author povolji2
   */
  private void takeLoot() {
    Item loot = rooms[roomCurrentId].getLoot();
    logger.info(Const.LOG_WHITE + ">>> You have found loot: " + loot.getName() + Const.LOG_RESET); // DEBUG
    player.takeLoot(loot);
  }

  // Key methods

  /**
   * @see Mode
   * @author profojak, povolji2
   */
  public void keyUp() {
    switch (state) {
      /* Walking */
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
            redraw(Const.State.DEFAULT);
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
      /* Combat */
      case COMBAT:
        itemPrevious();
        redraw(Const.State.INVENTORY);
        return;
      /* Loot */
      case LOOT:
        state = Const.State.DEFAULT;
        break;
      /* Story before */
      case STORY_BEFORE:
        close();

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
      /* Story after */
      case STORY_AFTER:
        close();

        if (checkForLoot()) {
          break;
        }

        if (checkForFloorEnd()) {
          break;
        }

        state = Const.State.DEFAULT;
        break;
      /* Menu */
      case MENU:
        this.menu.buttonPrevious();
        break;
    }
    redraw(state);
  }

  /**
   * @see Mode
   * @author profojak, povolji2
   */
  public void keyDown() {
    switch (state) {
      /* Combat */
      case COMBAT:
        itemNext();
        redraw(Const.State.INVENTORY);
        return;
      /* Loot */
      case LOOT:
        state = Const.State.DEFAULT;
        break;
      /* Story before */
      case STORY_BEFORE:
        close();

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
      /* Story after */
      case STORY_AFTER:
        close();

        if (checkForLoot()) {
          break;
        }

        if (checkForFloorEnd()) {
          break;
        }

        state = Const.State.DEFAULT;
        break;
      /* Menu */
      case MENU:
        this.menu.buttonNext();
        break;
    }
    redraw(state);
  }

  /**
   * @see Mode
   * @author profojak, povolji2
   */
  public void keyLeft() {
    switch (state) {
      /* Walking */
      case DEFAULT:
        // Turns player to the left
        turnPlayer(Const.TURN_LEFT);
        break;
      /* Combat */
      case COMBAT:
        itemPrevious();
        redraw(Const.State.INVENTORY);
        return;
      /* Loot */
      case LOOT:
        state = Const.State.DEFAULT;
        break;
      /* Story before */
      case STORY_BEFORE:
        close();

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
      /* Story after */
      case STORY_AFTER:
        close();

        if (checkForLoot()) {
          break;
        }

        if (checkForFloorEnd()) {
          break;
        }

        state = Const.State.DEFAULT;
        break;
      /* Menu */
      case MENU:
        this.menu.buttonPrevious();
        break;
    }
    redraw(state);
  }

  /**
   * @see Mode
   * @author profojak, povolji2
   */
  public void keyRight() {
    switch (state) {
      /* Walking */
      case DEFAULT:
        // Turns player to the right
        turnPlayer(Const.TURN_RIGHT);
        break;
      /* Combat */
      case COMBAT:
        itemNext();
        redraw(Const.State.INVENTORY);
        return;
      /* Loot */
      case LOOT:
        state = Const.State.DEFAULT;
        break;
      /* Story before */
      case STORY_BEFORE:
        close();

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
      /* Story after */
      case STORY_AFTER:
        close();

        if (checkForLoot()) {
          break;
        }

        if (checkForFloorEnd()) {
          break;
        }

        state = Const.State.DEFAULT;
        break;
      /* Menu */
      case MENU:
        this.menu.buttonNext();
        break;
    }
    redraw(state);
  }

  /**
   * @see Mode
   * @author profojak, povolji2
   */
  public void keyEscape() {
    switch (state) {
      /* Walking */
      case DEFAULT:
        // Opens menu
        this.menu = new Exit();
        state = Const.State.MENU;
        break;
      /* Combat */
      case COMBAT:
        return;
      /* Loot */
      case LOOT:
        state = Const.State.DEFAULT;
        break;
      /* Story before */
      case STORY_BEFORE:
        close();

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
      /* Story after */
      case STORY_AFTER:
        close();

        if (checkForLoot()) {
          break;
        }

        if (checkForFloorEnd()) {
          break;
        }

        state = Const.State.DEFAULT;
        break;
      /* Menu */
      case MENU:
        // Closes menu
        this.menu = null;
        state = Const.State.DEFAULT;
        break;
    }
    redraw(state);
  }

  /**
   * @see Mode
   * @author profojak, povolji2
   */
  public void keyEnter() {
    switch (state) {
      /* Victory */
      case VICTORY:
        close();
        this.root.switchMode(Const.MENU_MAINMENU);
        break;
      /* Death */
      case DEATH:
        close();
        changeLevel(saveFile);
        break;
      /* Combat */
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
        // Player is dead
        if (getPlayerHP() <= 0) {
          state = Const.State.DEATH;
          break;
        }

        // Monster is dead
        if (getMonsterHP() <= 0) {
          redraw(state);

          if (checkForStoryAfter()) {
            break;
          }

          if (checkForLoot()) {
            break;
          }
        }
        break;
      /* Loot */
      case LOOT:
        state = Const.State.DEFAULT;
        break;
      /* Story before */
      case STORY_BEFORE:
        close();

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
      /* Story after */
      case STORY_AFTER:
        close();

        if (checkForLoot()) {
          break;
        }

        if (checkForFloorEnd()) {
          break;
        }

        state = Const.State.DEFAULT;
        break;
      /* Menu */
      case MENU:
        // Cancel
        if (this.menu.getAction(this.menu.getActive()).equals(Const.MENU_CANCEL)) {
          this.menu = null;
          state = Const.State.DEFAULT;
        // Exit
        } else if (this.menu.getAction(this.menu.getActive()).equals(Const.MENU_EXIT)) {
          close();
          this.root.switchMode(Const.MENU_MAINMENU);
        // Descend
        } else if (this.menu.getAction(this.menu.getActive()).equals(Const.MENU_DESCEND)) {
          if (!hasNextMap()) {
            state = Const.State.VICTORY;
            redraw(state);
            return;
          }
          changeLevel(nextMap);
          return;
        // Not yet
        } else if (this.menu.getAction(this.menu.getActive()).equals(Const.MENU_NOT_YET)) {
          this.menu = null;
          state = Const.State.DEFAULT;
        }
        break;
    }
    redraw(state);
  }

  /**
   * @see Mode
   * @author profojak, povolji2
   */
  public void keyDelete() {
    switch (state) {
      /* Combat */
      case COMBAT:
        return;
      /* Loot */
      case LOOT:
        state = Const.State.DEFAULT;
        break;
      /* Story before */
      case STORY_BEFORE:
        close();

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
      /* Story after */
      case STORY_AFTER:
        close();

        if (checkForLoot()) {
          break;
        }

        if (checkForFloorEnd()) {
          break;
        }

        state = Const.State.DEFAULT;
        break;
    }
    redraw(state);
  }

  // Boolean methods

  /**
   * Sets state if room has monster.
   *
   * @return whether room has monster
   * @author profojak, povolji2
   */
  private Boolean checkForMonster() {
    if (rooms[roomCurrentId].hasMonster()) {
      state = Const.State.COMBAT;
      redraw(Const.State.MONSTER);
      return true;
    }
    return false;
  }

  /**
   * Sets state if room is the end of dungeon floor.
   *
   * @return whether room is the end of dungeon floor
   * @author profojak, povolji2
   */
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

  /**
   * Sets state if room has story before monster combat.
   *
   * @return whether room has story before monster combat
   * @author profojak, povolji2
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
   * @author profojak, povolji2
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
   * Sets state if room has loot.
   *
   * @return whether room has loot.
   * @author profojak, povolji2
   */
  private Boolean checkForLoot() {
    if (rooms[roomCurrentId].hasLoot()) {
      logger.info(Const.LOG_WHITE + ">>> Room has Loot!" + Const.LOG_RESET); // DEBUG
      takeLoot();
      state = Const.State.LOOT;
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
  private Boolean hasRoom(int directionChange) {
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
   * Returns whether there is a room to the left of the player.
   *
   * @return whether there is a room to the left of the player
   * @author povolji2
   */
  public Boolean hasRoomLeft() {
    return hasRoom(Const.TURN_LEFT);
  }

  /**
   * Returns whether there is a room to the right of the player.
   *
   * @return whether there is a room to the right of the player
   * @author povolji2
   */
  public Boolean hasRoomRight() {
    return hasRoom(Const.TURN_RIGHT);
  }

  /**
   * Returns whether there is a room in front of the player.
   *
   * @return whether there is a room in front of the player
   * @author povolji2
   */
  public Boolean hasRoomFront() {
    return hasRoom(Const.DONT_TURN);
  }

  /**
   * Returns whether there is a room behind the player.
   *
   * @return whether there is a room behind the player
   * @author povolji2
   */
  public Boolean hasRoomBehind() {
    return hasRoom(Const.TURN_RIGHT + Const.TURN_RIGHT);
  }

  /**
   * Returns whether there is a room behind the player.
   *
   * @return whether there is a room behind the player
   * @author povolji2
   */
  public Boolean hasNextMap() {
    return nextMap != null;
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
   * @author profojak, povolji2
   */
  private Boolean parseSaveFile(File parsedFile) {
    try {
      BufferedReader saveReader = new BufferedReader(new FileReader(parsedFile));
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
            logger.info(Const.LOG_WHITE + ">>> isVisited = " + isRoomVisited(roomCurrentId)
              + Const.LOG_RESET); // DEBUG
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
            } else {
              nextMap = null;
            }
            roomCurrentId = roomStartId;
            rooms[roomCurrentId].setVisited();
            break;
          // Wrong file format!
          default:
            saveReader.close();
            return false;
        }
      }
      saveReader.close();
    } catch (Exception exception) {
      logger.log(Level.SEVERE, Const.LOG_RED + "File could not be loaded."
        + Const.LOG_RESET, exception); // ERROR
      return false; // File could not be loaded
    }
    return true;
  }

  // Getters

  /**
   * Returns whether room specified by index is visited.
   *
   * @param index - index of room to check
   * @return whther room specified by index is visited
   * @author povolji2
   */
  public Boolean isRoomVisited(Integer index) {
    return rooms[index].isVisited();
  }

  /**
   * Returns index of current room.
   *
   * @return index of current room
   * @author povolji2
   */
  public Integer getRoomId() {
    return roomCurrentId;
  }

  /**
   * Returns sprite of current room.
   *
   * @return current room sprite
   * @author povolji2
   */
  public String getRoomSprite() {
    return rooms[roomCurrentId].getSprite();
  }

  /**
   * Gets story before entering a room.
   *
   * @return story string
   * @author povolji2
   */
  public String getStoryBefore() {
    return rooms[roomCurrentId].getStoryBefore();
  }

  /**
   * Gets story after killing a monster.
   *
   * @return story string
   * @author povolji2
   */
  public String getStoryAfter() {
    return rooms[roomCurrentId].getStoryAfter();
  }

  /**
   * Gets monster instance.
   *
   * @return monster instance
   * @author povolji2
   */
  public Monster getMonster() {
    return rooms[roomCurrentId].getMonster();
  }

  /**
   * Gets Loot type.
   *
   * @return type of Loot.
   * @author profojak
   */
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

  /**
   * Gets loot count.
   *
   * @return count of loot
   * @author profojak
   */
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

  /**
   * Returns current direction converted to String.
   *
   * @return current direction converted to String
   * @author povolji2
   */
  public String getDirection() {
    return direction.toString();
  }

  /**
   * Returns left direction relative to current direction converted to String.
   *
   * @return left direction relative to current direction converted to String
   * @author povolji2
   */
  public String getLeftDirection() {
    return changeDirection(Const.TURN_LEFT).toString();
  }

  /**
   * Returns right direction relative to current direction converted to String.
   *
   * @return right direction relative to current direction converted to String
   * @author povolji2
   */
  public String getRightDirection() {
    return changeDirection(Const.TURN_RIGHT).toString();
  }

  /**
   * Returns right direction relative to current direction converted to String.
   *
   * @return right direction relative to current direction converted to String
   * @author povolji2
   */
  public String getBackDirection() {
    return changeDirection(Const.TURN_RIGHT + Const.TURN_RIGHT).toString();
  }

  /**
   * Following methods are connecting Exit menu with GameDraw object.
   */

  /**
   * @see Layout
   */
  public String getMenuAction(Integer index) {
    return this.menu.getAction(index);
  }

  /**
   * @see Layout
   */
  public Integer getMenuActive() {
    return this.menu.getActive();
  }

  /**
   * @see Layout
   */
  public Integer getMenuCount() {
    return this.menu.getCount();
  }

  // Following methods are described in Entity, Player and Monster classes.

  /**
   * @see Player
   */
  public Const.ItemType getActiveItem() {
    return player.getActiveItem();
  }

  /**
   * @see Player
   */
  public void itemNext() {
    player.itemNext();
  }

  /**
   * @see Player
   */
  public void itemPrevious() {
    player.itemPrevious();
  }

  /**
   * @see Player
   */
  public Integer getBombDamage() {
    return player.getBombDamage();
  }

  /**
   * @see Player
   */
  public Integer getPotionHeal() {
    return player.getPotionHeal();
  }

  /**
   * @see Player
   */
  public Integer getPlayerHP() {
    return player.getHP();
  }

  /**
   * @see Player
   */
  public Integer getPlayerMaxHP() {
    return player.getMaxHP();
  }

  /**
   * @see Player
   */
  public Integer getWeaponDamage() {
    return player.getDamage();
  }

  /**
   * @see Player
   */
  public Integer getBombCount() {
    return player.getBombCount();
  }

  /**
   * @see Player
   */
  public Integer getPotionCount() {
    return player.getPotionCount();
  }

  /**
   * @see Player
   */
  public String getWeaponSprite() {
    return player.getSprite();
  }

  /**
   * @see Monster
   */
  public Integer getMonsterHP() {
    return getMonster().getHP();
  }

  /**
   * @see Monster
   */
  public Integer getMonsterMaxHP() {
    return getMonster().getMaxHP();
  }

  /**
   * @see Monster
   */
  public Integer getMonsterDamage() {
    return getMonster().getDamage();
  }

  /**
   * @see Monster
   */
  public String getMonsterSprite() {
    return getMonster().getSprite();
  }

  /**
   * @see Mode
   */
  public void close() {
    if (this.draw != null) {
      this.draw.close();
    }
  }
}

