package cz.cvut.fel.pjv.inventory;

/**
 * Implementation of Loot: item that drops from a Monster when killed.
 *
 * <p>Loot type is determined by its sprite String: it can be either "poiton" or "bomb". Every
 * other String variant is considered as a weapon.
 */
public class Loot {
  private final String sprite;
  private final Integer count;

  /**
   * @param sprite - loot type when "potion" or "bomb", weapon texture otherwise
   * @param count - loot count when type is "potion" or "bomb", weapon damage otherwise
   */
  public Loot(String sprite, Integer count) { 
    this.sprite = sprite;
    this.count = count;
  }

  // Getters

  /**
   * Gets loot count when type is "potion" or "bomb", gets weapon damage otherwise.
   *
   * @return either loot count or damage of weapon
   */
  public Integer getCount() {
    return count;
  }

  /**
   * Gets type of loot if sprite is "potion" or "bomb", gets weapon texture otherwise.
   *
   * @return either loot type or texture of weapon
   */
  public String getSprite() {
    return sprite;
  }
}

