package cz.cvut.fel.pjv;

import java.io.File;

/**
 * Class holding various constants.
 *
 * @author profojak, povolji2
 */
public class Const {
  // Player directions
  public static enum Direction {
    NORTH,
    EAST,
    SOUTH,
    WEST
  }

  // Types of items
  public static enum ItemType {
    BOMB,
    POTION,
    WEAPON
  }

  // Game and Editor states
  public static enum State {
    LOAD,
    VICTORY,
    DEATH,
    DEFAULT,
    INVENTORY,
    MONSTER,
    COMBAT,
    LOOT,
    STORY_BEFORE,
    STORY_AFTER,
    MENU,
    SET,
    ROOM,
    HP,
    DAMAGE
  }

  // Part of save files to load
  public static enum LoadPart {
    START,
    PLAYER,
    ID,
    STORY,
    MONSTER,
    LOOT,
    WALL,
    END
  }

  public static final String
    // Logger text colors
    LOG_WHITE = "\u001B[37m", LOG_YELLOW = "\u001B[33m",
    LOG_RED = "\u001B[31m", LOG_RESET = "\u001B[0m",

    // Menu options
    MENU_MAINMENU = "MainMenu", MENU_GAME = "Game", MENU_ABOUT = "About",
    MENU_EDITOR = "Editor", MENU_CANCEL = "Cancel", MENU_EXIT = "Exit",
    MENU_DESCEND = "Descend", MENU_NOT_YET = "Not yet", MENU_SAVE = "Save",

    // Menu buttons
    BUTTON = "/sprites/menu/button.png", BUTTON_ACTIVE = "/sprites/menu/active.png",

    // Draw colors
    COLOR_STROKE = "#282828", COLOR_FILL = "#FBF1C7", COLOR_BG = "#928374", COLOR_BAR = "504945",
    COLOR_INVENTORY = "#665C54", COLOR_BAR_PLAYER_BG = "#9D0006", COLOR_BAR_PLAYER_FG = "#CC241D",
    COLOR_BAR_MONSTER_BG = "#427B58", COLOR_BAR_MONSTER_FG = "#689D6A", FONT = "/silkscreen.ttf",
    COLOR_START = "#79740E", COLOR_END = "#9D0006",

    // Absolute paths
    GAME_DIR = "profojak", DUNG_EXTENSION = ".dung",
    PATH = new File("").getAbsolutePath(),
    SAVE_PATH = PATH.substring(0, PATH.lastIndexOf(GAME_DIR) + GAME_DIR.length() + 1) + "saves/";

  public static final Integer
    // Inventory constants
    NUMBER_OF_ITEMS = ItemType.values().length,
    NEXT_ITEM = 1, PREVIOUS_ITEM = NUMBER_OF_ITEMS - 1, USE_ITEM = -1,

    // Map constants
    NUMBER_OF_ROOMS = 35, NUMBER_OF_DIRECTIONS = Direction.values().length,
    MAP_WIDTH = 5, MAP_LENGTH = 7, MAP_OFFSET = 75,
    DONT_TURN = 0, TURN_RIGHT = 1, TURN_LEFT = NUMBER_OF_DIRECTIONS - 1,

    GO_NORTH = -MAP_WIDTH, GO_EAST = 1, GO_SOUTH = MAP_WIDTH, GO_WEST = -1,

    NORTHERN_BORDER = MAP_WIDTH - 1, EASTERN_BORDER = MAP_WIDTH - 1,
    SOUTHERN_BORDER = NUMBER_OF_ROOMS - MAP_WIDTH, WESTERN_BORDER = 0,

    // Menu constants
    TEXT_X_OFFSET = 141, TEXT_Y_OFFSET = 60, BUTTON_HEIGHT = 95,

    // Window
    WINDOW_WIDTH = 1000, WINDOW_HEIGHT = 525;
}

