package cz.cvut.fel.pjv.entities;

import cz.cvut.fel.pjv.entities.Entity;

/** @see Entity */
public class Monster extends Entity {
  private String sprite;
  private Integer hp, damage;

  /**
   * @param sprite - monster texture
   * @param hp - monster HP
   * @param damage - monster damage
   */
  public Monster(String sprite, Integer hp, Integer damage) {
    this.sprite = sprite;
    this.hp = hp;
    this.damage = damage;
  }

  // Getters

  public Integer getDamage() {
    return damage;
  }

  /**
   * Gets monster texture.
   *
   * @return monster texture
   */
  public String getSprite() {
    return sprite;
  }
}

