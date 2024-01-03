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

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.DigitConverter;

@AdventOfCodeSolution(year = 2016, day = 8, title = "Two-Factor Authentication")
@Component
public final class Year2016Day08 {

  @Autowired
  private InputLoader il;

  @Autowired
  private DigitConverter dc;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final boolean screen[][] = runSimulation(pc);
    long lit = 0;
    for (int y = 0; y < screen.length; ++y) {
      for (int x = 0; x < screen[y].length; ++x) {
        if (screen[y][x]) {
          ++lit;
        }
      }
    }
    return lit;
  }

  @Solver(part = 2)
  public String calculatePart2(final PuzzleContext pc) {
    final boolean screen[][] = runSimulation(pc);
    final StringBuilder str = new StringBuilder();
    for (int y = 0; y < screen.length; y += 5) {
      final boolean region[][] = new boolean[6][5];
      for (int y1 = 0; y1 < 5; ++y1) {
        for (int x1 = 0; x1 < 6; ++x1) {
          // Need to flip the digit
          region[x1][y1] = screen[y + y1][x1];
        }
      }
      final int codePoint = dc.toCodePoint(region, 0, 5, 6);
      str.appendCodePoint(codePoint);
    }
    return str.toString();
  }

  private boolean[][] runSimulation(final PuzzleContext pc) {
    final boolean screen[][] = new boolean[50][6];
    for (final Action action : il.linesAsObjects(pc, Action::make)) {
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

  private static enum Command {
    RECT,
    ROTATE_ROW,
    ROTATE_COLUMN;
  }

  private static final record Action(Command command, int arg1, int arg2) {

    private static final Pattern SPACE = Pattern.compile(" ");

    private static final Pattern X = Pattern.compile("x");

    static Action make(final String input) {
      final String[] tokens = SPACE.split(input);
      if ("rect".equals(tokens[0])) {
        final String[] xy = X.split(tokens[1]);
        return new Action(Command.RECT, Integer.parseInt(xy[0]), Integer.parseInt(xy[1]));
      }
      else {
        final Command cmd = "row".equals(tokens[1]) ? Command.ROTATE_ROW : Command.ROTATE_COLUMN;
        return new Action(cmd, Integer.parseInt(tokens[2].substring(2)), Integer.parseInt(tokens[4]));
      }
    }
  }

}
