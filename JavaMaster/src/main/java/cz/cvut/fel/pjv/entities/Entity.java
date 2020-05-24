package cz.cvut.fel.pjv.entities;

/**
 * Abstract class implementing basic HP and damage related methods of entities.
 */
public abstract class Entity {
  protected Integer hp, hpMax;

  // Setters

  /**
   * Takes damage from other entity.
   *
   * @param damage - damage dealt by player
   */
  public void takeDamage(Integer damage) {
    hp -= damage;
    if (hp < 0) {
      hp = 0;
    }
  }

  /**
   * Sets maximum HP of entity.
   *
   * <p>This method is used for initial setup of entity. It sets maximum HP and current HP.
   *
   * @param hp - set maximum HP of entity
   */
  public void setHp(Integer hp) {
    this.hpMax = hp;
    this.hp = hp;
  }

  // Getters

  /**
   * Gets current damage of entity.
   *
   * @return current damage of entity
   */
  abstract public Integer getDamage();

  /**
   * Gets current HP of entity.
   *
   * @return current HP of entity
   */
  public Integer getHP() {
    return hp;
  }

  /**
   * Gets max HP of entity.
   *
   * @return max HP of entity
   */
  public Integer getMaxHP() {
    return hpMax;
  }
}

