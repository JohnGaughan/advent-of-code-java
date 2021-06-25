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

import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import us.coffeecode.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2018/day/6">Year 2018, day 6</a>. This problem has us create regions on a map where
 * regions are those locations closest to each point on the map. Part one asks for the largest region that is not
 * infinite in size, while part two asks for the region with a total Manhattan distance to all points under 10,000.
 * </p>
 * <p>
 * Part one was actually more involved this time. The algorithm iterates over a grid, saving the closest point in each
 * array location. If there is a tie, store -1 which means no single point is closest. Then figure out which regions are
 * infinite in size: these are the ones that touch the outside border, since after a while no other points can ever be
 * encroach on that point's region. Finally, count the number of elements in each region, ignoring those we identified
 * as infinite. Take the largest region and that is the answer.
 * </p>
 * <p>
 * Part two is simpler. It does not require storing any grid, actually. Simply iterate over a 2D space - using iterator
 * variables only - and calculate the total Manhattan distance. If that total is under 10,000, increment the counter for
 * the size of the region. When complete, we have the number of squares in the region which is the answer.
 * </p>
 * <p>
 * Copyright (c) 2021 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2018Day06 {

  public long calculatePart1() {
    final int SIZE = 350;
    final int[][] points = getInput();
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
    int[] sizes = new int[points.length];
    for (int y = 0; y < grid.length; ++y) {
      for (int x = 0; x < grid[y].length; ++x) {
        if (grid[y][x] >= 0 && !infinites.contains(Integer.valueOf(grid[y][x]))) {
          ++sizes[grid[y][x]];
        }
      }
    }

    return Arrays.stream(sizes).max().getAsInt();
  }

  public long calculatePart2() {
    final int SIZE = 350;
    final int[][] points = getInput();
    int count = 0;
    for (int y = 0; y < SIZE; ++y) {
      for (int x = 0; x < SIZE; ++x) {
        int distance = 0;
        for (final int[] point : points) {
          distance += Math.abs(point[0] - x) + Math.abs(point[1] - y);
        }
        if (distance < 10_000) {
          ++count;
        }
      }
    }
    return count;
  }

  private static final Pattern SEPARATOR = Pattern.compile(", ");

  /** Get the input data for this solution. */
  private int[][] getInput() {
    try {
      return Files.readAllLines(Utils.getInput(2018, 6)).stream().map(
        s -> Arrays.stream(SEPARATOR.split(s)).mapToInt(Integer::parseInt).toArray()).toArray(int[][]::new);
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

}
