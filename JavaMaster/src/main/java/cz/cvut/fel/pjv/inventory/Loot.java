package cz.cvut.fel.pjv.inventory;

public class Loot {
  private String sprite;
  private Integer count;

  /**
   * Constructor.
   *
   * @param sprite - loot type/texture
   * @param count - loot count/damage
   */
  public Loot(String sprite, Integer count) {
    this.sprite = sprite;
    this.count = count;
  }

  // Getters

  /**
   * Gets damage of weapon.
   *
   * @return damage of weapon
   */
  public Integer getCount() {
    return count;
  }

  /** To be implemented. */
  public String getSprite() {
    return sprite;
  }
}

