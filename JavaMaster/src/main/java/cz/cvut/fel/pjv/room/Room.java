package cz.cvut.fel.pjv.room;

import cz.cvut.fel.pjv.entities.Monster;
import cz.cvut.fel.pjv.inventory.Loot;

/**
 * Implementation of Room: class that holds Monster and Loot objects.
 *
 * <p>Multiple instances of this class form a dungeon. Mode object changes current room and
 * calls room methods to interact with room objects and variables.
 */
public class Room {
  private Boolean isVisited = false;
  private String storyBefore = "", storyAfter = "";
  private String sprite;
  private Monster monster;
  private Loot loot;

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
   * Gets monster texture.
   *
   * @return texture of monster
   */
  public String getMonsterSprite() {
    return monster.getSprite();
  }

  /**
   * Gets loot.
   *
   * @return loot
   */
  public Loot getLoot() {
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

  /**
   * Sets loot.
   *
   * @param sprite - loot type/texture
   * @param count - loot count/damage
   */
  public void setLoot(String sprite, Integer count) {
    loot = new Loot(sprite, count);
  }
}

