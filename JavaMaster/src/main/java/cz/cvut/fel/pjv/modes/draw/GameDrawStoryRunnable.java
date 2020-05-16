package cz.cvut.fel.pjv.modes.draw;

import cz.cvut.fel.pjv.Const;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Implementation of DrawGameStoryRunnable: thread that prints letters of story dialogs.
 *
 * @author profojak
 */
public class GameDrawStoryRunnable implements Runnable {
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
    // How meny rows should the text occupy on screen
    rows = (length % 14) == 0 ? length / 14 : length / 14 + 1;
    // If rows is even number, set variable to center the text on screen
    even_offset = ((rows & 1) == 0) ? 22 : 0;
    // Which row to start with: 1st row is the top of the screen, 9th is the bottom
    row_offset = ((9 - rows) / 2) * 45;
  }

  @Override
  public void run() {
    try {
      Boolean print_letters = true;
      Integer i = 0, j = 0;
        
      // Printing letters
      this.gc.setFill(Color.web(Const.COLOR_FILL));
      while (!Thread.currentThread().isInterrupted()) {
        // Rows
        while (print_letters && i < 9) {
          // Letters in a row
          while (print_letters && j < 14) {
            Thread.sleep(30);

            this.gc.strokeText("" + story.charAt(i * 14 + j), 408 + j * 35,
              98 + i * 45 + even_offset + row_offset);
            this.gc.fillText("" + story.charAt(i * 14 + j), 408 + j * 35,
              98 + i * 45 + even_offset + row_offset);
            j++;

            // All letters are printed
            if (i * 14 + j == length) {
              print_letters = false;
            }
          }
          j = 0;
          i++;
        }
      }
    } catch (Exception e) {
      return;
    }
  }
}

