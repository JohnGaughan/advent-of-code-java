/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2021  John Gaughan
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
package us.coffeecode.advent_of_code.y2021;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2021, day = 25)
@Component
public final class Year2021Day25 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final Cucumber[][] board = il.linesAsObjectsArray(pc, this::parse, Cucumber[][]::new);
    long i = 0;
    while (true) {
      ++i;
      if (!round(board)) {
        break;
      }
    }
    return i;
  }

  private boolean round(final Cucumber[][] board) {
    boolean changed = false;
    final boolean[][] movers = new boolean[board.length][board[0].length];
    // Check which east-facing cucumbers can move.
    for (int y = 0; y < board.length; ++y) {
      for (int x = 0; x < board[y].length; ++x) {
        if (board[y][x] == Cucumber.EAST) {
          movers[y][x] = Cucumber.EAST.canMove(board, x, y);
        }
      }
    }
    // Move all east-facing cucumbers at once.
    for (int y = 0; y < board.length; ++y) {
      for (int x = 0; x < board[y].length; ++x) {
        if (movers[y][x]) {
          board[y][x].move(board, x, y);
          changed = true;
          // Reset it for south-facing cucumbers.
          movers[y][x] = false;
        }
      }
    }
    // Check which south-facing cucumbers can move.
    for (int y = 0; y < board.length; ++y) {
      for (int x = 0; x < board[y].length; ++x) {
        if (board[y][x] == Cucumber.SOUTH) {
          movers[y][x] = Cucumber.SOUTH.canMove(board, x, y);
        }
      }
    }
    // Move all south-facing cucumbers at once.
    for (int y = 0; y < board.length; ++y) {
      for (int x = 0; x < board[y].length; ++x) {
        if (movers[y][x]) {
          board[y][x].move(board, x, y);
          changed = true;
        }
      }
    }
    return changed;
  }

  private Cucumber[] parse(final String line) {
    final int[] codePoints = line.codePoints()
                                 .toArray();
    final Cucumber[] array = new Cucumber[codePoints.length];
    for (int i = 0; i < array.length; ++i) {
      array[i] = Cucumber.valueOf(codePoints[i]);
    }
    return array;
  }

  private enum Cucumber {

    EAST('>') {

      @Override
      boolean canMove(final Cucumber[][] board, final int x, final int y) {
        final int x0 = (x == board[y].length - 1 ? 0 : x + 1);
        return (board[y][x0] == null);
      }

      @Override
      void move(final Cucumber[][] board, final int x, final int y) {
        final int x0 = (x == board[y].length - 1 ? 0 : x + 1);
        board[y][x0] = this;
        board[y][x] = null;
      }
    },

    SOUTH('v') {

      @Override
      boolean canMove(final Cucumber[][] board, final int x, final int y) {
        final int y0 = (y == board.length - 1 ? 0 : y + 1);
        return (board[y0][x] == null);
      }

      @Override
      void move(final Cucumber[][] board, final int x, final int y) {
        final int y0 = (y == board.length - 1 ? 0 : y + 1);
        board[y0][x] = this;
        board[y][x] = null;
      }
    };

    static Cucumber valueOf(final int codePoint) {
      for (final Cucumber candidate : values()) {
        if (candidate.codePoint == codePoint) {
          return candidate;
        }
      }
      return null;
    }

    private final int codePoint;

    private Cucumber(final int ch) {
      codePoint = ch;
    }

    abstract boolean canMove(final Cucumber[][] board, final int x, final int y);

    abstract void move(final Cucumber[][] board, final int x, final int y);

    @Override
    public String toString() {
      return Character.toString(codePoint);
    }

  }

}
