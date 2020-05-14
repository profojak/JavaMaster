package cz.cvut.fel.pjv.inventory;

import cz.cvut.fel.pjv.inventory.items.Bomb;
import cz.cvut.fel.pjv.inventory.items.Item;
import cz.cvut.fel.pjv.inventory.items.Potion;
import cz.cvut.fel.pjv.inventory.items.Weapon;
import cz.cvut.fel.pjv.modes.Game;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Inventory class holds number of potions and bombs and a weapon.
 */
public class Inventory {
  /*enum Item {
    BOMB,
    POTION,
    WEAPON
  }*/

  private final Integer NUMBER_OF_ITEMS = Item.values().length, NEXT_ITEM = 1,
    PREVIOUS_ITEM = NUMBER_OF_ITEMS - 1;
  private final String RED = "\u001B[31m", RESET = "\u001B[0m";
  private static final Logger logger = Logger.getLogger(Inventory.class.getName());

  private Integer weaponDamage;
  private Item activeItem = Item.WEAPON;
  private String weaponSprite;
  private Weapon weapon = null;
  private Potion potion = null;
  private Bomb bomb = null;

  // Pick ups

  /**
   * Adds loot to inventory.
   *
   * <p>Determines type of loot and updates inventory.
   *
   * @param loot - loot instance
   */
  public void addLoot(Item loot) {
    switch (loot.getClass()) {
      // Potion
      case Potion.class:
        if (potion.equals(null)) {
          potion = (Potion) loot;
        }
        potion.updatePotionCount(((Potion) loot).getPotionCount());
        break;
      // Bomb
      case Bomb.class:
        if (bomb.equals(null)) {
          bomb = (Bomb) loot;
        }
        bomb.updateBombCount(((Bomb) loot).getBombCount());
        break;
      // Weapon
      // TODO Check if player wants to get new weapon
      default:
        
        setWeapon(sprite, loot.getCount());
    }
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
   *
   * @return whether potion was used
   */
  public Boolean usePotion() {
    return true;
  }

  /**
   * Uses bomb.
   *
   * @return whether bomb was used
   */
  public Boolean useBomb() {
    return true;
  }

  // Choosing item

  /**
   * Changes current item.
   *
   * @param itemChange - number to determine current item index
   */
  private Item changeItem(Integer itemChange) {
    Item newItem = null;
    try {
      Integer itemIndex = (activeItem.ordinal() + itemChange) % NUMBER_OF_ITEMS;
      newItem = Item.values()[itemIndex];
    } catch (Exception exception) {
      logger.log(Level.SEVERE, RED + ">>>  Error: Unexpected item value: " + newItem + RESET,
        exception); // ERROR
      return null;
    }

    return newItem;
  }

  /** Selects next item. */
  public void itemNext() {
    activeItem = changeItem(NEXT_ITEM);
  }

  /** Selects previous item. */
  public void itemPrevious() {
    activeItem = changeItem(PREVIOUS_ITEM);
  }

  // GUI

  /**
   * Gets number of potions.
   *
   * @return number of potions
   */
  public Integer getPotionCount() {
    if (potion.equals(null)) {
      return 0;
    }
    return potion.getPotionCount();
  }

  /**
   * Gets number of bombs.
   *
   * @return number of bombs
   */
  public Integer getBombCount() {
    if (bomb.equals(null)) {
      return 0;
    }
    return bomb.getBombCount();
  }

  /**
   * Gets weapon damage.
   *
   * @return damage of weapon.
   */
  public Integer getWeaponDamage() {
    return this.weaponDamage;
  }

  /**
   * Gets weapon texture.
   *
   * @return texture of weapon.
   */
  public String getWeaponSprite() {
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

