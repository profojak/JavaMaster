package cz.cvut.fel.pjv.modes;


import cz.cvut.fel.pjv.Const;
import cz.cvut.fel.pjv.Root;
import javafx.scene.layout.StackPane;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    String testFileName = "test.dung";
    File testFile = new File(Const.SAVE_PATH + testFileName);
    Game game = new Game(testFile);

    @Test
    public void testHasRoomLeft() {
        Boolean expVal1 = true, expVal2 = false;
        assertEquals(expVal2, game.hasRoomLeft());
        game.keyRight();
        assertEquals(expVal2, game.hasRoomLeft());
        game.keyLeft();
        assertEquals(expVal2, game.hasRoomLeft());
        game.keyLeft();
        assertEquals(expVal2, game.hasRoomLeft());
    }

    @Test
    public void testHasRoomRight() {
        Boolean expVal1 = true, expVal2 = false;
        assertEquals(expVal1, game.hasRoomRight());
        game.keyRight();
        assertEquals(expVal2, game.hasRoomRight());
        game.keyLeft();
        assertEquals(expVal1, game.hasRoomRight());
        game.keyLeft();
        assertEquals(expVal2, game.hasRoomRight());
    }

    @Test
    public void testHasRoomFront() {
        Boolean expVal1 = true, expVal2 = false;
        assertEquals(expVal2, game.hasRoomFront());
        game.keyRight();
        assertEquals(expVal2, game.hasRoomRight());
        game.keyLeft();
        assertEquals(expVal1, game.hasRoomRight());
        game.keyLeft();
        assertEquals(expVal2, game.hasRoomRight());
    }

    @Test
    public void testGetLeftDirection() {
        String expVal1 = "WEST", expVal2 = "NORTH", expVal3 = "SOUTH";
        assertEquals(expVal1, game.getLeftDirection());
        game.keyRight();
        assertEquals(expVal2, game.getLeftDirection());
        game.keyLeft();
        assertEquals(expVal1, game.getLeftDirection());
        game.keyLeft();
        assertEquals(expVal3, game.getLeftDirection());
    }

    @Test
    public void testGetRightDirection() {
        String expVal1 = "EAST", expVal2 = "SOUTH", expVal3 = "NORTH";
        assertEquals(expVal1, game.getRightDirection());
        game.keyRight();
        assertEquals(expVal2, game.getRightDirection());
        game.keyLeft();
        assertEquals(expVal1, game.getRightDirection());
        game.keyLeft();
        assertEquals(expVal3, game.getRightDirection());
    }

    @Test
    public void testIsRoomVisited() {
        Boolean expVal1 = true, expVal2 = false;
        Integer visitedRoomIndex = 0, notVisitedRoomIndex = 1;
        assertEquals(expVal1, game.isRoomVisited(visitedRoomIndex));
        assertEquals(expVal2, game.isRoomVisited(notVisitedRoomIndex));
        game.keyRight();
        game.keyUp();
        assertEquals(expVal1, game.isRoomVisited(notVisitedRoomIndex));
    }
}