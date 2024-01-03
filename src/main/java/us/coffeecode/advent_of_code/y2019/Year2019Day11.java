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
package us.coffeecode.advent_of_code.y2019;

import static us.coffeecode.advent_of_code.y2019.ExecutionOption.BLOCK_UNTIL_INPUT_AVAILABLE;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.DigitConverter;
import us.coffeecode.advent_of_code.util.Point2D;

@AdventOfCodeSolution(year = 2019, day = 11, title = "Space Police")
@Component
public final class Year2019Day11 {

  private static final Long BLACK = Long.valueOf(0);

  private static final Long WHITE = Long.valueOf(1);

  private static final int LETTER_WIDTH = 5;

  @Autowired
  private IntCodeFactory icf;

  @Autowired
  private DigitConverter dc;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return calculate(pc, BLACK.longValue()).size();
  }

  @Solver(part = 2)
  public String calculatePart2(final PuzzleContext pc) {
    final Map<Point2D, Long> colors = calculate(pc, WHITE.longValue());
    // Get the boundaries of whatever it paints white.
    int minX = 0;
    int maxX = 0;
    int minY = 0;
    int maxY = 0;
    for (final var entry : colors.entrySet()) {
      if (WHITE.equals(entry.getValue())) {
        final Point2D c = entry.getKey();
        minX = Math.min(minX, c.getX());
        maxX = Math.max(maxX, c.getX());
        minY = Math.min(minY, c.getY());
        maxY = Math.max(maxY, c.getY());
      }
    }

    // Transfer the coordinates of white paint to a grid.
    boolean[][] grid = new boolean[maxY - minY + 1][maxX - minX + 1];
    for (final var entry : colors.entrySet()) {
      if (WHITE.equals(entry.getValue())) {
        final Point2D c = entry.getKey();
        final int x = c.getX() - minX;
        // Y is flipped: flip it back.
        final int y = c.getY() - minY;
        grid[y][x] = true;
      }
    }

    final StringBuilder str = new StringBuilder(8);

    // Read each letter in the grid to get the answer.
    for (int i = 0; i < grid[0].length; i += LETTER_WIDTH) {
      str.appendCodePoint(dc.toCodePoint(grid, i, LETTER_WIDTH));
    }

    return str.toString();
  }

  private Map<Point2D, Long> calculate(final PuzzleContext pc, final long startingColor) {
    final IntCode state = icf.make(pc, BLOCK_UNTIL_INPUT_AVAILABLE);
    state.getInput().add(startingColor);

    Point2D current = new Point2D(0, 0);
    Direction heading = Direction.NORTH;
    final Map<Point2D, Long> colors = new HashMap<>();

    while (true) {
      state.exec();
      final long[] results = state.getOutput().removeAll();
      if (results.length == 0) {
        break;
      }

      // First result is the color to paint
      colors.put(current, Long.valueOf(results[0]));

      // Next, change heading and move.
      heading = heading.turn(results[1]);
      current = heading.move(current);

      // Tell the robot the state of the new coordinate.
      long color = BLACK.longValue();
      if (colors.containsKey(current)) {
        color = colors.get(current).longValue();
      }
      state.getInput().add(color);
    }
    return colors;
  }

  private static enum Direction {

    // Flip north and south so part two comes out correctly.
    NORTH(0, -1),
    SOUTH(0, 1),
    EAST(1, 0),
    WEST(-1, 0);

    private static final Map<Direction, Direction> LEFT = Map.of(NORTH, WEST, WEST, SOUTH, SOUTH, EAST, EAST, NORTH);

    private static final Map<Direction, Direction> RIGHT = Map.of(NORTH, EAST, EAST, SOUTH, SOUTH, WEST, WEST, NORTH);

    private final int dx;

    private final int dy;

    private Direction(final int _dx, final int _dy) {
      dx = _dx;
      dy = _dy;
    }

    Point2D move(final Point2D current) {
      return new Point2D(current, dx, dy);
    }

    Direction turn(final long turn) {
      if (turn == 0) {
        return LEFT.get(this);
      }
      else if (turn == 1) {
        return RIGHT.get(this);
      }
      throw new IllegalArgumentException("Unhandled direction [" + turn + "]");
    }
  }

}
