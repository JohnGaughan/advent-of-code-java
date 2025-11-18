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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.Point2D;

@AdventOfCodeSolution(year = 2019, day = 17)
@Component
public final class Year2019Day17 {

  @Autowired
  private IntCodeFactory icf;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final Scaffold scaffold = getScaffold(pc);
    long result = 0;
    for (int y = 2; y < scaffold.grid.length - 2; ++y) {
      for (int x = 2; x < scaffold.grid[y].length - 2; ++x) {
        // If the current location is the center of a "plus sign" of true values, this is a crossing.
        if (scaffold.grid[y][x] && scaffold.grid[y - 1][x] && scaffold.grid[y + 1][x] && scaffold.grid[y][x - 1]
          && scaffold.grid[y][x + 1]) {
          result += x * y;
        }
      }
    }
    return result;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final Scaffold scaffold = getScaffold(pc);

    // Get the route through the scaffold maze. Move forward as far as possible, then turn either left or right. If no
    // turns are possible, we reached the end.
    final String steps = getSteps(scaffold);

    // Convert the individual steps into a more descriptive string.
    final String route = toRoute(steps);

    // Calculate the solution.
    final String[] solution = bruteForce(route);
    if (solution.length != 4) {
      return 0;
    }

    final IntCode state = icf.make(pc);
    final IntCodeIoQueue input = state.getInput();

    // Give the main movement routine.
    input.add(solution[0].codePoints()
                         .mapToLong(ch -> ch)
                         .toArray());
    input.add('\n');

    // Give the three functions in order
    for (int i = 1; i < solution.length; ++i) {
      input.add(solution[i].codePoints()
                           .mapToLong(ch -> ch)
                           .toArray());
      input.add('\n');
    }

    // Answer about having a live update.
    input.add('n');
    input.add('\n');

