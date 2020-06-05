package cz.cvut.fel.pjv.modes;


import cz.cvut.fel.pjv.Const;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    final String testFileName = "empty.dung";
    final File testFile = new File(Const.SAVE_PATH + testFileName);
    Game game;

    @BeforeEach
    void before() {
        game = new Game(testFile);
    }

    @AfterEach
    void after() {
        game = null;
    }

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
        game.keyLeft();
        assertEquals(expVal1, game.hasRoomLeft());
        game.keyLeft();
        game.keyUp();
        game.keyUp();
        game.keyUp();
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

    @Test
    public void testParseSaveFile() {
        Boolean expVal = true;
        assertEquals(expVal, game.parseSaveFile(testFile));
        final String nonExistentFileName = "džava.dung";
        final String corruptedFileName = "list_of_reasons_why_windows_is_great.dung";
        final File nonExistentFile = new File(Const.SAVE_PATH + nonExistentFileName);
        final File corruptedFile = new File(Const.SAVE_PATH + corruptedFileName);
        assertThrows(IllegalArgumentException.class, () -> {
            game.parseSaveFile(nonExistentFile);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            game.parseSaveFile(corruptedFile);
        });
    }
}