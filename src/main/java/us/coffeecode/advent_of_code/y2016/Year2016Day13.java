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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2016, day = 13, title = "A Maze of Twisty Little Cubicles")
@Component
public final class Year2016Day13 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final int[] target = Arrays.stream(TARGET_SPLIT.split(pc.getString("target"))).mapToInt(Integer::parseInt).toArray();
    final int input = il.fileAsInt(pc);
    return traverse(100, input).get(new Point(target[0], target[1], input)).intValue();
  }

  private static final Pattern TARGET_SPLIT = Pattern.compile(",");

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return traverse(50, il.fileAsInt(pc)).size();
  }

  private Map<Point, Integer> traverse(final int limit, final int input) {
    final Map<Point, Integer> visited = new HashMap<>(limit << 1);
    visited.put(new Point(1, 1, input), Integer.valueOf(0));

    boolean updated = true;
    while (updated) {
      updated = false;
      // Work on a copy to avoid ConcurrentModificationException. This does a breadth-first search.
      for (final Map.Entry<Point, Integer> current : new ArrayList<>(visited.entrySet())) {
        final int nextPathLength = current.getValue().intValue() + 1;
        if (nextPathLength > limit) {
          continue;
        }
        // Try each direction around the point.
        for (final Point next : getAdjacents(current.getKey(), input)) {
          // Already visited: update with the better of the two paths to this point.
          if (visited.containsKey(next)) {
            final int existingLength = visited.get(next).intValue();
            if (nextPathLength < existingLength) {
              visited.put(next, Integer.valueOf(nextPathLength));
              updated = true;
            }
          }
          // Not visited: if this is not a wall, add the new node.
          else if (!next.isWall()) {
            visited.put(next, Integer.valueOf(nextPathLength));
            updated = true;
          }
        }
      }
    }
    return visited;
  }

  private Point[] getAdjacents(final Point p, final int input) {
    return new Point[] { new Point(p.x - 1, p.y, input), new Point(p.x + 1, p.y, input), new Point(p.x, p.y - 1, input),
        new Point(p.x, p.y + 1, input) };
  }

  private static final class Point {

    final int x;

    final int y;

    private final int hashCode;

    private final boolean isWall;

    Point(final int _x, final int _y, final int input) {
      x = _x;
      y = _y;
      hashCode = (x << 16) + y;
      if ((x < 0) || (y < 0)) {
        isWall = true;
      }
      else {
        final int value = (x * x) + (3 * x) + (2 * x * y) + y + (y * y) + input;
        final int count = Integer.bitCount(value);
        isWall = (count & 1) > 0;
      }
    }

    boolean isWall() {
      return isWall;
    }

    @Override
    public boolean equals(final Object obj) {
      if (this == obj) {
        return true;
      }
      else if (obj instanceof Point o) {
        return (x == o.x) && (y == o.y);
      }
      return false;
    }

    @Override
    public int hashCode() {
      return hashCode;
    }

    @Override
    public String toString() {
      return "(" + x + "," + y + ")";
    }

  }

}
