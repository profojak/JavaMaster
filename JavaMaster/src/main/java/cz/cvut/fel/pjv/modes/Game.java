package cz.cvut.fel.pjv.modes;

import cz.cvut.fel.pjv.Root;
import cz.cvut.fel.pjv.modes.draw.Draw;
import cz.cvut.fel.pjv.modes.draw.GameDraw;
import cz.cvut.fel.pjv.entities.Player;
import cz.cvut.fel.pjv.room.Room;
import cz.cvut.fel.pjv.inventory.Loot;
import cz.cvut.fel.pjv.menu.layouts.Layout;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;

import javafx.scene.canvas.GraphicsContext;

import java.util.Objects;
import java.util.logging.Logger;
import java.util.logging.Level;

/** @see Mode */
public class Game implements Mode {
  enum Item {
    BOMB,
    POTION,
    WEAPON
  }

  enum Direction {
    NORTH,
    EAST,
    SOUTH,
    WEST
  }

  enum Load {
    START,
    PLAYER,
    ID,
    STORY,
    MONSTER,
    LOOT,
    WALL,
    END
  }

  private final int NUMBER_OF_ROOMS = 35, MAP_WIDTH = 5,
    NUMBER_OF_DIRECTIONS = Direction.values().length, DONT_TURN = 0, TURN_RIGHT = 1,
    TURN_LEFT = NUMBER_OF_DIRECTIONS - 1, GO_NORTH = -MAP_WIDTH, GO_EAST = 1, GO_SOUTH = MAP_WIDTH,
    GO_WEST = -1, WESTERN_BORDER = 0, EASTERN_BORDER = MAP_WIDTH - 1,
    NORTHERN_BORDER = MAP_WIDTH - 1, SOUTHERN_BORDER = NUMBER_OF_ROOMS - MAP_WIDTH;
  private final String RED = "\u001B[31m", WHITE = "\u001B[37m", YELLOW = "\u001B[33m",
    RESET = "\u001B[0m", PART_ROOM = "ROOM", PART_MONSTER = "MONSTER";
  private Root root;
  private Draw draw;
  private Player player;
  private Room[] rooms = new Room[NUMBER_OF_ROOMS];
  private Integer roomStartId, roomEndId, roomCurrentId;
  private Direction direction = Direction.NORTH;
  private static final Logger logger = Logger.getLogger(Game.class.getName());

  /**
   * @param gc - GraphicsContext to draw images to
   * @param root - parent object
   */
  public Game(GraphicsContext gc, Root root) {
    this.root = root;
    player = new Player();
    File saveFile = this.root.getFile();
    parseSaveFile(saveFile);
    this.draw = new GameDraw(gc, this);
  }

  /**
   * @deprecated use Game(String) constructor instead
   */
  @Deprecated
  public Game() {
  }

