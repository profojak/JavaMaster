package cz.cvut.fel.pjv.entities;

abstract class Entity {
  // Damage

  /**
   * Gets damage entity deals to other entity.
   *
   * @return monster damage
   */
  public Integer getDamage() {
    return 0;
  }

  /**
   * Takes damage from other entity.
   *
   * @param damage - damage dealt by player
   */
  public void takeDamage() {
  }

  // HP

  /**
   * Gets current HP of entity.
   *
   * @return current HP of monster
   */
  public Integer getHP() {
    return 0;
  }
}

