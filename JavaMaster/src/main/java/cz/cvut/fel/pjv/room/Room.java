package cz.cvut.fel.pjv.room;

import cz.cvut.fel.pjv.entities.Monster;
import cz.cvut.fel.pjv.inventory.Loot;

public class Room {
  // Boolean methods

  /**
   * Is there any monster?
   *
   * @return whether Room has Monster object assigned
   */
  public Boolean hasMonster() {
    return true;
  }

  /**
   * Is there any loot?
   *
   * @return whether Room has Loot object assigned
   */
  public void hasLoot() {
  }

  /**
   * Is there any story dialog before monster encounter?
   *
   * @return whether Room has StoryBefore variable set
   */
  public void hasStoryBefore() {
  }

  /**
   * Is there any story dialog after monster encounter?
   *
   * @return whether Room has StoryAfter variable set
   */
  public void hasStoryAfter() {
  }

  /**
   * Is room already visited?
   *
   * @return whether Room is already visited
   */
  public Boolean isVisited() {
    return true;
  }

  // Getters

  /**
   * Gets monster.
   *
   * @return Monster assigned to this room
   */
  public Monster getMonster() {
    return new Monster();
  }

  /**
   * Gets loot.
   *
   * @return Loot assigned to this room
   */
  public Loot getLoot() {
    return new Loot();
  }

  /**
   * Gets story dialog before monster encounter.
   *
   * @return StoryBefore variable assigned to this room
   */
  public String getStoryBefore() {
    return "";
  }

  /**
   * Gets story dialog after monster encounter.
   *
   * @return StoryAfter variable assigned to this room
   */
  public String getStoryAfter() {
    return "";
  }
}

