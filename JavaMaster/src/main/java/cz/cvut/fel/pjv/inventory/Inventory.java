package cz.cvut.fel.pjv.inventory;

public class Inventory {
  private Integer numPotions = 0, numBombs = 0;
  private Integer damage;
  private String sprite;

  // Getters

  /**
   * Gets number of potions.
   *
   * @return number of potions
   */
  public Integer getPotionCount() {
    return 0;
  }

  /**
   * Gets number of bombs.
   *
   * @return number of bombs
   */
  public Integer getBombCount() {
    return 0;
  }

  /**
   * Gets weapon damage.
   *
   * @return damage of weapon.
   */
  public Integer getDamage() {
    return damage;
  }

  /**
   * Gets weapon texture.
   *
   * @return texture of weapon.
   */
  public String getSprite() {
    return sprite;
  }

  // Pick ups

  /**
   * Adds loot to inventory.
   *
   * <p>Determines type of loot and updates inventory.
   *
   * @param sprite - loot type/texture
   * @param count - loot count/damage
   */
  public void addLoot(String sprite, Integer count) {
    switch (sprite) {
      // Potion
      case "potion":
        addPotion(count);
        break;
      // Bomb
      case "bomb":
        addPotion(count);
        break;
      // Weapon
      default:
        setWeapon(sprite, count);
    }
  }

  /**
   * Adds potion to inventory.
   *
   * @param count - how many potions to add
   */
  public void addPotion(Integer count) {
    numPotions += count;
  }

  /**
   * Adds bomb to inventory.
   *
   * @param count - how many bombs to add
   */
  public void addBomb(Integer count) {
    numBombs += count;
  }

  /**
   * Adds new weapon to inventory.
   *
   * @param sprite - weapon texture
   * @param damage - weapon damage
   */
  public void setWeapon(String sprite, Integer damage) {
    this.sprite = sprite;
    this.damage = damage;
  }

  /**
   * Uses potion.
   */
  public void usePotion() {
  }

  /**
   * Uses bomb.
   */
  public void useBomb() {
  }
}

