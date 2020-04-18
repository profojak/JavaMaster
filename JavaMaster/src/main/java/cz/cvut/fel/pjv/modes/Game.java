package cz.cvut.fel.pjv.modes;

import cz.cvut.fel.pjv.Root;
import cz.cvut.fel.pjv.modes.draw.Draw;
import cz.cvut.fel.pjv.modes.draw.GameDraw;
import cz.cvut.fel.pjv.entities.Player;
import cz.cvut.fel.pjv.room.Room;
import cz.cvut.fel.pjv.menu.layouts.Layout;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;

import javafx.scene.canvas.GraphicsContext;

/** @see Mode */
public class Game implements Mode {
  private Root root;
  private Draw draw;
  private Player player;
  private Room[] rooms = new Room[35];
  private Integer roomStartId, roomEndId, roomCurrentId;
  String direction;

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

  // Key methods

  /**
   * Moves the player forward if possible.
   *
   * @author povolji2
   */
  public void keyUp() {
    if (hasRoomFront()) {
      switch (direction) {
        case "UP":
          roomCurrentId -= 5;
          break;
        case "RIGHT":
          roomCurrentId += 1;
          break;
        case "DOWN":
          roomCurrentId += 5;
          break;
        case "LEFT":
          roomCurrentId -= 1;
          break;
        default:
          System.out.println(">>>  Error: Unexpected direction value: " + direction); // ERROR
          return;
      }

      System.out.println(">>> Room index: " + roomCurrentId + ", direction: " + direction); // DEBUG

      this.draw.redraw("room");
      if (!rooms[roomCurrentId].isVisited()) {
        rooms[roomCurrentId].setVisited();
        // Story
              // if room has story before entering, then
        System.out.println(">>> Story before: " + rooms[roomCurrentId].getStoryBefore()); // DEBUG
              // if room has story after interacting, then
        System.out.println(">>> Story after: " + rooms[roomCurrentId].getStoryAfter()); // DEBUG
      }
    } else {
      System.out.println(">>>  You can't go there."); // DEBUG
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
    switch (direction) {
      case "UP":
        direction = "LEFT";
        break;
      case "RIGHT":
        direction = "UP";
        break;
      case "DOWN":
        direction = "RIGHT";
        break;
      case "LEFT":
        direction = "DOWN";
        break;
      default:
        System.out.println(">>>  Error: Unexpected direction value: " + direction); // ERROR
        return;
    }

    System.out.println(">>> Room index: " + roomCurrentId + ", direction: " + direction); // DEBUG

    this.draw.redraw("room");
  }

  /**
   * Turns the player to the right.
   *
   * @author povolji2
   */
  public void keyRight() {
    switch (direction) {
      case "UP":
        direction = "RIGHT";
        break;
      case "RIGHT":
        direction = "DOWN";
        break;
      case "DOWN":
        direction = "LEFT";
        break;
      case "LEFT":
        direction = "UP";
        break;
      default:
        System.out.println(">>>  Error: Unexpected direction value: " + direction); // ERROR
        return;
    }

    System.out.println(">>> Room index: " + roomCurrentId + ", direction: " + direction); // DEBUG

    this.draw.redraw("room");
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
   * @return whether there is a room to the left of the player
   * @author povolji2
   */
  public Boolean hasRoomLeft() {
    switch (direction) {
      case "UP":
        return roomCurrentId % 5 != 0 && rooms[roomCurrentId - 1] != null;
      case "RIGHT":
        return roomCurrentId > 4 && rooms[roomCurrentId - 5] != null;
      case "DOWN":
        return roomCurrentId % 5 != 4 && rooms[roomCurrentId + 1] != null;
      case "LEFT":
        return roomCurrentId < 30 && rooms[roomCurrentId + 5] != null;
      default:
        System.out.println(">>>  Error: Unexpected direction value: " + direction); // ERROR
        return false;
    }
  }

  /**
   * @return whether there is a room to the right of the player
   * @author povolji2
   */
  public Boolean hasRoomRight() {
    switch (direction) {
      case "UP":
        return roomCurrentId % 5 != 4 && rooms[roomCurrentId + 1] != null;
      case "RIGHT":
        return roomCurrentId < 30 && rooms[roomCurrentId + 5] != null;
      case "DOWN":
        return roomCurrentId % 5 != 0 && rooms[roomCurrentId - 1] != null;
      case "LEFT":
        return roomCurrentId > 4 && rooms[roomCurrentId - 5] != null;
      default:
        System.out.println(">>>  Error: Unexpected direction value: " + direction); // ERROR
        return false;
    }
  }

  /**
   * @return whether there is a room in front of the player
   * @author povolji2
   */
  public Boolean hasRoomFront() {
    switch (direction) {
      case "UP":
        return roomCurrentId > 4 && rooms[roomCurrentId - 5] != null;
      case "RIGHT":
        return roomCurrentId % 5 != 4 && rooms[roomCurrentId + 1] != null;
      case "DOWN":
        return roomCurrentId < 30 && rooms[roomCurrentId + 5] != null;
      case "LEFT":
        return roomCurrentId % 5 != 0 && rooms[roomCurrentId - 1] != null;
      default:
        System.out.println(">>>  Error: Unexpected direction value: " + direction); // ERROR
        return false;
    }
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

        switch (line[0]) {
          // Room id where the dungeon starts and ends
          case "start":
            roomStartId = Integer.parseInt(line[1]);
            System.out.println(">>> roomStartId = " + roomStartId); // DEBUG
            roomEndId = Integer.parseInt(line[2]);
            System.out.println(">>> roomEndId = " + roomEndId); // DEBUG
            break;
          // Player variables
          case "player":
            player.setHp(Integer.parseInt(line[2]));
            System.out.println(">>> player = " + player.getHp()); // DEBUG
            player.takeLoot(line[1], Integer.parseInt(line[3]));
            System.out.println(">>> player.getDamage = " + player.getDamage()); // DEBUG
            System.out.println(">>> player.getSprite = " + player.getSprite()); // DEBUG
            break;
          // Dungeon rooms
          case "id":
            roomCurrentId = Integer.parseInt(line[1]);
            System.out.println(">>> roomCurrentId = " + roomCurrentId); // DEBUG
            rooms[roomCurrentId] = new Room();
            System.out.println(">>> isVisited = " + rooms[roomCurrentId].isVisited()); // DEBUG
            break;
          // Story of current room
          case "story":
            rooms[roomCurrentId].setStoryBefore(line[1].replaceAll("_", " "));
            System.out.println(">>> storyBefore = " + rooms[roomCurrentId].getStoryBefore()); // DEBUG
            rooms[roomCurrentId].setStoryAfter(line[2].replaceAll("_", " "));
            System.out.println(">>> storyAfter = " + rooms[roomCurrentId].getStoryAfter()); // DEBUG
            break;
          // Monster in current room
          case "monster":
            rooms[roomCurrentId].setMonster(line[1],
              Integer.parseInt(line[2]), Integer.parseInt(line[3]));
            System.out.println(">>> room.getMonsterSprite = " +
              rooms[roomCurrentId].getMonsterSprite()); // DEBUG
            break;
          // Loot in current room
          case "loot":
            rooms[roomCurrentId].setLoot(line[1], Integer.parseInt(line[2]));
            System.out.println(">>> room.getLootSprite = "
              + rooms[roomCurrentId].getLootSprite()); // DEBUG
            break;
          // Texture of current room
          case "wall":
            rooms[roomCurrentId].setSprite(line[1]);
            System.out.println(">>> room.sprite = " + rooms[roomCurrentId].getSprite()); // DEBUG
            break;
          // Current room and direction
          case "end":
            roomCurrentId = roomStartId;
            direction = "UP";
            break;
          // Wrong file format!
          default:
            return false;
        }
      }
    } catch (Exception exception) {
      System.out.println(exception);
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

  public String getDirection() {
    return direction;
  }
}

