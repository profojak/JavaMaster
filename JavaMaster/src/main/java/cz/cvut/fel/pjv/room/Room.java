package cz.cvut.fel.pjv.room;

import cz.cvut.fel.pjv.room.Monster;
import cz.cvut.fel.pjv.room.Loot;

public class Room {
  // Boolean methods

  /**
   * Is there any monster?
   *
   * @return whether Room has Monster object assigned
   */
  public Boolean hasMonster() {
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
  public void isVisited() {
  }

  // Getters

  /**
   * Gets monster.
   *
   * @return Monster assigned to this room
   */
  public Monster getMonster() {
  }

  /**
   * Gets loot.
   *
   * @return Loot assigned to this room
   */
  public Loot getLoot() {
  }

  /**
   * Gets story dialog before monster encounter.
   *
   * @return StoryBefore variable assigned to this room
   */
  public String getStoryBefore() {
  }

  /**
   * Gets story dialog after monster encounter.
   *
   * @return StoryAfter variable assigned to this room
   */
  public String getStoryAfter() {
  }
}

