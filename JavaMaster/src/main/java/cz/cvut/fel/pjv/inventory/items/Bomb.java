package cz.cvut.fel.pjv.inventory.items;

/**
 * Class implementing Bomb Item in inventory.
 *
 * @author povolji2
 */
public class Bomb extends Item {
  private Integer damage, count;

  public Bomb(String sprite, String name, Integer damage, Integer count) {
    this.sprite = sprite;
    this.name = name;
    this.damage = damage;
    this.count = count;
  }

  /**
   * Updates number of bombs.
   *
   * @param addCount - how many bombs to add
   */
  public void updateBombCount(Integer addCount) {
    count += addCount;
  }

  /**
   * Gets damage dealt by bomb.
   *
   * @return damage dealt by bomb
   */
  public Integer getBombDamage() {
    return damage;
  }

  /**
   * Gets bomb count.
   *
   * @return bomb count
   */
  public Integer getBombCount() {
    return count;
  }
}
