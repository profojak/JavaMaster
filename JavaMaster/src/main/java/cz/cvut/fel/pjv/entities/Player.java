package cz.cvut.fel.pjv.entities;

import cz.cvut.fel.pjv.Const;
import cz.cvut.fel.pjv.entities.Entity;
import cz.cvut.fel.pjv.inventory.Inventory;
import cz.cvut.fel.pjv.inventory.items.Item;

/**
 * Implementation of Player: Entity controlled by user.
 *
 * @see Entity
 */
public class Player extends Entity {
  private Inventory inventory;

  public Player() {
    inventory = new Inventory();
  }

  // Getters

  /**
   * Gets damage of weapon.
   *
   * @return damage of weapon
   */
  public Integer getDamage() {
    return inventory.getWeaponDamage();
  }

  /**
   * Gets texture of weapon.
   *
   * @return texture of weapon
   */
  public String getSprite() {
    return inventory.getWeaponSprite();
  }

  /**
   * Gets bomb count.
   *
   * @return bomb count
   */
  public Integer getBombCount() {
    return inventory.getBombCount();
  }

  /**
   * Gets bomb damage.
   *
   * @return bomb damage
   */
  public Integer getBombDamage() {
    return inventory.getBombDamage();
  }

  /**
   * Gets potion count.
   *
   * @return potion count
   */
  public Integer getPotionCount() {
    return inventory.getPotionCount();
  }

  /**
   * Gets heal value of potion.
   *
   * @return heal value of potion
   */
  public Integer getPotionHeal() {
    return inventory.getPotionHeal();
  }

  /**
   * Gets active item.
   *
   * @return active item, can be weapon, potion or bomb
   */
  public Const.ItemType getActiveItem() {
    return inventory.getActiveItem();
  }

  // Actions

  /** Selects next item. */
  public void itemNext() {
    inventory.itemNext();
  }

  /** Selects previous item. */
  public void itemPrevious() {
    inventory.itemPrevious();
  }

  public Boolean usePotion() {
    if(inventory.usePotion()) {
      heal(inventory.getPotionHeal());
      return true;
    }
    return false;
  }

  public Integer useBomb() {
    if(inventory.useBomb()) {
      return inventory.getBombDamage();
    } else {
      return 0;
    }
  }

  /**
   * Heals player.
   *
   * @param hp - give player this much HP
   */
  public void heal(Integer hp) {
    this.hp += hp;
    if (hpMax < this.hp) {
      this.hp = hpMax;
    }
  }

  /**
   * Adds loot to player inventory.
   *
   * @param loot - loot instance
   *
   */
  public void takeLoot(Item loot) {
    inventory.addLoot(loot);
  }
}

