package cz.cvut.fel.pjv.entities;

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

  // Actions

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
   */
  public void takeLoot(Item loot) {
    inventory.addLoot(loot);
  }
}

