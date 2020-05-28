package cz.cvut.fel.pjv.room;

import cz.cvut.fel.pjv.entities.Monster;
import cz.cvut.fel.pjv.inventory.items.Bomb;
import cz.cvut.fel.pjv.inventory.items.Item;
import cz.cvut.fel.pjv.inventory.items.Potion;
import cz.cvut.fel.pjv.inventory.items.Weapon;

/**
 * Implementation of Room: class that holds Monster and Loot objects.
 *
 * <p>Multiple instances of this class form a dungeon. Mode object changes current room and
 * calls room methods to interact with room objects and variables.
 */
public class Room {
  private final String PNG_EXTENSION = ".png";
  private final Integer HEALING_DIVIDER = 2, BOMBING_DIVIDER = 2;
  private Boolean isVisited = false;
  private String storyBefore = "", storyAfter = "";
  private String sprite = null;
  private Monster monster = null;
  private Item loot = null;

  // Boolean methods

  /**
   * Is there any monster?
   *
   * @return whether Room has Monster object assigned
   */
  public Boolean hasMonster() {
    return monster != null;
  }

  /**
   * Does this Room have non default sprite?
   *
   * @return whether Room has default sprite
   */
  public Boolean hasSprite() {
    return sprite != null;
  }

  /**
   * Is there any loot?
   *
   * @return whether Room has Loot object assigned
   */
  public Boolean hasLoot() {
    return loot != null;
  }

  /**
   * Is there any story dialog before monster encounter?
   *
   * @return whether Room has StoryBefore variable set
   */
  public Boolean hasStoryBefore() {
    return storyBefore != null && !storyBefore.isBlank();
  }

  /**
   * Is there any story dialog after monster encounter?
   *
   * @return whether Room has StoryAfter variable set
   */
  public Boolean hasStoryAfter() {
    return storyAfter != null && !storyAfter.isBlank();
  }

  /**
   * Is room already visited?
   *
   * @return whether Room is already visited
   */
  public Boolean isVisited() {
    return isVisited;
  }

  // Getters

  /**
   * Gets story dialog before monster encounter.
   *
   * @return StoryBefore variable assigned to this room
   */
  public String getStoryBefore() {
    return storyBefore;
  }

  /**
   * Gets story dialog after monster encounter.
   *
   * @return StoryAfter variable assigned to this room
   */
  public String getStoryAfter() {
    return storyAfter;
  }

  /**
   * Gets wall texture.
   *
   * @return texture of room wall
   */
  public String getSprite() {
    return sprite;
  }

  /**
   * Gets monster.
   *
   * @return instance of monster
   */
  public Monster getMonster() {
    return monster;
  }

  /**
   * Gets loot.
   *
   * @return loot
   */
  public Item getLoot() {
    return loot;
  }

  // Setters

  /**
   * Sets room as visited.
   */
  public void setVisited() {
    isVisited = true;
  }

  /**
   * Sets story dialog before monster encounter.
   *
   * @param dialog - string to save as story dialog
   */
  public void setStoryBefore(String dialog) {
    storyBefore = dialog;
  }

  /**
   * Sets story dialog after monster encounter.
   *
   * @param dialog - string to save as story dialog
   */
  public void setStoryAfter(String dialog) {
    storyAfter = dialog;
  }

  /**
   * Sets texture of room walls.
   *
   * @param sprite - texture of room walls
   */
  public void setSprite(String sprite) {
    this.sprite = sprite;
  }

  /**
   * Sets monster.
   *
   * @param sprite - monster texture
   * @param hp - monster HP
   * @param damage - monster damage
   */
  public void setMonster(String sprite, Integer hp, Integer damage) {
    monster = new Monster(sprite, hp, damage);
  }

  public void deleteMonster() {
    monster = null;
  }

  /**
   * Sets loot.
   *
   * @param name - loot name/type/texture
   * @param number - loot count/damage
   * @param playerHP - max HP of player
   */
  public void setLoot(String name, Integer number, Integer playerHP) {
    switch(name) {
      case "potion":
        loot = new Potion(name + PNG_EXTENSION, name, playerHP/HEALING_DIVIDER, number);
        break;
      case "bomb":
        loot = new Bomb(name + PNG_EXTENSION, name, playerHP/BOMBING_DIVIDER, number);
        break;
      default:
        loot = new Weapon(name, name.substring(0, name.lastIndexOf('.')), number);
    }
  }

  public void deleteLoot() {
    loot = null;
  }
}

