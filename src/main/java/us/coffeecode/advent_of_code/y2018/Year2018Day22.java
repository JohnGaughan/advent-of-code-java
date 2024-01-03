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
package us.coffeecode.advent_of_code.y2018;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.Point2D;

@AdventOfCodeSolution(year = 2018, day = 22, title = "Mode Maze")
@Component
public final class Year2018Day22 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final Input input = getInput(pc);

    final long[][] map = getMap(input);
    long sum = 0;

    for (int y = 0; y <= input.target_y; ++y) {
      for (int x = 0; x <= input.target_x; ++x) {
        sum += map[y][x];
      }
    }
    return sum;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final Input input = getInput(pc);
    final Point2D target = new Point2D(input.target_x, input.target_y);

    final long[][] map = getMap(input);

    Path.target_x = target.getX();
    Path.target_y = target.getY();

    final Queue<Path> queue = new PriorityQueue<>();
    final Map<VisitedMapKey, Long> visited = new HashMap<>();

    // Initial location
    final Path origin = new Path(new Point2D(0, 0), 0, Tool.TORCH);
    visited.put(new VisitedMapKey(origin), Long.valueOf(0));
    queue.offer(origin);

    // Keep going as long as the queue has more to process. This loop should never end.
    while (!queue.isEmpty()) {
      final Path current = queue.poll();
      // Success: current location is the target location.
      if (target.equals(current.point)) {
        if (current.tool != Tool.TORCH) {
          queue.add(new Path(current.point, current.time + 7, Tool.TORCH));
        }
        else {
          return current.time;
        }
      }

      // Not at the target. Try each neighboring square.
      for (final Point2D neighbor : current.neighbors(map)) {
        // Moving takes one minute. Changing gear requires seven.
        final long currentTerrain = current.point.get(map);
        final long newTerrain = neighbor.get(map);
        final int time;
        final Tool tool;
        if (current.tool.isCompatible(newTerrain)) {
          tool = current.tool;
          time = current.time + 1;
        }
        else {
          tool = Tool.compatibleWith(currentTerrain, newTerrain);
          time = current.time + 8;
        }
        final Path next = new Path(neighbor, time, tool);
        // Only add this next path to the queue if it is viable.
        boolean add = true;
        final VisitedMapKey key = new VisitedMapKey(next);
        if (visited.containsKey(key)) {
          final long previousTime = visited.get(key).longValue();
          if (time >= previousTime) {
            add = false;
          }
        }
        if (add) {
          visited.put(new VisitedMapKey(next), Long.valueOf(time));
          queue.offer(next);
        }
      }
    }
    return Long.MAX_VALUE;
  }

  private long[][] getMap(final Input input) {

    // Make the map larger than the minimal rectangle. Part two may take a route past its coordinates.
    final int size_x = Math.max(input.target_x, input.target_y) << 1;
    final int size_y = Math.max(input.target_x, input.target_y) << 1;

    final long[][] geologic_index = new long[size_y][size_x];
    final long[][] erosion_level = new long[size_y][size_x];
    final long[][] risk = new long[size_y][size_x];

    erosion_level[0][0] = input.depth % MOD;

    // Fill in the left column and top row, minus the entrance.
    for (int y = 1; y < size_y; ++y) {
      geologic_index[y][0] = y * 48_271;
      erosion_level[y][0] = (geologic_index[y][0] + input.depth) % MOD;
      risk[y][0] = erosion_level[y][0] % 3;
    }
    for (int x = 1; x < size_x; ++x) {
      geologic_index[0][x] = x * 16_807;
      erosion_level[0][x] = (geologic_index[0][x] + input.depth) % MOD;
      risk[0][x] = erosion_level[0][x] % 3;
    }

    // Base cases are set up: fill in the rest of the maps.
    for (int y = 1; y < size_y; ++y) {
      for (int x = 1; x < size_x; ++x) {
        if (x == input.target_x && y == input.target_y) {
          geologic_index[y][x] = 0;
        }
        else {
          geologic_index[y][x] = erosion_level[y - 1][x] * erosion_level[y][x - 1];
        }
        erosion_level[y][x] = (geologic_index[y][x] + input.depth) % MOD;
        risk[y][x] = erosion_level[y][x] % 3;
      }
    }

    // Target's coordinates are always rocky
    risk[input.target_y][input.target_x] = 0;
    return risk;
  }

  private static final int MOD = 20_183;

  /** Get the input data for this solution. */
  private Input getInput(final PuzzleContext pc) {
    final int[] i = il.fileAsIntsFromDigitGroups(pc);
    return new Input(i[0], i[1], i[2]);
  }

  private static enum Tool {

    CLIMBING_GEAR(0, 1),
    NEITHER(1, 2),
    TORCH(0, 2);

    /* Get the other tool compatible with the given terrain */
    static Tool compatibleWith(final long terrain1, final long terrain2) {
      for (final Tool tool : values()) {
        if (tool.isCompatible(terrain1) && tool.isCompatible(terrain2)) {
          return tool;
        }
      }
      throw new IllegalStateException("No tool!");
    }

    private final long a;

    private final long b;

    private Tool(final long _a, final long _b) {
      a = _a;
      b = _b;
    }

    boolean isCompatible(final long terrain) {
      return (terrain == a) || (terrain == b);
    }

  }

  private static final class VisitedMapKey {

    final Point2D point;

    final Tool tool;

    final int hash;

    VisitedMapKey(final Path path) {
      point = path.point;
      tool = path.tool;
      hash = Objects.hash(tool, point);
    }

    @Override
    public boolean equals(final Object obj) {
      if (this == obj) {
        return true;
      }
      else if (obj instanceof VisitedMapKey o) {
        return (tool == o.tool) && Objects.equals(point, o.point);
      }
      return false;
    }

    @Override
    public int hashCode() {
      return hash;
    }
  }

  private static final class Path
  implements Comparable<Path> {

    static int target_x = -1;

    static int target_y = -1;

    final Point2D point;

    final int time;

    final Tool tool;

    final int distance;

    Path(final Point2D _point, final int _time, final Tool _tool) {
      assert target_x > 0;
      assert target_y > 0;
      point = _point;
      time = _time;
      tool = _tool;
      distance = Math.abs(target_x - point.getX()) + Math.abs(target_y - point.getY());
    }

    public List<Point2D> neighbors(final long[][] map) {
      final List<Point2D> result = new ArrayList<>(4);
      final int x = point.getX();
      final int y = point.getY();
      if (x > 0) {
        result.add(new Point2D(x - 1, y));
      }
      if (y > 0) {
        result.add(new Point2D(x, y - 1));
      }
      if (x < map[y].length - 1) {
        result.add(new Point2D(x + 1, y));
      }
      if (y < map.length - 1) {
        result.add(new Point2D(x, y + 1));
      }
      return result;
    }

    @Override
    public int compareTo(final Path o) {
      // This is used to sort in the priority queue. Most important is time, followed by Manhattan distance from the
      // target.
      if (time == o.time) {
        return distance - o.distance;
      }
      return time - o.time;
    }

    @Override
    public boolean equals(final Object obj) {
      if (this == obj) {
        return true;
      }
      else if (obj instanceof Path o) {
        return (time == o.time) && (tool == o.tool) && Objects.equals(point, o.point);
      }
      return false;
    }

    @Override
    public int hashCode() {
      return Objects.hash(Integer.valueOf(time), point, tool);
    }

    @Override
    public String toString() {
      return point + "::(" + time + "," + tool + ")";
    }

  }

  private static record Input(int depth, int target_x, int target_y) {}
}
