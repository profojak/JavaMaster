package cz.cvut.fel.pjv.inventory;

public class Inventory {
  private Integer numPotions = 0, numBombs = 0;
  private Integer damage, activeItem = 2;
  private String sprite;
  private String[] items = { "bomb", "potion", "weapon" };

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
  public Boolean usePotion() {
    return true;
  }

  /**
   * Uses bomb.
   */
  public Boolean useBomb() {
    return true;
  }

  // Choosing item

  /**
   * Selects next item.
   */
  public void itemNext() {
    activeItem += 1;
    if (activeItem >= items.length) {
      activeItem = 0;
    }
  }

  /**
   * Selects previous item.
   */
  public void itemPrevious() {
    activeItem -= 1;
    if (activeItem < 0) {
      activeItem = items.length - 1;
    }
  }

  // GUI

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

  /**
   * Gets active item.
   *
   * @return active item, can be weapon, potion or bomb.
   */
  public String getActiveItem() {
    return items[activeItem];
  }
}

