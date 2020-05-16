package cz.cvut.fel.pjv.inventory.items;

public class Potion extends Item {
  private Integer heal, count;

  public Potion(String sprite, String name, Integer heal, Integer count) {
    this.sprite = sprite;
    this.name = name;
    this.heal = heal;
    this.count = count;
  }

  /**
   * Updates number of potions.
   *
   * @param addCount - how many potions to add
   */
  public void updatePotionCount(Integer addCount) {
    count += addCount;
  }

  /**
   * Gets how much HP potion heals.
   *
   * @return how much HP potion heals
   */
  public Integer getPotionHeal() {
    return heal;
  }

  /**
   * Gets potion count.
   *
   * @return potion count
   */
  public Integer getPotionCount() {
    return count;
  }
}
