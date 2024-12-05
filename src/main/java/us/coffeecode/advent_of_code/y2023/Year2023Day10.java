/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2023  John Gaughan
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
package us.coffeecode.advent_of_code.y2023;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.MyArrays;
import us.coffeecode.advent_of_code.util.Point2D;
import us.coffeecode.advent_of_code.visualization.IVisualizationResult;
import us.coffeecode.advent_of_code.visualization.StringVisualizationResult;

@AdventOfCodeSolution(year = 2023, day = 10, title = "Pipe Maze")
@Component
public class Year2023Day10 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return calculate(getInput(pc)).distance;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return calculatePart2Impl(pc, false).interiorPoints.size();
  }

  @Solver(part = 2, visual = true)
  public Collection<IVisualizationResult> calculatePart2Visual(final PuzzleContext pc) {
    return calculatePart2Impl(pc, true).visualResults;
  }

  /** Calculate part two, and return both the answer as well as visualization results. */
  private OutputPart2 calculatePart2Impl(final PuzzleContext pc, final boolean visual) {
    final Collection<IVisualizationResult> visualResults = new ArrayList<>(3);
    final Input input = getInput(pc);
    final OutputPart1 output = calculate(input);
    // This is an implementation of the even-odd algorithm. For each point not a part of the loop, count how many times
    // we cross the loop between that point and the top edge of the board. We need to do some pre-processing ahead of
    // time, however, to simplify the main part of the loop. The readme contains more information.
    if (visual) {
      visualResults.add(new StringVisualizationResult("Input", toString(input.board, null)));
    }

    // Overwrite junk pipe with periods to clean it up.
    for (int y = 0; y < input.board.length; ++y) {
      for (int x = 0; x < input.board[y].length; ++x) {
        if (!output.loop.contains(new Point2D(x, y))) {
          input.board[y][x] = '.';
        }
      }
    }
    int[][] boardCopy = null;
    if (visual) {
      boardCopy = MyArrays.copy(input.board);
      visualResults.add(new StringVisualizationResult("Junk Pipes Removed", toString(input.board, null)));
    }

    // Next, find vertical instance of a 7, zero or more |, and a L. Or the same, except with F and J. These sections
    // count as being a horizontal piece for the purposes of the even-odd algorithm. Replace the top part with -, to
    // make processing easier later.
    for (int y = 0; y < input.board.length; ++y) {
      for (int x = 0; x < input.board[y].length; ++x) {
        final int target;
        if (input.board[y][x] == '7') {
          target = 'L';
        }
        else if (input.board[y][x] == 'F') {
          target = 'J';
        }
        else {
          continue;
        }
        for (int y0 = y + 1; y0 < input.board.length; ++y0) {
          if (input.board[y0][x] == target) {
            input.board[y][x] = '-';
            break;
          }
          if (input.board[y0][x] != '|') {
            break;
          }
        }
      }
    }

    // Run the even-odd algorithm looking up. Use memoization by calculating one row at a time, and referencing rows
    // that were calculated during the previous loop.
    final Collection<Point2D> even = new HashSet<>(1 << 13);
    final Collection<Point2D> odd = new HashSet<>(1 << 9);
    for (int y = 0; y < input.board.length; ++y) {
      for (int x = 0; x < input.board[y].length; ++x) {
        final Point2D p = new Point2D(x, y);
        // Don't process points that are part of the loop.
        if (output.loop.contains(p)) {
          continue;
        }
        // If this is the first row, then the parity is even.
        if (y == 0) {
          even.add(p);
          continue;
        }
        // Look at each point above and count the number of times we cross the loop, until we either reach the top or
        // find a point we already know the answer for.
        Point2D above = null;
        boolean oddParity = false;
        for (int y0 = y - 1; y0 >= 0; --y0) {
          final Point2D candidate = new Point2D(x, y0);
          if (output.loop.contains(candidate)) {
            // This works because we collapsed S-curve sections earlier.
            if (candidate.get(input.board) == '-') {
              oddParity = !oddParity;
            }
          }
          else if (even.contains(candidate) || odd.contains(candidate)) {
            above = candidate;
            break;
          }
        }
        // Check for previous odd parity: even or not found cannot flip this point's parity.
        if ((above != null) && odd.contains(above)) {
          oddParity = !oddParity;
        }
        (oddParity ? odd : even).add(p);
      }
    }
    if (visual) {
      visualResults.add(new StringVisualizationResult("Interior Marked", toString(boardCopy, odd)));
    }

    return new OutputPart2(odd, visualResults);
  }

  /** Convert the board state to a string for viewing by the user. */
  private String toString(final int[][] board, final Collection<Point2D> interior) {
    final StringBuilder str = new StringBuilder(1 << 12);
    for (int y = 0; y < board.length; ++y) {
      for (int x = 0; x < board[y].length; ++x) {
        if (board[y][x] == '-') {
          str.appendCodePoint('\u2500');
        }
        else if (board[y][x] == '|') {
          str.appendCodePoint('\u2502');
        }
        else if (board[y][x] == 'F') {
          str.appendCodePoint('\u250C');
        }
        else if (board[y][x] == '7') {
          str.appendCodePoint('\u2510');
        }
        else if (board[y][x] == 'J') {
          str.appendCodePoint('\u2518');
        }
        else if (board[y][x] == 'L') {
          str.appendCodePoint('\u2514');
        }
        else if ((interior != null) && interior.contains(new Point2D(x, y))) {
          str.appendCodePoint('\u2219');
        }
        else {
          str.appendCodePoint(' ');
        }
      }
      str.appendCodePoint('\n');
    }
    return str.toString();
  }

  /**
   * Perform the calculation for part one. This method determines which points are on the loop, as well as the loop's
   * size. This information is used for part two as well, which is why this is split out and returns more information
   * than part one needs by itself.
   */
  public OutputPart1 calculate(final Input input) {
    final Collection<Point2D> seen = new HashSet<>(1 << 15);
    seen.add(input.start);

    // Iterate in both directions, incrementing the step count until they collide.
    final Point2D[] current = new Point2D[2];
    long distance = 0;
    while (true) {
      // First time through.
      if (current[0] == null) {
        final Point2D[] next = next(input.start.get(input.board), input.start);
        current[0] = next[0];
        current[1] = next[1];
      }
      // Not the first time through.
      else {
        // We looped back on a state already inspected, so go with the steps taken prior to now.
        if (seen.contains(current[0]) || seen.contains(current[1])) {
          break;
        }
        // Take another step.
        seen.add(current[0]);
        seen.add(current[1]);
        for (int i = 0; i < current.length; ++i) {
          for (final Point2D next : next(current[i].get(input.board), current[i])) {
            if (!seen.contains(next)) {
              current[i] = next;
              break;
            }
          }
        }
        ++distance;
      }
    }
    return new OutputPart1(seen, distance);
  }

  /** Given a point and a pipe character, get the two adjacent points to which it connects. */
  private Point2D[] next(final int boardPiece, final Point2D current) {
    // Origin is in upper left.
    switch (boardPiece) {
      case '-':
        return new Point2D[] { new Point2D(current.getX() - 1, current.getY()), new Point2D(current.getX() + 1, current.getY()) };
      case '|':
        return new Point2D[] { new Point2D(current.getX(), current.getY() - 1), new Point2D(current.getX(), current.getY() + 1) };
      case 'F':
        return new Point2D[] { new Point2D(current.getX() + 1, current.getY()), new Point2D(current.getX(), current.getY() + 1) };
      case '7':
        return new Point2D[] { new Point2D(current.getX() - 1, current.getY()), new Point2D(current.getX(), current.getY() + 1) };
      case 'L':
        return new Point2D[] { new Point2D(current.getX() + 1, current.getY()), new Point2D(current.getX(), current.getY() - 1) };
      case 'J':
        return new Point2D[] { new Point2D(current.getX() - 1, current.getY()), new Point2D(current.getX(), current.getY() - 1) };
    }
    throw new IllegalArgumentException(current + ", " + Character.toString(boardPiece));
  }

  /** Get the input, but mark the start location and replace it with its symbol. */
  private Input getInput(final PuzzleContext pc) {
    final int[][] board = il.linesAs2dIntArray(pc, s -> s.codePoints()
                                                         .toArray());
    int x0 = Integer.MIN_VALUE;
    int y0 = Integer.MIN_VALUE;
    outer: for (int y = 0; y < board.length; ++y) {
      for (int x = 0; x < board[y].length; ++x) {
        if (board[y][x] == 'S') {
          x0 = x;
          y0 = y;
          break outer;
        }
      }
    }
    // Determine the replacement symbol. Consider the location may be at the edge of the board.
    boolean above = false;
    boolean left = false;
    boolean right = false;
    if (y0 > 0) {
      above = (board[y0 - 1][x0] == '|') || (board[y0 - 1][x0] == '7') || (board[y0 - 1][x0] == 'F');
    }
    if (x0 > 0) {
      left = (board[y0][x0 - 1] == '-') || (board[y0][x0 - 1] == 'L') || (board[y0][x0 - 1] == 'F');
    }
    if (x0 < board[y0].length - 1) {
      right = (board[y0][x0 + 1] == '-') || (board[y0][x0 + 1] == '7') || (board[y0][x0 + 1] == 'J');
    }
    final int replacement;
    // Check each unique combination of connections. This is simplified because there are always two connections and the
    // input is always well-formed.
    if (above) {
      if (left) {
        replacement = 'J';
      }
      else if (right) {
        replacement = 'L';
      }
      else {
        replacement = '|';
      }
    }
    else if (left) {
      if (right) {
        replacement = '-';
      }
      else {
        replacement = '7';
      }
    }
    else {
      replacement = 'F';
    }
    board[y0][x0] = replacement;
    return new Input(new Point2D(x0, y0), board);
  }

  /** Contains the input data. */
  private record Input(Point2D start, int[][] board) {}

  /** Contains the output of running part one, which is also used for part two. */
  private record OutputPart1(Collection<Point2D> loop, long distance) {}

  private record OutputPart2(Collection<Point2D> interiorPoints, Collection<IVisualizationResult> visualResults) {}
}
