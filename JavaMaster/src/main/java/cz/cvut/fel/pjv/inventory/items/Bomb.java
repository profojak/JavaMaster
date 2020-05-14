package cz.cvut.fel.pjv.inventory.items;

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
    public void updateBombCount(Integer addCount) { count += addCount; }

    public Integer getBombDamage() { return damage; }

    public Integer getBombCount() { return count; }
}
