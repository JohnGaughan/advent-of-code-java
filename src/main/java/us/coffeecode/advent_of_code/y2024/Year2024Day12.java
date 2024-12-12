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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.function.ToLongFunction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.Point2D;

@AdventOfCodeSolution(year = 2024, day = 12, title = "Garden Groups")
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

  private List<Set<Point2D>> getDisjointRegions(final Set<Point2D> points) {
    final List<Set<Point2D>> regions = new ArrayList<>();
    while (!points.isEmpty()) {
      final Set<Point2D> region = new HashSet<>();
      final Queue<Point2D> queue = new LinkedList<>();
      final Point2D first = points.iterator()
                                  .next();
      points.remove(first);
      queue.add(first);
      region.add(first);
      while (!queue.isEmpty()) {
        final Point2D next = queue.poll();
        for (final Point2D neighbor : next.getCardinalNeighbors()) {
          if (points.contains(neighbor) && !region.contains(neighbor)) {
            points.remove(neighbor);
            region.add(neighbor);
            queue.add(neighbor);
          }
        }
      }
      regions.add(region);
    }
    return regions;
  }

  private long scorePart1(final Set<Point2D> points) {
    long perimeter = 0;
    for (final Point2D point : points) {
      for (final Point2D neighbor : point.getCardinalNeighbors()) {
        if (!points.contains(neighbor)) {
          ++perimeter;
        }
      }
    }
    return points.size() * perimeter;
  }

  private long scorePart2(final Set<Point2D> points) {
    // TODO
    return 0;
  }

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
