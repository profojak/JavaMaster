package cz.cvut.fel.pjv.inventory.items;

public class Weapon extends Item {
    private Integer damage;

    public Weapon(String sprite, String name, Integer damage) {
        this.sprite = sprite;
        this.name = name;
        this.damage = damage;
    }

    public Integer getWeaponDamage() { return damage; }
}
