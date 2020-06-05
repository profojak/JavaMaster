package cz.cvut.fel.pjv.modes;

import cz.cvut.fel.pjv.Const;
import cz.cvut.fel.pjv.Root;
import cz.cvut.fel.pjv.inventory.items.*;
import cz.cvut.fel.pjv.modes.draw.*;
import cz.cvut.fel.pjv.entities.*;
import cz.cvut.fel.pjv.room.Room;
import cz.cvut.fel.pjv.menu.layouts.*;

import java.io.*;
import java.util.logging.Logger;
import java.util.logging.Level;

import javafx.scene.layout.StackPane;
import javafx.scene.image.Image;

/**
 * Class implementing Editor.
 *
 * <p>This mode is loaded when player wants to create or edit dungeon.
 *
 * @see Mode
 * @author profojak
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

  /**
   * @param stack - StackPane to draw images to
   * @param root - parent object
   */
  public Editor(StackPane stack, Root root) {
    this.root = root;
    this.player = new Player();
    this.draw = new EditorDraw(stack, this);
    this.draw.redraw(state);
  }

  /**
   * @deprecated use Editor(StackPane, Root) instead
   */
  @Deprecated
  public Editor() {
    this.root = null;
    this.player = null;
    this.draw = null;
  }

  /**
   * @see Mode
   */
  public void keyUp() {
    switch (state) {
      /* Select room */
      case DEFAULT:
        roomCurrentId += Const.GO_NORTH;
        if (roomCurrentId < 0) {
          roomCurrentId -= Const.GO_NORTH;
        }
        break;
      /* Edit room */
      case LOOT:
        if (getRoomId() != roomEndId) {
          roomStartId = getRoomId();        
          rooms[getRoomId()].deleteMonster();
          rooms[roomCurrentId].setStoryBefore("");
          rooms[roomCurrentId].setStoryAfter("");
        }
        break;
      /* Edit monster and loot */
      case MONSTER:
        Integer count;
        String loot = root.getInputDialog(Const.State.LOOT, "sword.png");
        rooms[getRoomId()].deleteLoot();
        try {
          if (!loot.equals("bomb") && !loot.equals("potion")) {
            Image image = new Image("/sprites/inventory/weapons/" + loot);
          }
        } catch (Exception e) {
          logger.log(Level.SEVERE, Const.LOG_RED + "Weapon sprite " + loot + " does not exist!"
            + Const.LOG_RESET); // DEBUG
          break;
        }

        String temp = root.getInputDialog(Const.State.INVENTORY, "1");
        try {
          count = Integer.parseInt(temp);
        } catch (Exception e) {
          logger.log(Level.SEVERE, Const.LOG_RED + temp + " is not a number!"
            + Const.LOG_RESET); // DEBUG
          count = 1;
        }

        rooms[getRoomId()].setLoot(loot, count, 1);
        break;
      /* Edit room texture and story */
      case ROOM:
        String texture = root.getInputDialog(state, getRoomSprite());
        try {
          if (texture != null || !texture.equals("default.png")) {
            Image checkImage = new Image("/sprites/room/front/" + texture);
          }
          rooms[roomCurrentId].setSprite(texture);
        } catch (Exception e) {
          logger.log(Level.SEVERE, Const.LOG_RED + "Room sprite " + texture + " does not exist!"
            + Const.LOG_RESET); // DEBUG
          break;
        }
        break;
      /* Menu */
      case MENU:
        this.menu.buttonPrevious();
        break;
    }
    this.draw.redraw(state);
  }

  /**
   * @see Mode
   */
  public void keyDown() {
    switch (state) {
      /* Select room */
      case DEFAULT:
        roomCurrentId += Const.GO_SOUTH;
        if (roomCurrentId >= Const.NUMBER_OF_ROOMS) {
          roomCurrentId -= Const.GO_SOUTH;
        }
        break;
      /* Edit room */
      case LOOT:
        if (getRoomId() != roomStartId) {
          roomEndId = getRoomId();
          rooms[getRoomId()].deleteMonster();
        }
        break;
      /* Menu */
      case MENU:
        this.menu.buttonNext();
        break;
    }
    this.draw.redraw(state);
  }

  /**
   * @see Mode
   */
  public void keyLeft() {
    switch (state) {
      /* Create or load dungeon */
      case LOAD:
        player.setHp(10);
        Weapon weapon = new Weapon("sword.png", "sword", 2);
        player.takeLoot(weapon);
        this.draw.redraw(Const.State.SET);
        state = Const.State.DEFAULT;
        break;
      /* Select room */
      case DEFAULT:
        roomCurrentId += Const.GO_WEST;
        if (roomCurrentId < 0) {
          roomCurrentId -= Const.GO_WEST;
        }
        break;
      /* Edit room */
      case LOOT:
        state = Const.State.ROOM;
        break;
      /* Edit monster and loot */
      case MONSTER:
        if (getMonsterSprite() == null) {
          return;
        }
        Integer damage;
        String temp = root.getInputDialog(Const.State.DAMAGE, String.valueOf(getMonsterDamage()));
        try {
          damage = Integer.parseInt(temp);
        } catch (Exception e) {
          logger.log(Level.SEVERE, Const.LOG_RED + temp + " is not a number!"
            + Const.LOG_RESET); // DEBUG
          damage = 1;
        }
        rooms[getRoomId()].getMonster().setDamage(damage);
        break;
      /* Edit room texture and story */
      case ROOM:
        if (getRoomId() != roomStartId) {
          String story = root.getInputDialog(Const.State.STORY_BEFORE, getStoryBefore());
          if (story != null && !story.equals("")) {
            rooms[roomCurrentId].setStoryBefore(story);
          } else {
            rooms[roomCurrentId].setStoryBefore("");
            rooms[roomCurrentId].setStoryAfter("");
          }
        } else {
          logger.log(Level.SEVERE, Const.LOG_RED + "Cannot assign story to start room!"
            + Const.LOG_RESET); // DEBUG
        }
        break;
      /* Menu */
      case MENU:
        this.menu.buttonPrevious();
        break;
    }
    this.draw.redraw(state);
  }

  /**
   * @see Mode
   */
  public void keyRight() {
    switch (state) {
      /* Create or load dungeon */
      case LOAD:
        this.saveFile = this.root.getFile();
        if (saveFile != null && saveFile.canRead()) {
          parseSaveFile(saveFile);
        } else {
          player.setHp(10);
          Weapon weapon = new Weapon("sword.png", "sword", 2);
          player.takeLoot(weapon);
        }
        this.draw.redraw(Const.State.SET);
        state = Const.State.DEFAULT;
        break;
      /* Select room */
      case DEFAULT:
        roomCurrentId += Const.GO_EAST;
        if (roomCurrentId >= Const.NUMBER_OF_ROOMS) {
          roomCurrentId -= Const.GO_EAST;
        }
        break;
      /* Edit monster and loot */
      case MONSTER:
        if (getMonsterSprite() == null) {
          return;
        }
        Integer HP;
        String temp = root.getInputDialog(Const.State.HP, String.valueOf(getMonsterMaxHP()));
        try {
          HP = Integer.parseInt(temp);
        } catch (Exception e) {
          logger.log(Level.SEVERE, Const.LOG_RED + temp + " is not a number!"
            + Const.LOG_RESET); // DEBUG
          HP = 1;
        }
        rooms[getRoomId()].getMonster().setHp(HP);
        break;
      /* Edit room */
      case LOOT:
        if (getRoomId() != roomStartId && getRoomId() != roomEndId) {
          state = Const.State.MONSTER;
        } else {
          logger.log(Level.SEVERE, Const.LOG_RED + "Cannot assign monster and loot to start or end room!"
            + Const.LOG_RESET); // DEBUG
        }
        break;
      /* Edit room texture and story */
      case ROOM:
        if (getRoomId() != roomStartId) {
          String story = root.getInputDialog(Const.State.STORY_AFTER, getStoryAfter());
          if (story != null && !story.equals("") &&
              getStoryBefore() != null && !getStoryBefore().equals("")) {
            rooms[roomCurrentId].setStoryAfter(story);
          } else {
            rooms[roomCurrentId].setStoryAfter("");
          }
        } else {
          logger.log(Level.SEVERE, Const.LOG_RED + "Cannot assign story to start room!"
            + Const.LOG_RESET); // DEBUG
        }
        break;
      /* Menu */
      case MENU:
        this.menu.buttonNext();
        break;
    }
    this.draw.redraw(state);
  }

  /**
   * @see Mode
   */
  public void keyEnter() {
    switch (state) {
      /* Select room */
      case DEFAULT:
        if (!hasRoom(roomCurrentId)) {
          rooms[roomCurrentId] = new Room();
        }
        state = Const.State.LOOT;
        break;
      /* Edit monster and loot */
      case MONSTER:
        Integer damage, HP;
        String temp = root.getInputDialog(Const.State.MONSTER, getMonsterSprite());
        try {
          Image checkImage = new Image("/sprites/monster/" + temp);
        } catch (Exception e) {
          logger.log(Level.SEVERE, Const.LOG_RED + "Monster sprite " + temp + " does not exist!"
            + Const.LOG_RESET); // DEBUG
          break;
        }
        rooms[getRoomId()].setMonster(temp, 0, 0);

        temp = root.getInputDialog(Const.State.DAMAGE, "3");
        try {
          damage = Integer.parseInt(temp);
        } catch (Exception e) {
          logger.log(Level.SEVERE, Const.LOG_RED + temp + " is not a number!"
            + Const.LOG_RESET); // DEBUG
          damage = 1;
        }
        rooms[getRoomId()].getMonster().setDamage(damage);

        temp = root.getInputDialog(Const.State.HP, "10");
        try {
          HP = Integer.parseInt(temp);
        } catch (Exception e) {
          logger.log(Level.SEVERE, Const.LOG_RED + temp + " is not a number!"
            + Const.LOG_RESET); // DEBUG
          HP = 1;
        }
        rooms[getRoomId()].getMonster().setHp(HP);
        break;
      /* Edit room */
      case LOOT:
        Integer dmg;
        String textur = root.getInputDialog(Const.State.VICTORY, "sword.png");
        try {
          Image image = new Image("/sprites/inventory/weapons/" + textur);
        } catch (Exception e) {
          logger.log(Level.SEVERE, Const.LOG_RED + "Weapon sprite " + textur + " does not exist!"
            + Const.LOG_RESET); // DEBUG
          break;
        }

        String tmp = root.getInputDialog(Const.State.INVENTORY, "1");
        try {
          dmg = Integer.parseInt(tmp);
        } catch (Exception e) {
          logger.log(Level.SEVERE, Const.LOG_RED + tmp + " is not a number!"
            + Const.LOG_RESET); // DEBUG
          dmg = 1;
        }

        Weapon weapon = new Weapon(textur, textur.substring(0, textur.lastIndexOf('.')), dmg);
        player.takeLoot(weapon);
        break;
      /* Menu */
      case MENU:
        // Cancel
        if (this.menu.getAction(this.menu.getActive()).equals(Const.MENU_CANCEL)) {
          this.menu = null;
          state = Const.State.DEFAULT;
        // Save
        } else if (this.menu.getAction(this.menu.getActive()).equals(Const.MENU_SAVE)) {
          String save = root.getInputDialog(Const.State.LOAD, "my_best_dung");
          if (save != null && !save.equals("")) {
            createSaveFile(save);
          }
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

  /**
   * @see Mode
   */
  public void keyEscape() {
    switch (state) {
      /* Select room */
      case DEFAULT:
        this.menu = new Save();
        state = Const.State.MENU;
        break;
      /* Edit room */
      case LOOT:
        state = Const.State.DEFAULT;
        break;
      /* Edit monster and loot */
      case MONSTER:
        state = Const.State.LOOT;
        break;
      /* Edit room texture and story */
      case ROOM:
        state = Const.State.LOOT;
        break;
      /* Menu */
      case MENU:
        this.menu = null;
        state = Const.State.DEFAULT;
        break;
    }
    this.draw.redraw(state);
  }

  /**
   * @see Mode
   */
  public void keyDelete() {
    switch (state) {
      /* Select room */
      case DEFAULT:
        rooms[roomCurrentId] = null;
        break;
      /* Edit monster and loot */
      case MONSTER:
        rooms[roomCurrentId].deleteMonster();
        break;
      /* Edit room */
      case LOOT:
        Integer Hp;
        String string = root.getInputDialog(Const.State.HP, "10");
        try {
          Hp = Integer.parseInt(string);
        } catch (Exception e) {
          logger.log(Level.SEVERE, Const.LOG_RED + string + " is not a number!"
            + Const.LOG_RESET); // DEBUG
          Hp = 10;
        }
        player.setHp(Hp);
        break;
    }
    this.draw.redraw(state);
  }

  /**
   * Returns whether there is a room at the specified index.
   *
   * @param index - index of room to check
   * @return whether there is a room at the specified index
   */
  public Boolean hasRoom(Integer index) {
    return rooms[index] != null;
  }

  /**
   * Returns whether a room specified by index is the starting one.
   *
   * @param index - index of room to check
   * @return whether a room is the starting one
   */
  public Boolean isStartRoom(Integer index) {
    return index == roomStartId;
  }

  /**
   * Returns whether a room specified by index is the ending one.
   *
   * @param index - index of room to check
   * @return whether a room is the ending one
   */
  public Boolean isEndRoom(Integer index) {
    return index == roomEndId;
  }

  // Getters

  /**
   * Gets monster max HP.
   *
   * @return monster max HP
   */
  public Integer getMonsterMaxHP() {
    if (rooms[getRoomId()].getMonster() != null) {
      return rooms[getRoomId()].getMonster().getMaxHP();
    }
    return 0;
  }

  /**
   * Gets monster damage.
   *
   * @return monster damage
   */
  public Integer getMonsterDamage() {
    if (rooms[getRoomId()].getMonster() != null) {
      return rooms[getRoomId()].getMonster().getDamage();
    }
    return 0;
  }

  /**
   * Gets monster sprite.
   *
   * @return monster sprite
   */
  public String getMonsterSprite() {
    if (rooms[getRoomId()].getMonster() != null) {
      return rooms[getRoomId()].getMonster().getSprite();
    }
    return null;
  }

  /**
   * Gets player max HP.
   *
   * @return player max HP
   */
  public Integer getPlayerMaxHP() {
    return player.getMaxHP();
  }

  /**
   * Gets player's weapon damage.
   *
   * @return player's weapon damage
   */
  public Integer getWeaponDamage() {
    return player.getDamage();
  }

  /**
   * Gets player's weapon sprite.
   *
   * @return player's weapon sprite
   */
  public String getWeaponSprite() {
    return player.getSprite();
  }

  /**
   * Gets loot.
   *
   * @return loot
   */
  public Item getLoot() {
    return rooms[getRoomId()].getLoot();
  }

  /**
   * Gets loot type.
   *
   * @return loot type
   */
  public Const.ItemType getLootType() {
    if (getLoot() instanceof Weapon) {
      return Const.ItemType.WEAPON;
    } else if (getLoot() instanceof Bomb) {
      return Const.ItemType.BOMB;
    } else if (getLoot() instanceof Potion) {
      return Const.ItemType.POTION;
    }
    return null;
  }

  /**
   * Gets loot count.
   *
   * @return loot count
   */
  public Integer getLootCount() {
    if (getLoot() instanceof Weapon) {
      return ((Weapon)getLoot()).getWeaponDamage();
    } else if (getLoot() instanceof Bomb) {
      return ((Bomb)getLoot()).getBombCount();
    } else if (getLoot() instanceof Potion) {
      return ((Potion)getLoot()).getPotionCount();
    }
    return null;
  }

  /**
   * Gets current room index.
   *
   * @return current room index
   */
  public Integer getRoomId() {
    return roomCurrentId;
  }

  /**
   * Gets story before.
   *
   * @return story before
   */
  public String getStoryBefore() {
    return rooms[getRoomId()].getStoryBefore();
  }

  /**
   * Gets story after.
   *
   * @return story after
   */
  public String getStoryAfter() {
    return rooms[getRoomId()].getStoryAfter();
  }

  /**
   * Gets room texture.
   *
   * @return room texture
   */
  public String getRoomSprite() {
    return rooms[getRoomId()].getSprite();
  }

  /**
   * Gets room texture.
   *
   * @return room texture
   */
  public String getMenuAction(Integer index) {
    return this.menu.getAction(index);
  }

  /**
   * Gets index of active menu option.
   *
   * @return index of active menu option
   */
  public Integer getMenuActive() {
    return this.menu.getActive();
  }

  /**
   * Gets count of menu options.
   *
   * @return count of menu options
   */
  public Integer getMenuCount() {
    return this.menu.getCount();
  }

  // Save files

  /**
   * Parses save file and prepares the dungeon.
   *
   * <p>This method reads .dung text file and creates instances of described classes. After this
   * method is finished, Editor object is ready for editing.
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

  /**
   * Save dungeon to file.
   *
   * <p>This method processes the dungeon structure and saves it to .dung text file.
   *
   * @param newSaveFileName - .dung file to save to
   * @return whether saving was successful
   * @author povolji2
   */
  private Boolean createSaveFile(String newSaveFileName) {
    try {
      File newSaveFile = new File(Const.SAVE_PATH + newSaveFileName + Const.DUNG_EXTENSION);

      if (!newSaveFile.exists()) {
        newSaveFile.createNewFile();
      }

      BufferedWriter saveWriter = new BufferedWriter(new FileWriter(newSaveFile));

      // Room id where the dungeon starts and ends
      saveWriter.write(Const.LoadPart.START.toString().toLowerCase() + " " + roomStartId + " " + roomEndId);
      saveWriter.newLine();
      // Player variables
      saveWriter.write(Const.LoadPart.PLAYER.toString().toLowerCase() + " " + player.getSprite() + " " + player.getMaxHP() + " " + player.getDamage());
      saveWriter.newLine();
      // Dungeon rooms
      for (Integer i = 0; i < Const.NUMBER_OF_ROOMS; i++) {
        if (rooms[i] != null) {
          saveWriter.write(Const.LoadPart.ID.toString().toLowerCase() + " " + i);
          saveWriter.newLine();
          // Story of current room
          if (rooms[i].hasStoryBefore()) {
            String storyBefore = rooms[i].getStoryBefore().replaceAll(" ", "_");
            saveWriter.write(Const.LoadPart.STORY.toString().toLowerCase() + " " + storyBefore);
            if (rooms[i].hasStoryAfter()) {
              String storyAfter = rooms[i].getStoryAfter().replaceAll(" ", "_");
              saveWriter.write(" " + storyAfter);
            }
            saveWriter.newLine();
          }
          // Monster in current room
          if (rooms[i].hasMonster()) {
            Monster monster = rooms[i].getMonster();
            saveWriter.write(Const.LoadPart.MONSTER.toString().toLowerCase() + " " + monster.getSprite() + " " + monster.getMaxHP() + " " + monster.getDamage());
            saveWriter.newLine();
          }
          // Loot in current room
          if (rooms[i].hasLoot()) {
            Item loot = rooms[i].getLoot();
            // Potion
            if (loot instanceof Potion) {
              Potion potion = (Potion) loot;
              saveWriter.write(Const.LoadPart.LOOT.toString().toLowerCase() + " " + potion.getName() + " " + potion.getPotionCount());
            // Bomb
            } else if (loot instanceof Bomb) {
              Bomb bomb = (Bomb) loot;
              saveWriter.write(Const.LoadPart.LOOT.toString().toLowerCase() + " " + bomb.getName() + " " + bomb.getBombCount());
            // Weapon
            } else if (loot instanceof Weapon) {
              Weapon weapon = (Weapon) loot;
              saveWriter.write(Const.LoadPart.LOOT.toString().toLowerCase() + " " + weapon.getSprite() + " " + weapon.getWeaponDamage());
            }

            saveWriter.newLine();
          }
          // Texture of current room
          if (rooms[i].hasSprite()) {
            saveWriter.write(Const.LoadPart.WALL.toString().toLowerCase() + " " + rooms[i].getSprite());
            saveWriter.newLine();
          }
        }
      }
      // End of save file
      saveWriter.write(Const.LoadPart.END.toString().toLowerCase());
      // Name of next map
      String nextMap = this.root.getInputDialog(Const.State.MENU, "next_dung");
      if (nextMap != null && !nextMap.equals("")) {
        saveWriter.write(" " + nextMap + ".dung");
      }
      saveWriter.close();
    } catch (Exception exception) {
      logger.log(Level.SEVERE, Const.LOG_RED + "File could not be saved."
              + Const.LOG_RESET, exception); // ERROR
      return false; // File could not be loaded
    }
    return true;
  }

  /**
   * @see Mode
   */
  public void close() {
    this.draw.close();
  }
}

