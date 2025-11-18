/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2024  John Gaughan
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
package us.coffeecode.advent_of_code.y2024;

import java.util.*;
import java.util.function.ToLongFunction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.Point2D;
import us.coffeecode.advent_of_code.util.Range;

@AdventOfCodeSolution(year = 2024, day = 12)
@Component
public class Year2024Day12 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return calculate(pc, this::scorePart1);
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return calculate(pc, this::scorePart2);
  }

  public long calculate(final PuzzleContext pc, final ToLongFunction<Set<Point2D>> score) {
    final Map<Integer, Set<Point2D>> input = getInput(pc);
    final List<Set<Point2D>> regions = new ArrayList<>(input.size() << 1);
    for (final var entry : input.entrySet()) {
      regions.addAll(getDisjointRegions(entry.getValue()));
    }
    return regions.stream()
                  .mapToLong(score)
                  .sum();
  }

  private List<Set<Point2D>> getDisjointRegions(final Set<Point2D> allRegions) {
    final List<Set<Point2D>> regions = new ArrayList<>();
    // While there are still points on the board that are not part of a region
    while (!allRegions.isEmpty()) {
      final Set<Point2D> region = new HashSet<>();
      final Queue<Point2D> queue = new LinkedList<>();
      // Pick a point in the entire board as a starting point to build another region
      final Point2D first = allRegions.iterator()
                                      .next();
      allRegions.remove(first);
      queue.add(first);
      region.add(first);
      // While there is a point in the region that we have not explored, see if it has any neighbors in the current
      // region
      while (!queue.isEmpty()) {
        final Point2D next = queue.poll();
        // Check each neighbor: if it is on the board and not in the current region, add it to the current region and
        // remove it from the board so it is not a candidate for future inclusion.
        for (final Point2D neighbor : next.getCardinalNeighbors()) {
          if (allRegions.contains(neighbor) && !region.contains(neighbor)) {
            allRegions.remove(neighbor);
            region.add(neighbor);
            queue.add(neighbor);
          }
        }
      }
      regions.add(region);
    }
    return regions;
  }

  private long scorePart1(final Set<Point2D> region) {
    // For each neighbor not in the region, this point contributes one unit of perimeter.
    final long perimeter = region.stream()
                                 .mapToLong(p -> p.getCardinalNeighbors()
                                                  .stream()
                                                  .filter(n -> !region.contains(n))
                                                  .count())
                                 .sum();
    return region.size() * perimeter;
  }

  private long scorePart2(final Set<Point2D> region) {
    // Build a mapping of each direction to ranges that form a border in that direction.
    final Map<BorderMapKey, Collection<Range>> borders = new HashMap<>();

    // Populate the border mapping for the given region of points.
    for (final Point2D point : region) {
      for (final Direction d : Direction.values()) {
        if (!region.contains(d.translate(point))) {
          final BorderMapKey key = d.makeMapKey(point);
          borders.computeIfAbsent(key, k -> new ArrayList<>());
          borders.get(key)
                 .add(d.makeRange(point));
        }
      }
    }

    // Merge all the ranges in each bucket of direction and level and count them.
    long borderCount = borders.values()
                              .stream()
                              .map(this::merge)
                              .mapToLong(Collection::size)
                              .sum();
    return borderCount * region.size();
  }

  private Collection<Range> merge(final Collection<Range> ranges) {
    final List<Range> merged = new ArrayList<>(ranges);
    // Must sort first: ranges are unlikely to be in correct order.
    Collections.sort(merged);
    for (int i = 0; i < merged.size() - 1;) {
      boolean updated = false;
      final Range r1 = merged.get(i);
      final Range r2 = merged.get(i + 1);
      if (r1.getX2() == r2.getX1() - 1) {
        merged.remove(i);
        merged.set(i, new Range(r1.getX1(), r2.getX2()));
        updated = true;
      }
      if (!updated) {
        ++i;
      }
    }
    return merged;
  }

  private enum Direction {

    NORTH(0, -1),
    SOUTH(0, 1),
    WEST(-1, 0),
    EAST(1, 0);

    private final int dx, dy;

    private Direction(final int _dx, final int _dy) {
      dx = _dx;
      dy = _dy;
    }

    public Point2D translate(final Point2D point) {
      return point.add(dx, dy);
    }

    /** Create a range using the point's coordinate that is appropriate for this direction. */
    public Range makeRange(final Point2D point) {
      if (dx == 0) {
        return new Range(point.getX(), point.getX());
      }
      return new Range(point.getY(), point.getY());
    }

    /**
     * Make a map key using this direction and the point's coordinate that differentiates it from other points that
     * would form parallel lines when considering borders.
     */
    public BorderMapKey makeMapKey(final Point2D point) {
      if (dx == 0) {
        return new BorderMapKey(this, point.getY());
      }
      return new BorderMapKey(this, point.getX());
    }
  }

  /**
   * Map key used to store a combination of direction and value in that direction, such that two e.g. north borders are
   * not stored in the same bucket unless they are in the same line.
   */
  private record BorderMapKey(Direction dir, int xy) {}

  private Map<Integer, Set<Point2D>> getInput(final PuzzleContext pc) {
    final Map<Integer, Set<Point2D>> input = new HashMap<>();
    final int[][] grid = il.linesAsCodePoints(pc);
    for (int y = 0; y < grid.length; ++y) {
      for (int x = 0; x < grid[y].length; ++x) {
        final Integer key = Integer.valueOf(grid[y][x]);
        input.computeIfAbsent(key, k -> new HashSet<>());
        input.get(key)
             .add(new Point2D(x, y));
      }
    }
    return input;
  }
}
