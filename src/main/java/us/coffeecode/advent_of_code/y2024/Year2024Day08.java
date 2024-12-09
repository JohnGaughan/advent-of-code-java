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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.Point2D;

@AdventOfCodeSolution(year = 2024, day = 8, title = "Resonant Collinearity")
@Component
public class Year2024Day08 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return calculate(pc, (p1, p2, grid) -> {
      final Collection<Point2D> antinodes = new ArrayList<>(2);
      final int dx = p1.getX() - p2.getX();
      final int dy = p1.getY() - p2.getY();
      // Antinodes are linear with the points, one in each direction.
      antinodes.add(p1.add(dx, dy));
      antinodes.add(p2.add(-dx, -dy));
      return antinodes.stream()
                      .filter(p -> p.isIn(grid))
                      .toList();
    });
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return calculate(pc, (p1, p2, grid) -> {
      final Collection<Point2D> antinodes = new ArrayList<>(32);
      final int dx = p1.getX() - p2.getX();
      final int dy = p1.getY() - p2.getY();
      // Add antinodes in the direction of p1, starting with p1.
      Point2D next = p1;
      while (next.isIn(grid)) {
        antinodes.add(next);
        next = next.add(dx, dy);
      }
      // Add antinodes in the direction of p2, starting with p2.
      next = p2;
      while (next.isIn(grid)) {
        antinodes.add(next);
        next = next.add(-dx, -dy);
      }
      return antinodes;
    });
  }

  public long calculate(final PuzzleContext pc, final AntinodeGenerator func) {
    final Input input = getInput(pc);
    final Set<Point2D> antinodes = new HashSet<>(2_048);
    for (final Integer key : input.antennas.keySet()) {
      antinodes.addAll(getAntinodes(input.antennas.get(key), func, input.grid));
    }
    return antinodes.size();
  }

  /** Get all antinodes for a single frequency of antenna. */
  private Collection<Point2D> getAntinodes(final List<Point2D> antennas, final AntinodeGenerator func, final int[][] grid) {
    final Collection<Point2D> antinodes = new HashSet<>();
    for (int i = 0; i < antennas.size() - 1; ++i) {
      for (int j = i + 1; j < antennas.size(); ++j) {
        antinodes.addAll(func.getAntinodes(antennas.get(i), antennas.get(j), grid));
      }
    }
    return antinodes;
  }

  private Input getInput(final PuzzleContext pc) {
    final int[][] grid = il.linesAsCodePoints(pc);
    final Map<Integer, List<Point2D>> antennas = new HashMap<>();
    for (int y = 0; y < grid.length; ++y) {
      for (int x = 0; x < grid[y].length; ++x) {
        if (Character.isDigit(grid[y][x]) || Character.isAlphabetic(grid[y][x])) {
          final Integer key = Integer.valueOf(grid[y][x]);
          antennas.computeIfAbsent(key, k -> new ArrayList<>());
          antennas.get(key)
                  .add(new Point2D(x, y));
        }
      }
    }
    return new Input(antennas, grid);
  }

  private record Input(Map<Integer, List<Point2D>> antennas, int[][] grid) {}

  @FunctionalInterface
  private static interface AntinodeGenerator {

    /** Get all antinodes for a single pair of antennas. */
    Collection<Point2D> getAntinodes(final Point2D p1, final Point2D p2, final int[][] grid);
  }
}
