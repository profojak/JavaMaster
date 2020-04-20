package cz.cvut.fel.pjv.inventory;

import cz.cvut.fel.pjv.inventory.Loot;

public class Inventory {
  enum Item {
    BOMB, POTION, WEAPON
  }

  private Integer numPotions = 0, numBombs = 0;
  private Integer weaponDamage;
  private Item activeItem = Item.WEAPON;
  private String weaponSprite;

  // Pick ups

  /**
   * Adds loot to inventory.
   *
   * <p>Determines type of loot and updates inventory.
   *
   * @param loot - loot instance
   */
  public void addLoot(Loot loot) {
    String sprite = loot.getSprite();
    switch (sprite) {
      // Potion
      case "potion":
        addPotion(loot.getCount());
        break;
      // Bomb
      case "bomb":
        addPotion(loot.getCount());
        break;
      // Weapon
      default:
        setWeapon(sprite, loot.getCount());
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
    this.weaponSprite = sprite;
    this.weaponDamage = damage;
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
    switch (activeItem) {
      case WEAPON:
        activeItem = Item.BOMB;
        break;
      case BOMB:
        activeItem = Item.POTION;
        break;
      case POTION:
        activeItem = Item.WEAPON;
        break;
    }
  }

  /**
   * Selects previous item.
   */
  public void itemPrevious() {
    switch (activeItem) {
      case WEAPON:
        activeItem = Item.POTION;
        break;
      case POTION:
        activeItem = Item.BOMB;
        break;
      case BOMB:
        activeItem = Item.WEAPON;
        break;
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
    return this.weaponDamage;
  }

  /**
   * Gets weapon texture.
   *
   * @return texture of weapon.
   */
  public String getSprite() {
    return this.weaponSprite;
  }

  /**
   * Gets active item.
   *
   * @return active item, can be weapon, potion or bomb.
   */
  public Item getActiveItem() {
    return activeItem;
  }
}

