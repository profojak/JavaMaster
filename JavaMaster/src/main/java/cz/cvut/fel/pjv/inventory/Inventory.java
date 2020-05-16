package cz.cvut.fel.pjv.inventory;

import cz.cvut.fel.pjv.Const;
import cz.cvut.fel.pjv.inventory.items.Bomb;
import cz.cvut.fel.pjv.inventory.items.Item;
import cz.cvut.fel.pjv.inventory.items.Potion;
import cz.cvut.fel.pjv.inventory.items.Weapon;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Inventory class holding number of potions and bombs and a weapon.
 */
public class Inventory {
  private static final Logger logger = Logger.getLogger(Inventory.class.getName());

  private Weapon weapon = null;
  private Potion potion = null;
  private Bomb bomb = null;
  private Const.ItemType activeItem = Const.ItemType.WEAPON;

  // Pick ups

  /**
   * Adds loot to inventory.
   *
   * <p>Determines type of loot and updates inventory.
   *
   * @param loot - loot instance
   */
  public void addLoot(Item loot) {
    // Potion
    if (loot instanceof Potion) {
      if (potion.equals(null)) {
        potion = (Potion) loot;
      }
      potion.updatePotionCount(((Potion) loot).getPotionCount());
    // Bomb
    } else if (loot instanceof Bomb) {
      if (bomb.equals(null)) {
        bomb = (Bomb) loot;
      }
      bomb.updateBombCount(((Bomb) loot).getBombCount());
    // Weapon
    } else if (loot instanceof Weapon) {
      if (weapon.equals(null)) {
        weapon = (Weapon) loot;
      }
      // TODO Check if player wants to get new weapon
    }
  }

  /**
   * Uses potion.
   *
   * @return whether potion was used
   */
  public Boolean usePotion() {
    if (!potion.equals(null) && potion.getPotionCount() > 0) {
      potion.updatePotionCount(Const.USE_ITEM);
      return true;
    }
    return false;
  }

  /**
   * Uses bomb.
   *
   * @return whether bomb was used
   */
  public Boolean useBomb() {
    if (!bomb.equals(null) && bomb.getBombCount() > 0) {
      bomb.updateBombCount(Const.USE_ITEM);
      return true;
    }
    return false;
  }

  // Choosing item

  /**
   * Changes current item.
   *
   * @param itemChange - number to determine current item index
   */
  private Const.ItemType changeItem(Integer itemChange) {
    Const.ItemType newItem = null;
    try {
      Integer itemIndex = (activeItem.ordinal() + itemChange) % Const.NUMBER_OF_ITEMS;
      newItem = Const.ItemType.values()[itemIndex];
    } catch (Exception exception) {
      logger.log(Level.SEVERE, Const.LOG_RED + ">>>  Error: Unexpected item value: " + newItem
        + Const.LOG_RESET, exception); // ERROR
      return null;
    }

    return newItem;
  }

  /** Selects next item. */
  public void itemNext() {
    activeItem = changeItem(Const.NEXT_ITEM);
  }

  /** Selects previous item. */
  public void itemPrevious() {
    activeItem = changeItem(Const.PREVIOUS_ITEM);
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
    return weapon.getWeaponDamage();
  }

  /**
   * Gets weapon texture.
   *
   * @return texture of weapon.
   */
  public String getWeaponSprite() {
    return weapon.getSprite();
  }

  /**
   * Gets active item.
   *
   * @return active item, can be weapon, potion or bomb.
   */
  public Const.ItemType getActiveItem() {
    return activeItem;
  }
}

