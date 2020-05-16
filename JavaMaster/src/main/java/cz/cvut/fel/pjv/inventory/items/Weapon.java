package cz.cvut.fel.pjv.inventory.items;

public class Weapon extends Item {
  private Integer damage;

  public Weapon(String sprite, String name, Integer damage) {
    this.sprite = sprite;
    this.name = name;
    this.damage = damage;
  }

  /**
   * Gets damage of weapon.
   *
   * @return damage of weapon
   */
  public Integer getWeaponDamage() {
    return damage;
  }
}