  /**
   * Returns changed direction.
   *
   * @param directionChange - number representing to which side to turn
   * @return changed direction
   * @author povolji2
   */
  private Direction changeDirection(int directionChange) {
    Direction newDirection = null;
    try {
      int directionIndex = (direction.ordinal() + directionChange) % NUMBER_OF_DIRECTIONS;
      newDirection = Direction.values()[directionIndex];
    } catch (Exception exception) {
      logger.log(Level.SEVERE, RED + ">>>  Error: Unexpected direction value: " + newDirection +
        RESET, exception); // ERROR
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
    logger.info(WHITE + ">>> Room index: " + roomCurrentId + ", direction: " + direction + RESET); // DEBUG
    this.draw.redraw(PART_ROOM);
  }

  // Key methods

  /**
   * Moves the player forward if possible.
   *
   * @author povolji2
   */
  public void keyUp() {
    if (hasRoomFront()) {
      switch (direction) {
        case NORTH:
          roomCurrentId += GO_NORTH;
          break;
        case EAST:
          roomCurrentId += GO_EAST;
          break;
        case SOUTH:
          roomCurrentId += GO_SOUTH;
          break;
        case WEST:
          roomCurrentId += GO_WEST;
          break;
        default:
          logger.severe(RED + ">>>  Error: Unexpected direction value: " + direction + RESET); // ERROR
          return;
      }

      logger.info(WHITE + ">>> Room index: " + roomCurrentId + ", direction: " + direction +
        RESET); // DEBUG
      this.draw.redraw(PART_ROOM);

      if (!rooms[roomCurrentId].isVisited()) {
        rooms[roomCurrentId].setVisited();
        // Story and Monster
        if (rooms[roomCurrentId].hasStoryBefore()) {
          logger.info(WHITE + ">>> Story before: " + rooms[roomCurrentId].getStoryBefore() +
            RESET); // DEBUG
        }

        // TODO Start fight
        if (rooms[roomCurrentId].hasMonster()) {
          this.draw.redraw(PART_MONSTER);

        }

        if (rooms[roomCurrentId].hasStoryAfter()) {
          logger.info(WHITE + ">>> Story after: " + rooms[roomCurrentId].getStoryAfter() + RESET); // DEBUG
        }

        // TODO Better message
        if (rooms[roomCurrentId].hasLoot()) {
          Loot loot = rooms[roomCurrentId].getLoot();
          logger.info(WHITE + ">>> You have found loot: " + loot.getCount() + " instance/s of " +
            loot.getSprite() + RESET); // DEBUG
          player.takeLoot(loot);
        }
      }

      if (roomCurrentId.equals(roomEndId)) {
        logger.info(WHITE + ">>> You have entered the End room on this floor" + RESET); // DEBUG
      }
    } else {
      logger.warning(YELLOW + ">>>  You can't go there." + RESET); // DEBUG
    }
  }

  public void keyDown() {
  }

  /**
   * Turns the player to the left.
   *
   * @author povolji2
   */
  public void keyLeft() {
    turnPlayer(TURN_LEFT);
  }

  /**
   * Turns the player to the right.
   *
   * @author povolji2
   */
  public void keyRight() {
    turnPlayer(TURN_RIGHT);
  }

  /**
   * Exits the program.
   */
  public void keyEscape() {
    System.exit(0);
  }

  public void keyEnter() {
  }

  public void keyDelete() {
  }

  // Boolean methods

  /**
   * Returns whether there is a room in specified direction.
   *
   * @param directionChange - number representing which side newDirection will point to
   * @return whether there is a room
   */
  public Boolean hasRoom(int directionChange) {
    Direction newDirection = direction;
    if (directionChange != 0) {
      newDirection = changeDirection(directionChange);
    }

    try {
      switch (Objects.requireNonNull(newDirection)) {
        case NORTH:
          return roomCurrentId > NORTHERN_BORDER && rooms[roomCurrentId + GO_NORTH] != null;
        case EAST:
          return roomCurrentId % MAP_WIDTH != EASTERN_BORDER && rooms[roomCurrentId + GO_EAST] !=
            null;
        case SOUTH:
          return roomCurrentId < SOUTHERN_BORDER && rooms[roomCurrentId + GO_SOUTH] != null;
        case WEST:
          return roomCurrentId % MAP_WIDTH != WESTERN_BORDER && rooms[roomCurrentId + GO_WEST] !=
            null;
      }
    } catch (Exception exception) {
      logger.log(Level.SEVERE, RED + ">>>  Error: Unexpected direction value: " + newDirection +
        RESET, exception); // ERROR
      return null;
    }
    return null;
  }

  /**
   * @return whether there is a room to the left of the player
   * @author povolji2
   */
  public Boolean hasRoomLeft() {
    return hasRoom(TURN_LEFT);
  }

  /**
   * @return whether there is a room to the right of the player
   * @author povolji2
   */
  public Boolean hasRoomRight() {
    return hasRoom(TURN_RIGHT);
  }

  /**
   * @return whether there is a room in front of the player
   * @author povolji2
   */
  public Boolean hasRoomFront() {
    return hasRoom(DONT_TURN);
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
        Load load = Load.valueOf(line[0].toUpperCase());

        switch (load) {
          // Room id where the dungeon starts and ends
          case START:
            roomStartId = Integer.parseInt(line[1]);
            logger.info(WHITE + ">>> roomStartId = " + roomStartId + RESET); // DEBUG
            roomEndId = Integer.parseInt(line[2]);
            logger.info(WHITE + ">>> roomEndId = " + roomEndId + RESET); // DEBUG
            break;
          // Player variables
          case PLAYER:
            player.setHp(Integer.parseInt(line[2]));
            logger.info(WHITE + ">>> player = " + player.getHp() + RESET); // DEBUG
            Loot weapon = new Loot(line[1], Integer.parseInt(line[3]));
            player.takeLoot(weapon);
            logger.info(WHITE + ">>> player.getDamage = " + player.getDamage() + RESET); // DEBUG
            logger.info(WHITE + ">>> player.getSprite = " + player.getSprite() + RESET); // DEBUG
            break;
          // Dungeon rooms
          case ID:
            roomCurrentId = Integer.parseInt(line[1]);
            logger.info(WHITE + ">>> roomCurrentId = " + roomCurrentId + RESET); // DEBUG
            rooms[roomCurrentId] = new Room();
            logger.info(WHITE + ">>> isVisited = " + rooms[roomCurrentId].isVisited() + RESET); // DEBUG
            break;
          // Story of current room
          case STORY:
            rooms[roomCurrentId].setStoryBefore(line[1].replaceAll("_", " "));
            logger.info(WHITE + ">>> storyBefore = " + rooms[roomCurrentId].getStoryBefore() +
              RESET); // DEBUG
            rooms[roomCurrentId].setStoryAfter(line[2].replaceAll("_", " "));
            logger.info(WHITE + ">>> storyAfter = " + rooms[roomCurrentId].getStoryAfter() +
              RESET); // DEBUG
            break;
          // Monster in current room
          case MONSTER:
            rooms[roomCurrentId].setMonster(line[1],
              Integer.parseInt(line[2]), Integer.parseInt(line[3]));
            logger.info(WHITE + ">>> room.getMonsterSprite = " +
              rooms[roomCurrentId].getMonsterSprite() + RESET); // DEBUG
            break;
          // Loot in current room
          case LOOT:
            rooms[roomCurrentId].setLoot(line[1], Integer.parseInt(line[2]));
            logger.info(WHITE + ">>> room.getLootSprite = " +
              rooms[roomCurrentId].getLoot().getSprite() + RESET); // DEBUG
            break;
          // Texture of current room
          case WALL:
            rooms[roomCurrentId].setSprite(line[1]);
            logger.info(WHITE + ">>> room.sprite = " + rooms[roomCurrentId].getSprite() + RESET); // DEBUG
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
      logger.log(Level.SEVERE, RED + "File could not be loaded." + RESET, exception);
      return false; // File could not be loaded
    }
    return true;
  }

  // GUI

  /**
   * Returns index of current room.
   *
   * @return index of current room
   */
  public Integer getRoomId() {
    return roomCurrentId;
  }

  /**
   * Returns current direction converted to String.
   *
   * @return current direction converted to String
   */
  public String getDirection() {
    return direction.toString();
  }
}

