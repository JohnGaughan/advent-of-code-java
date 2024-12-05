/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2022  John Gaughan
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
package us.coffeecode.advent_of_code.y2022;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.Point2D;

@AdventOfCodeSolution(year = 2022, day = 22, title = "Monkey Map")
@Component
public class Year2022Day22 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return calculate(getInput(pc));
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return calculate(getInput(pc));
  }

  private long calculate(final Input input) {
    Direction facing = Direction.EAST;
    Point2D location = input.start;
    for (final String command : input.commands) {
      if (Character.isAlphabetic(command.codePointAt(0))) {
        facing = DIRECTION_CHANGE.get(new FacingKey(facing, command));
      }
      else {
        final int distance = Integer.parseInt(command);
        for (int i = 0; i < distance; ++i) {
          Point2D nextLocation = facing.next(location);
          Direction nextFacing = facing;
          final Wrapping key = new Wrapping(nextLocation, facing);
          if (input.wraps.containsKey(key)) {
            final Wrapping value = input.wraps.get(key);
            nextLocation = value.point;
            nextFacing = value.facing;
          }
          if (nextLocation.get(input.board) == '#') {
            break;
          }
          location = nextLocation;
          facing = nextFacing;
        }
      }
    }
    return (location.getY() + 1) * 1_000 + (location.getX() + 1) * 4 + facing.score;
  }

  private Input getInput(final PuzzleContext pc) {
    final List<List<String>> groups = il.groups(pc);
    final List<String> group0 = groups.getFirst();
    final String group1 = groups.get(1)
                                .getFirst();

    final int[][] board = new int[group0.size()][];
    int maxX = 0;
    for (int i = 0; i < board.length; ++i) {
      maxX = Math.max(maxX, group0.get(i)
                                  .length());
    }
    for (int i = 0; i < board.length; ++i) {
      int[] row = group0.get(i)
                        .chars()
                        .toArray();
      board[i] = Arrays.copyOf(row, maxX);
      for (int x = 0; x < board[i].length; ++x) {
        if (board[i][x] == 0) {
          board[i][x] = ' ';
        }
      }
    }

    Point2D point = new Point2D(0, 0);
    for (int x = 0; x < board[0].length; ++x) {
      if (board[0][x] == '.') {
        point = new Point2D(x, 0);
      }
    }

    final List<String> tokens = new ArrayList<>(group1.length() >> 1);
    int start = 0;
    for (int i = 1; i <= group1.length(); ++i) {
      boolean startIsDigit = Character.isDigit(group1.codePointAt(start));
      boolean thisIsDigit = (i < group1.length()) && Character.isDigit(group1.codePointAt(i));
      if ((startIsDigit != thisIsDigit) || (i == group1.length())) {
        tokens.add(group1.substring(start, i));
        start = i;
      }
    }

    final Map<Wrapping, Wrapping> wraps = (pc.getBoolean("3D") ? get3dWraps(board) : get2dWraps(board));

    return new Input(board, tokens.toArray(String[]::new), point, wraps);
  }

  private Map<Wrapping, Wrapping> get2dWraps(final int[][] board) {
    final Map<Wrapping, Wrapping> wraps = new HashMap<>();
    // For each row, wrap left <--> right
    for (int y = 0; y < board.length; ++y) {
      int left = -1;
      while (board[y][left + 1] == ' ') {
        ++left;
      }
      int right = left + 1;
      while ((right < board[y].length) && (board[y][right] != ' ')) {
        ++right;
      }
      wraps.put(new Wrapping(new Point2D(left, y), Direction.WEST), new Wrapping(new Point2D(right - 1, y), Direction.WEST));
      wraps.put(new Wrapping(new Point2D(right, y), Direction.EAST), new Wrapping(new Point2D(left + 1, y), Direction.EAST));
    }
    // For each column, wrap top <--> bottom
    for (int x = 0; x < board[0].length; ++x) {
      int top = -1;
      while (board[top + 1][x] == ' ') {
        ++top;
      }
      int bottom = top + 1;
      while ((bottom < board.length) && (board[bottom][x] != ' ')) {
        ++bottom;
      }
      wraps.put(new Wrapping(new Point2D(x, top), Direction.NORTH), new Wrapping(new Point2D(x, bottom - 1), Direction.NORTH));
      wraps.put(new Wrapping(new Point2D(x, bottom), Direction.SOUTH), new Wrapping(new Point2D(x, top + 1), Direction.SOUTH));
    }
    return wraps;
  }

  private Map<Wrapping, Wrapping> get3dWraps(final int[][] board) {
    final Map<Wrapping, Wrapping> wraps = new HashMap<>();
    final int faceLength = (board.length < 50) ? 4 : 50;
    if (faceLength == 4) {
      for (int x = 0; x < 4; ++x) {
        wraps.put(new Wrapping(new Point2D(x, 3), Direction.NORTH), new Wrapping(new Point2D(11 - x, 0), Direction.SOUTH));
        wraps.put(new Wrapping(new Point2D(x, 8), Direction.SOUTH), new Wrapping(new Point2D(11 - x, 7), Direction.NORTH));
      }
      for (int x = 4; x < 8; ++x) {
        wraps.put(new Wrapping(new Point2D(x, 3), Direction.NORTH), new Wrapping(new Point2D(8, x - 4), Direction.EAST));
        wraps.put(new Wrapping(new Point2D(x, 8), Direction.SOUTH), new Wrapping(new Point2D(8, 15 - x), Direction.EAST));
      }
      for (int x = 8; x < 12; ++x) {
        wraps.put(new Wrapping(new Point2D(x, -1), Direction.NORTH), new Wrapping(new Point2D(11 - x, 4), Direction.SOUTH));
        wraps.put(new Wrapping(new Point2D(x, 12), Direction.SOUTH), new Wrapping(new Point2D(11 - x, 7), Direction.NORTH));
      }
      for (int x = 12; x < 16; ++x) {
        wraps.put(new Wrapping(new Point2D(x, 7), Direction.NORTH), new Wrapping(new Point2D(19 - x, 11), Direction.WEST));
        wraps.put(new Wrapping(new Point2D(x, 12), Direction.SOUTH), new Wrapping(new Point2D(19 - x, 0), Direction.EAST));
      }

      for (int y = 0; y < 4; ++y) {
        wraps.put(new Wrapping(new Point2D(7, y), Direction.WEST), new Wrapping(new Point2D(y + 4, 4), Direction.SOUTH));
        wraps.put(new Wrapping(new Point2D(12, y), Direction.EAST), new Wrapping(new Point2D(15, 11 - y), Direction.WEST));
      }
      for (int y = 4; y < 8; ++y) {
        wraps.put(new Wrapping(new Point2D(-1, y), Direction.WEST), new Wrapping(new Point2D(19 - y, 11), Direction.NORTH));
        wraps.put(new Wrapping(new Point2D(12, y), Direction.EAST), new Wrapping(new Point2D(19 - y, 8), Direction.SOUTH));
      }
      for (int y = 8; y < 12; ++y) {
        wraps.put(new Wrapping(new Point2D(7, y), Direction.WEST), new Wrapping(new Point2D(16 - y, 7), Direction.NORTH));
        wraps.put(new Wrapping(new Point2D(16, y), Direction.EAST), new Wrapping(new Point2D(y - 15, 11), Direction.WEST));
      }
    }
    else {
      for (int x = 0; x < 50; ++x) {
        wraps.put(new Wrapping(new Point2D(x, 99), Direction.NORTH), new Wrapping(new Point2D(50, 50 + x), Direction.EAST));
        wraps.put(new Wrapping(new Point2D(x, 200), Direction.SOUTH), new Wrapping(new Point2D(100 + x, 0), Direction.SOUTH));
      }
      for (int x = 50; x < 100; ++x) {
        wraps.put(new Wrapping(new Point2D(x, -1), Direction.NORTH), new Wrapping(new Point2D(0, x + 100), Direction.EAST));
        wraps.put(new Wrapping(new Point2D(x, 150), Direction.SOUTH), new Wrapping(new Point2D(49, x + 100), Direction.WEST));
      }
      for (int x = 100; x < 150; ++x) {
        wraps.put(new Wrapping(new Point2D(x, -1), Direction.NORTH), new Wrapping(new Point2D(x - 100, 199), Direction.NORTH));
        wraps.put(new Wrapping(new Point2D(x, 50), Direction.SOUTH), new Wrapping(new Point2D(99, x - 50), Direction.WEST));
      }

      for (int y = 0; y < 50; ++y) {
        wraps.put(new Wrapping(new Point2D(49, y), Direction.WEST), new Wrapping(new Point2D(0, 149 - y), Direction.EAST));
        wraps.put(new Wrapping(new Point2D(150, y), Direction.EAST), new Wrapping(new Point2D(99, 149 - y), Direction.WEST));
      }
      for (int y = 50; y < 100; ++y) {
        wraps.put(new Wrapping(new Point2D(49, y), Direction.WEST), new Wrapping(new Point2D(y - 50, 100), Direction.SOUTH));
        wraps.put(new Wrapping(new Point2D(100, y), Direction.EAST), new Wrapping(new Point2D(y + 50, 49), Direction.NORTH));
      }
      for (int y = 100; y < 150; ++y) {
        wraps.put(new Wrapping(new Point2D(-1, y), Direction.WEST), new Wrapping(new Point2D(50, 149 - y), Direction.EAST));
        wraps.put(new Wrapping(new Point2D(100, y), Direction.EAST), new Wrapping(new Point2D(149, 149 - y), Direction.WEST));
      }
      for (int y = 150; y < 200; ++y) {
        wraps.put(new Wrapping(new Point2D(-1, y), Direction.WEST), new Wrapping(new Point2D(y - 100, 0), Direction.SOUTH));
        wraps.put(new Wrapping(new Point2D(50, y), Direction.EAST), new Wrapping(new Point2D(y - 100, 149), Direction.NORTH));
      }
    }
    return wraps;
  }

  private record Input(int[][] board, String[] commands, Point2D start, Map<Wrapping, Wrapping> wraps) {}

  private record Wrapping(Point2D point, Direction facing) {}

  private record FacingKey(Direction direction, String change) {}

  /** This exists because enum instances cannot reference each other. */
  private static final Map<FacingKey, Direction> DIRECTION_CHANGE = Map.of( //
    new FacingKey(Direction.NORTH, "L"), Direction.WEST, //
    new FacingKey(Direction.NORTH, "R"), Direction.EAST, //
    new FacingKey(Direction.SOUTH, "L"), Direction.EAST, //
    new FacingKey(Direction.SOUTH, "R"), Direction.WEST, //
    new FacingKey(Direction.WEST, "L"), Direction.SOUTH, //
    new FacingKey(Direction.WEST, "R"), Direction.NORTH, //
    new FacingKey(Direction.EAST, "L"), Direction.NORTH, //
    new FacingKey(Direction.EAST, "R"), Direction.SOUTH);

  private static enum Direction {

    NORTH(3, 0, -1),
    SOUTH(1, 0, 1),
    WEST(2, -1, 0),
    EAST(0, 1, 0);

    final int score;

    final int dx;

    final int dy;

    Direction(final int _score, final int _dx, final int _dy) {
      score = _score;
      dx = _dx;
      dy = _dy;
    }

    Point2D next(final Point2D point) {
      return new Point2D(point.getX() + dx, point.getY() + dy);
    }
  }
}
