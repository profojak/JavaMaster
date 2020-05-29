package cz.cvut.fel.pjv.entities;

import cz.cvut.fel.pjv.Const;
import cz.cvut.fel.pjv.inventory.items.Bomb;
import cz.cvut.fel.pjv.inventory.items.Potion;
import cz.cvut.fel.pjv.inventory.items.Weapon;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    Player player = new Player();
    String test = "test";
    Integer damage = 50, count = 1;
    Bomb bomb = new Bomb(test, test, damage, count);
    Potion potion = new Potion(test, test, damage, count);
    Weapon weapon = new Weapon(test, test, damage);

    @Test
    void testGetDamage() {
        Integer expVal = 0;
        assertEquals(expVal, player.getDamage());
        player.takeLoot(weapon);
        assertEquals(damage, player.getDamage());
    }

    @Test
    void testGetSprite() {
        String expVal = null;
        assertEquals(expVal, player.getSprite());
        player.takeLoot(weapon);
        assertEquals(test, player.getSprite());
    }

    @Test
    void testGetBombCount() {
        Integer expVal = 0;
        assertEquals(expVal, player.getBombCount());
        player.takeLoot(bomb);
        assertEquals(count, player.getBombCount());
        player.useBomb();
        assertEquals(count - 1, player.getBombCount());
        player.useBomb();
        assertEquals(count - 1, player.getBombCount());
    }

    @Test
    void testGetBombDamage() {
        Integer expVal = 0;
        assertEquals(expVal, player.getBombDamage());
        player.takeLoot(bomb);
        assertEquals(damage, player.getBombDamage());
        player.useBomb();
        assertEquals(damage, player.getBombDamage());
        player.useBomb();
        assertEquals(damage, player.getBombDamage());
    }

    @Test
    void testGetPotionCount() {
        Integer expVal = 0;
        assertEquals(expVal, player.getPotionCount());
        player.takeLoot(potion);
        assertEquals(count, player.getPotionCount());
        player.usePotion();
        assertEquals(count - 1, player.getPotionCount());
        player.usePotion();
        assertEquals(count - 1, player.getPotionCount());
    }

    @Test
    void getPotionHeal() {
        Integer expVal = 0;
        assertEquals(expVal, player.getPotionHeal());
        player.takeLoot(potion);
        assertEquals(damage, player.getPotionHeal());
        player.usePotion();
        assertEquals(damage, player.getPotionHeal());
        player.usePotion();
        assertEquals(damage, player.getPotionHeal());
    }

    @Test
    void usePotion() {
    }

    @Test
    void useBomb() {
    }

    @Test
    void testHeal() {
        Integer expVal1 = 100, expVal2 = 0, damage = 60, heal = 120;
        player.setHp(expVal1);
        assertEquals(expVal1, player.getMaxHP());
        assertEquals(expVal1, player.getHP());
        player.takeDamage(damage);
        assertEquals(expVal1, player.getMaxHP());
        assertEquals(expVal1 - damage, player.getHP());
        player.takeDamage(damage);
        assertEquals(expVal1, player.getMaxHP());
        assertEquals(expVal2, player.getHP());
        player.heal(heal);
        assertEquals(expVal1, player.getMaxHP());
        assertEquals(expVal1, player.getHP());
    }

    @Test
    void testTakeLoot() {
        assertEquals(Const.ItemType.WEAPON, player.getActiveItem());
        player.itemNext();
        assertEquals(Const.ItemType.BOMB, player.getActiveItem());
        player.itemPrevious();
        player.takeLoot(bomb);
        player.takeLoot(potion);
        player.takeLoot(weapon);
        assertEquals(Const.ItemType.WEAPON, player.getActiveItem());
        player.itemNext();
        assertEquals(Const.ItemType.BOMB, player.getActiveItem());
        player.itemNext();
        assertEquals(Const.ItemType.POTION, player.getActiveItem());
        player.itemPrevious();
        assertEquals(Const.ItemType.BOMB, player.getActiveItem());
    }
}