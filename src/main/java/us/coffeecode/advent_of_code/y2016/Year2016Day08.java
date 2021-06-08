/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2020  John Gaughan
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package us.coffeecode.advent_of_code.y2016;

import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import us.coffeecode.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2016/day/8">Year 2016, day 8</a>. This problem asks us to run a simulation on a
 * bitmap, using commands to shift values around. Once done, we need to extract data from it. For part one, we need to
 * know how many bits are set: this is a sanity check to know the simulation ran correctly. Then in part two, we need to
 * parse letters based on the bits set to true. This is similar to a simple digital LED display.
 * </p>
 * <p>
 * The simulation is fairly straightforward, but the parsing is a little rough to do without knowing the font used. I
 * printed out the bitmap and used those letters, then looked at other solutions with other inputs to get more of the
 * alphabet. Since the letters are 5x6, therefore 30 bits, this fits in an integer. I convert each segment into an
 * integer, and match them up based on precomputed values. This matches up up with known letters, so the system can
 * convert bitmap chunks to letters without machine learning or other advanced topics outside the scope of Advent of
 * Code.
 * </p>
 * <p>
 * Copyright (c) 2021 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2016Day08 {

  public long calculatePart1() {
    final boolean screen[][] = runSimulation();
    int lit = 0;
    for (int i = 0; i < screen.length; ++i) {
      for (int j = 0; j < screen[i].length; ++j) {
        if (screen[i][j]) {
          ++lit;
        }
      }
    }
    return lit;
  }

  public String calculatePart2() {
    final boolean screen[][] = runSimulation();
    StringBuilder str = new StringBuilder();
    for (int x = 0; x < screen.length; x += 5) {
      boolean region[][] = new boolean[5][6];
      for (int x1 = 0; x1 < 5; ++x1) {
        for (int y1 = 0; y1 < 6; ++y1) {
          region[x1][y1] = screen[x + x1][y1];
        }
      }
      Integer bitmap = toBitmap(region);
      Integer codePoint = bitmapsToCodePoints.get(bitmap);
      if (codePoint == null) {
        throw new IllegalArgumentException("Unknown bitmap");
      }
      str.appendCodePoint(codePoint.intValue());
    }
    return str.toString();
  }

  private Integer toBitmap(final boolean[][] letter) {
    int bitmap = 0;
    for (int y = 0; y < letter[0].length; ++y) {
      for (int x = 0; x < letter.length; ++x) {
        if (letter[x][y]) {
          ++bitmap;
        }
        bitmap <<= 1;
      }
    }
    return Integer.valueOf(bitmap);
  }

  private boolean[][] runSimulation() {
    final boolean screen[][] = new boolean[50][6];
    for (Action action : getInput()) {
      if (Command.RECT == action.command) {
        for (int x = 0; x < action.arg1; ++x) {
          for (int y = 0; y < action.arg2; ++y) {
            screen[x][y] = true;
          }
        }
      }
      else if (Command.ROTATE_ROW == action.command) {
        for (int i = 0; i < action.arg2; ++i) {
          final boolean temp = screen[screen.length - 1][action.arg1];
          for (int x = screen.length - 1; x > 0; --x) {
            screen[x][action.arg1] = screen[x - 1][action.arg1];
          }
          screen[0][action.arg1] = temp;
        }
      }
      else {
        for (int i = 0; i < action.arg2; ++i) {
          final boolean temp = screen[action.arg1][screen[action.arg1].length - 1];
          for (int y = screen[action.arg1].length - 1; y > 0; --y) {
            screen[action.arg1][y] = screen[action.arg1][y - 1];
          }
          screen[action.arg1][0] = temp;
        }
      }
    }
    return screen;
  }

  /** Get the input data for this solution. */
  private List<Action> getInput() {
    try {
      return Files.readAllLines(Utils.getInput(2016, 8)).stream().map(Action::new).collect(Collectors.toList());
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private static enum Command {
    RECT,
    ROTATE_ROW,
    ROTATE_COLUMN;
  }

  private static final class Action {

    private static final Pattern SPACE = Pattern.compile(" ");

    private static final Pattern X = Pattern.compile("x");

    final Command command;

    final int arg1;

    final int arg2;

    Action(final String input) {
      final String[] tokens = SPACE.split(input);
      if ("rect".equals(tokens[0])) {
        command = Command.RECT;
        final String[] xy = X.split(tokens[1]);
        arg1 = Integer.parseInt(xy[0]);
        arg2 = Integer.parseInt(xy[1]);
      }
      else {
        if ("row".equals(tokens[1])) {
          command = Command.ROTATE_ROW;
        }
        else {
          command = Command.ROTATE_COLUMN;
        }
        arg1 = Integer.parseInt(tokens[2].substring(2));
        arg2 = Integer.parseInt(tokens[4]);
      }
    }
  }

  private static Integer toBitmap(final String[] letter) {
    int bitmap = 0;
    for (String line : letter) {
      for (int i = 0; i < line.length(); ++i) {
        int ch = line.codePointAt(i);
        if (ch != ' ') {
          ++bitmap;
        }
        bitmap <<= 1;
      }
    }
    return Integer.valueOf(bitmap);
  }

  private static final Map<Integer, Integer> bitmapsToCodePoints = new HashMap<>();

  static {
    bitmapsToCodePoints.put(toBitmap(new String[] { " ##  ", "#  # ", "#  # ", "#### ", "#  # ", "#  # " }),
      Integer.valueOf('A'));
    bitmapsToCodePoints.put(toBitmap(new String[] { "###  ", "#  # ", "###  ", "#  # ", "#  # ", "###  " }),
      Integer.valueOf('B'));
    bitmapsToCodePoints.put(toBitmap(new String[] { " ##  ", "#  # ", "#    ", "#    ", "#  # ", " ##  " }),
      Integer.valueOf('C'));
    bitmapsToCodePoints.put(toBitmap(new String[] { "#### ", "#    ", "###  ", "#    ", "#    ", "#### " }),
      Integer.valueOf('E'));
    bitmapsToCodePoints.put(toBitmap(new String[] { "#### ", "#    ", "###  ", "#    ", "#    ", "#    " }),
      Integer.valueOf('F'));
    bitmapsToCodePoints.put(toBitmap(new String[] { " ##  ", "#  # ", "#    ", "# ## ", "#  # ", " ### " }),
      Integer.valueOf('G'));
    bitmapsToCodePoints.put(toBitmap(new String[] { "#  # ", "#  # ", "#### ", "#  # ", "#  # ", "#  # " }),
      Integer.valueOf('H'));
    bitmapsToCodePoints.put(toBitmap(new String[] { "###  ", " #   ", " #   ", " #   ", " #   ", "###  " }),
      Integer.valueOf('I'));
    bitmapsToCodePoints.put(toBitmap(new String[] { "  ## ", "   # ", "   # ", "   # ", "#  # ", " ##  " }),
      Integer.valueOf('J'));
    bitmapsToCodePoints.put(toBitmap(new String[] { "#  # ", "# #  ", "##   ", "# #  ", "# #  ", "#  # " }),
      Integer.valueOf('K'));
    bitmapsToCodePoints.put(toBitmap(new String[] { "#    ", "#    ", "#    ", "#    ", "#    ", "#### " }),
      Integer.valueOf('L'));
    bitmapsToCodePoints.put(toBitmap(new String[] { " ##  ", "#  # ", "#  # ", "#  # ", "#  # ", " ##  " }),
      Integer.valueOf('O'));
    bitmapsToCodePoints.put(toBitmap(new String[] { "###  ", "#  # ", "#  # ", "###  ", "#    ", "#    " }),
      Integer.valueOf('P'));
    bitmapsToCodePoints.put(toBitmap(new String[] { "###  ", "#  # ", "#  # ", "###  ", "# #  ", "#  # " }),
      Integer.valueOf('R'));
    bitmapsToCodePoints.put(toBitmap(new String[] { " ### ", "#    ", "#    ", " ##  ", "   # ", "###  " }),
      Integer.valueOf('S'));
    bitmapsToCodePoints.put(toBitmap(new String[] { "#  # ", "#  # ", "#  # ", "#  # ", "#  # ", " ##  " }),
      Integer.valueOf('U'));
    bitmapsToCodePoints.put(toBitmap(new String[] { "#   #", "#   #", " # # ", "  #  ", "  #  ", "  #  " }),
      Integer.valueOf('Y'));
    bitmapsToCodePoints.put(toBitmap(new String[] { "#### ", "   # ", "  #  ", " #   ", "#    ", "#### " }),
      Integer.valueOf('Z'));
  }

}
