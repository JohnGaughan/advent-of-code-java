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
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * <a href="https://adventofcode.com/2016/day/13">Year 2016, day 13</a>. This puzzle is all about traversing a maze
 * (graph) where the walls are generated from an algorithm using coordinates as inputs. For part 1 we need to know the
 * shortest path to a specific coordinate, and for part 2, the number of distinct points within a given traversal
 * distance.
 * </p>
 * <p>
 * The overall traversal algorithm is the same between parts. Traverse the graph a certain number of steps, keeping
 * track of the shortest distance to each node. This algorithm is a breadth-first algorithm because it is simpler,
 * avoiding recursion, and because there may be multiple ways to reach a given node. Breadth-first makes it easier to
 * avoid duplicating work. In the end we either get the path length corresponding to the desired node, or simply the
 * number of nodes, as appropriate for each part.
 * </p>
 * <p>
 * Copyright (c) 2021 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2016Day13 {

  private static final int INPUT = 1352;

  public long calculatePart1() {
    return traverse(100).get(new Point(31, 39)).intValue();
  }

  public long calculatePart2() {
    return traverse(50).size();
  }

  private Map<Point, Integer> traverse(final int limit) {
    final Map<Point, Integer> visited = new HashMap<>(limit << 1);
    visited.put(new Point(1, 1), Integer.valueOf(0));

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
        for (final Point next : getAdjacents(current.getKey())) {
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

  private Point[] getAdjacents(final Point p) {
    return new Point[] { new Point(p.x - 1, p.y), new Point(p.x + 1, p.y), new Point(p.x, p.y - 1),
        new Point(p.x, p.y + 1) };
  }

  private static final class Point {

    final int x;

    final int y;

    private final int hashCode;

    private final boolean isWall;

    Point(final int _x, final int _y) {
      x = _x;
      y = _y;
      hashCode = (x << 16) + y;
      if (x < 0 || y < 0) {
        isWall = true;
      }
      else {
        final int value = x * x + 3 * x + 2 * x * y + y + y * y + INPUT;
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
      else if (!(obj instanceof Point)) {
        return false;
      }
      Point o = (Point) obj;
      return x == o.x && y == o.y;
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
