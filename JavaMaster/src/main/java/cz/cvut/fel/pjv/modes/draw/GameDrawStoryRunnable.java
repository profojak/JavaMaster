package cz.cvut.fel.pjv.modes.draw;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Implementation of DrawGameStoryRunnable: thread that prints letters of story dialogs.
 */
public class GameDrawStoryRunnable implements Runnable {
  private final String COLOR_TEXT = "#FBF1C7";

  private String story;
  private GraphicsContext gc;
  private Integer length, rows, even_offset, row_offset;

  /**
   * @param gc - GraphicsContext to draw images to
   * @param story - story to print on screen
   */
  public GameDrawStoryRunnable(GraphicsContext gc, String story) {
    this.gc = gc;
    this.story = story;

    length = story.length();
    rows = (length % 14) == 0 ? length / 14 : length / 14 + 1;
    even_offset = ((rows & 1) == 0) ? 22 : 0;
    row_offset = ((9 - rows) / 2) * 45;
  }

  @Override
  public void run() {
    try {
      Boolean print_letters = true;
      Integer i = 0, j = 0;
        
      // Printing letters
      this.gc.setFill(Color.web(COLOR_TEXT));
      while (print_letters && i < 9) {
        while (print_letters && j < 14) {
          Thread.sleep(30);

          this.gc.strokeText("" + story.charAt(i * 14 + j), 408 + j * 35,
            98 + i * 45 + even_offset + row_offset);
          this.gc.fillText("" + story.charAt(i * 14 + j), 408 + j * 35,
            98 + i * 45 + even_offset + row_offset);
          j++;

          if (i * 14 + j == length) {
            print_letters = false;
          }
        }
        j = 0;
        i++;
      }
    } catch (Exception e) {
      Thread.currentThread().stop();
      return;
    }
  }
}

