package cz.cvut.fel.pjv.entities;

import cz.cvut.fel.pjv.entities.Entity;
import cz.cvut.fel.pjv.inventory.Loot;
import cz.cvut.fel.pjv.inventory.Inventory;

/** @see Entity */
public class Player extends Entity {
  private Inventory inventory;

  public Player() {
    inventory = new Inventory();
  }

  // Getters

  public Integer getDamage() {
    return inventory.getDamage();
  }

  public String getSprite() {
    return inventory.getSprite();
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
  public void takeLoot(Loot loot) {
    inventory.addLoot(loot);
  }
}

