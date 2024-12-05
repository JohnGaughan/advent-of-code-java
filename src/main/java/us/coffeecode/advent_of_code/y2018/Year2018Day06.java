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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2018, day = 6, title = "Chronal Coordinates")
@Component
public final class Year2018Day06 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final int[][] points = il.linesAs2dIntArrayFromSplit(pc, SEPARATOR);
    final int[][] grid = new int[SIZE][SIZE];

    // For each point in the grid, store the ID of the point that is closest.
    for (int y = 0; y < grid.length; ++y) {
      for (int x = 0; x < grid[y].length; ++x) {
        // For each grid location, store the ID of the point with the shortest Manhattan distance.
        int closestPoint = -1;
        int closestDistance = Integer.MAX_VALUE;
        for (int p = 0; p < points.length; ++p) {
          // Manhattan distance.
          int distance = Math.abs(points[p][0] - x) + Math.abs(points[p][1] - y);
          if (distance < closestDistance) {
            closestDistance = distance;
            closestPoint = p;
          }
          else if (distance == closestDistance) {
            // On the line
            closestPoint = -1;
          }
        }
        grid[y][x] = closestPoint;
      }
    }

    // Get all points in infinite regions. These are regions that touch the outside of the grid.
    final Set<Integer> infinites = new HashSet<>();
    for (int y = 0; y < grid.length; ++y) {
      infinites.add(Integer.valueOf(grid[y][0]));
      infinites.add(Integer.valueOf(grid[y][SIZE - 1]));
    }
    for (int x = 0; x < grid[0].length; ++x) {
      infinites.add(Integer.valueOf(grid[0][x]));
      infinites.add(Integer.valueOf(grid[SIZE - 1][x]));
    }

    // Get the size of each region that is not infinite.
    final int[] sizes = new int[points.length];
    for (int y = 0; y < grid.length; ++y) {
      for (int x = 0; x < grid[y].length; ++x) {
        if (grid[y][x] >= 0 && !infinites.contains(Integer.valueOf(grid[y][x]))) {
          ++sizes[grid[y][x]];
        }
      }
    }

    return Arrays.stream(sizes)
                 .max()
                 .getAsInt();
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final int maxDistance = pc.getInt("distance");
    final int[][] points = il.linesAs2dIntArrayFromSplit(pc, SEPARATOR);
    long count = 0;
    for (int y = 0; y < SIZE; ++y) {
      for (int x = 0; x < SIZE; ++x) {
        int distance = 0;
        for (final int[] point : points) {
          distance += Math.abs(point[0] - x) + Math.abs(point[1] - y);
        }
        if (distance < maxDistance) {
          ++count;
        }
      }
    }
    return count;
  }

  private static final int SIZE = 350;

  private static final Pattern SEPARATOR = Pattern.compile(", ");
}
