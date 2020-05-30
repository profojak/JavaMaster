package cz.cvut.fel.pjv.entities;

import cz.cvut.fel.pjv.entities.Entity;

/**
 * Class implementing Monster, hostile Entity present in dungeons.
 *
 * @see Entity
 * @author profojak
 */
public class Monster extends Entity {
  private String sprite;
  private Integer damage;

  /**
   * @param sprite - monster texture
   * @param hp - monster HP
   * @param damage - monster damage
   */
  public Monster(String sprite, Integer hp, Integer damage) {
    this.sprite = sprite;
    this.setHp(hp);
    this.damage = damage;
  }

  // Setters

  /**
   * Sets damage of monster.
   *
   * @param damage - damage to set
   */
  public void setDamage(Integer damage) {
    this.damage = damage;
  }

  // Getters

  /**
   * Gets monster damage.
   *
   * @return monster damage
   */
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

