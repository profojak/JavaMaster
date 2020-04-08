package cz.cvut.fel.pjv.modes;

import cz.cvut.fel.pjv.entities.Player;
import cz.cvut.fel.pjv.room.Room;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

/** @see Mode */
public class Game implements Mode {
  private Player player;
  private Room[] rooms = new Room[35];
  private Integer roomStartId, roomEndId, roomCurrentId;

  // Constructor and destructor

  /**
   * Constructor.
   *
   * @param saveName - dungeon to be parsed, saved in .dung file
   */
  public Game(String saveName) {
    player = new Player();
    parseSaveFile(saveName);
  }

  /**
   * Constructor.
   *
   * @deprecated use Game(String) constructor instead
   */
  @Deprecated
  public Game() {
  }

  /**
   * Destructor, handles proper unloading of class instance.
   */
  public void close() {
  }

  // Key methods

  public void keyUp() {
  }

  public void keyDown() {
  }

  public void keyLeft() {
  }

  public void keyRight() {
  }

  public void keyEscape() {
  }

  public void keyEnter() {
  }

  public void keyDelete() {
  }

  // Loading files

  /**
   * Parses save file and prepares the dungeon.
   *
   * <p>This method opens .dung text file from resources/saves/ directory and creates instances
   * of described classes. After this method is finished, Game object is ready for playthrough.
   *
   * @return whether parsing was succesful
   * @param saveName - dungeon to be parsed, saved in .dung file
   * @author profojak
   */
  private Boolean parseSaveFile(String saveName) {
    InputStream saveInputStream = Game.class.getResourceAsStream("/saves/" + saveName);
    BufferedReader saveReader = new BufferedReader(new InputStreamReader(saveInputStream));

    try {
      while (saveReader.ready()) {
        String[] line = saveReader.readLine().split(" ");

        switch (line[0]) {
          // Room id where the dungeon starts and ends
          case "start":
            roomStartId = Integer.parseInt(line[1]);
            System.out.println("roomStartId = " + roomStartId); // DEBUG
            roomEndId = Integer.parseInt(line[2]);
            System.out.println("roomEndId = " + roomEndId); // DEBUG
            break;
          // Player variables
          case "player":
            player.setHp(Integer.parseInt(line[2]));
            System.out.println("player = " + player.getHp()); // DEBUG
            player.takeLoot(line[1], Integer.parseInt(line[3]));
            System.out.println("player.getDamage = " + player.getDamage()); // DEBUG
            System.out.println("player.getSprite = " + player.getSprite()); // DEBUG
            break;
          // Current room
          case "id":
            roomCurrentId = Integer.parseInt(line[1]);
            System.out.println("roomCurrentId = " + roomCurrentId); // DEBUG
            rooms[roomCurrentId] = new Room();
            System.out.println("isVisited = " + rooms[roomCurrentId].isVisited()); // DEBUG
            break;
          // Story of current room
          case "story":
            rooms[roomCurrentId].setStoryBefore(line[1].replaceAll("_", " "));
            System.out.println("storyBefore = " + rooms[roomCurrentId].getStoryBefore()); // DEBUG
            rooms[roomCurrentId].setStoryAfter(line[2].replaceAll("_", " "));
            System.out.println("storyAfter = " + rooms[roomCurrentId].getStoryAfter()); // DEBUG
            break;
          // Monster in current room
          case "monster":
            rooms[roomCurrentId].setMonster(line[1],
              Integer.parseInt(line[2]), Integer.parseInt(line[3]));
            System.out.println("room.getMonsterSprite = " +
              rooms[roomCurrentId].getMonsterSprite()); // DEBUG
            break;
          // Loot in current room
          case "loot":
            rooms[roomCurrentId].setLoot(line[1], Integer.parseInt(line[2]));
            System.out.println("room.getLootSprite = "
              + rooms[roomCurrentId].getLootSprite()); // DEBUG
            break;
          // Texture of current room
          case "wall":
            rooms[roomCurrentId].setSprite(line[1]);
            System.out.println("room.sprite = " + rooms[roomCurrentId].getSprite()); // DEBUG
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

  /** To be implemented. */
  private void redraw() {
  }
}