    state.getMemory()
         .set(0, 2);
    state.exec();
    final long[] output = state.getOutput()
                               .removeAll();
    return output[output.length - 1];
  }

  private String getSteps(final Scaffold scaffold) {
    final StringBuilder steps = new StringBuilder();
    Point2D current = scaffold.start;
    Direction d = scaffold.d;
    while (true) {
      Point2D forward = d.next(current);
      final Direction left = d.left();
      final Direction right = d.right();

      // Can move forward?
      if (scaffold.isValid(forward)) {
        current = forward;
        steps.append("F");
      }

      // Can turn left and only left?
      else if (scaffold.isValid(left.next(current)) && !scaffold.isValid(right.next(current))) {
        d = left;
        steps.append("L");
      }

      // Can turn right and only right?
      else if (!scaffold.isValid(left.next(current)) && scaffold.isValid(right.next(current))) {
        d = right;
        steps.append("R");
      }

      // Can turn neither right nor left? Destination.
      else if (!scaffold.isValid(left.next(current)) && !scaffold.isValid(right.next(current))) {
        break;
      }

      else {
        throw new IllegalStateException();
      }
    }
    return steps.toString();
  }

  private String toRoute(final String steps) {
    final Collection<String> segments = new ArrayList<>(72);
    int distance = 0;
    for (final int ch : steps.codePoints()
                             .toArray()) {
      if (ch == 'F') {
        ++distance;
      }
      else {
        if (distance > 0) {
          segments.add(Integer.toString(distance));
          distance = 0;
        }
        segments.add(Character.toString(ch));
      }
    }
    segments.add(Integer.toString(distance));

    return segments.stream()
                   .collect(Collectors.joining(","));
  }

  private String[] bruteForce(final String route) {
    // Outer loop finds A, the first string. Something has to capture the start of the route, and that something is
    // string A. Capture pairs of (letter,number) going until A is too long.
    for (int i = 0; i < route.length(); ++i) {
      final int i_ch = route.codePointAt(i);
      if (i_ch == ',' && oddCommas(route, 0, i)) {
        final String a = route.substring(0, i);
        if (a.length() > 20) {
          break;
        }
        final String routeA = route.replace(a, "A");

        // Next loop finds B. Much like A captures the start of the string, something has to capture whatever is
        // immediately after A.
        for (int j = 2; j < routeA.length(); ++j) {
          final int j_ch = routeA.codePointAt(j);
          if (j_ch == 'A') {
            // Edge case: there are two As to start. Skip over the second (or third, etc) A if this is true.
            continue;
          }
          else if (j_ch == ',' && oddCommas(routeA, 2, j)) {
            // Found a B. Capture this and move on to C.
            final String b = routeA.substring(2, j);
            if (b.length() > 20) {
              // Found a capture group for B, but didn't find a solution here. Move on to the next A. Note that as long
              // as we found /something/ for B this is valid, because /something/ has to capture whatever is between two
              // As.
              break;
            }
            final String routeB = routeA.replace(b, "B");
            // Now we need to find /anything/ that remains for C. Find the first turn that is not already captured in A
            // or B.
            int cStart = -1;
            for (int k = 4; k < routeB.length(); ++k) {
              final int k_ch = routeB.codePointAt(k);
              if (k_ch == 'L' || k_ch == 'R') {
                cStart = k;
                break;
              }
            }
            // Now find the end for this C. It must be a minimum of four characters later, and ends either at an A, a B,
            // or the end of the string.
            int cEnd = -1;
            for (int k = cStart + 4; k < routeB.length(); ++k) {
              final int k_ch = routeB.codePointAt(k);
              if (k_ch == 'A' || k_ch == 'B') {
                cEnd = k - 1;
                break;
              }
            }
            if (cEnd < 0) {
              cEnd = routeB.length();
            }
            // Final substitution. If everything is 20 or fewer characters and the routine (route) only references
            // functions A, B, and C, this is a solution.
            final String c = routeB.substring(cStart, cEnd);
            final String routeC = routeB.replace(c, "C");
            if (c.length() <= 20 && routeC.length() <= 20 && routeC.codePoints()
                                                                   .allMatch(
                                                                     ch -> ch == 'A' || ch == 'B' || ch == 'C' || ch == ',')) {
              return new String[] { routeC, a, b, c };
            }
          }
        }
      }
    }
    return new String[0];
  }

  private boolean oddCommas(final String str, final int begin, final int end) {
    boolean odd = false;
    for (int i = begin; i < end; ++i) {
      if (str.codePointAt(i) == ',') {
        odd = !odd;
      }
    }
    return odd;
  }

  private Scaffold getScaffold(final PuzzleContext pc) {
    // Convert the raw output into a list of strings represents lines of text.
    final IntCode state = icf.make(pc);
    state.exec();
    final List<String> lines = new ArrayList<>(45);
    StringBuilder str = new StringBuilder(29);
    for (final long value : state.getOutput()
                                 .removeAll()) {
      if ('\n' == value && !str.isEmpty()) {
        lines.add(str.toString());
        str = new StringBuilder(29);
      }
      else {
        str.appendCodePoint((int) value);
      }
    }

    // Convert the list of strings into a 2D boolean array where true represents a scaffold. Also track the starting
    // location and direction the robot faces.
    final boolean[][] grid = new boolean[lines.size()][];
    int startX = -1;
    int startY = -1;
    Direction d = null;
    for (int y = 0; y < grid.length; ++y) {
      final String line = lines.get(y);
      grid[y] = new boolean[line.length()];
      for (int x = 0; x < grid[y].length; ++x) {
        final int ch = line.codePointAt(x);
        grid[y][x] = ch != '.';
        if (ch != '.' && ch != '#') {
          startX = x;
          startY = y;
          d = Direction.valueOf(ch);
        }
      }
    }

    return new Scaffold(grid, new Point2D(startX, startY), d);
  }

  private static enum Direction {

    N('^', 0, -1),
    S('v', 0, 1),
    W('<', -1, 0),
    E('>', 1, 0);

    public static Direction valueOf(final int c) {
      for (final Direction d : values()) {
        if (d.ch == c) {
          return d;
        }
      }
      return null;
    }

    private static final Map<Direction, Direction> LEFT = Map.of(N, W, W, S, S, E, E, N);

    private static final Map<Direction, Direction> RIGHT = Map.of(N, E, E, S, S, W, W, N);

    private final int ch;

    private final int dx;

    private final int dy;

    private Direction(final int c, final int _dx, final int _dy) {
      ch = c;
      dx = _dx;
      dy = _dy;
    }

    Point2D next(final Point2D c) {
      return new Point2D(c, dx, dy);
    }

    Direction left() {
      return LEFT.get(this);
    }

    Direction right() {
      return RIGHT.get(this);
    }

  }

  /** Represents the initial state of the scaffold. */
  private static record Scaffold(boolean[][] grid, Point2D start, Direction d) {

    boolean isValid(final Point2D c) {
      if ((c.getX() < 0) || (c.getX() >= grid[0].length) || (c.getY() < 0) || (c.getY() >= grid.length)) {
        return false;
      }
      return c.get(grid);
    }

  }

}
