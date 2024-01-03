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
package us.coffeecode.advent_of_code.y2017;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.Point2D;

@AdventOfCodeSolution(year = 2017, day = 14, title = "Disk Defragmentation")
@Component
public final class Year2017Day14 {

  private static final int ROWS = 128;

  @Autowired
  private InputLoader il;

  @Autowired
  private KnotHash knot;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    int ones = 0;
    for (final int[] hash : getInput(pc)) {
      for (final int i : hash) {
        ones += Integer.bitCount(i);
      }
    }
    return ones;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final boolean[][] grid = toGrid(getInput(pc));
    int groups = 0;
    for (int y = 0; y < grid.length; ++y) {
      for (int x = 0; x < grid[y].length; ++x) {
        if (grid[y][x]) {
          eraseGroup(grid, new Point2D(x, y));
          ++groups;
        }
      }
    }
    return groups;
  }

  private void eraseGroup(final boolean[][] grid, final Point2D p) {
    final Set<Point2D> visited = new HashSet<>();
    Set<Point2D> visiting = new HashSet<>();
    visiting.add(p);
    while (!visiting.isEmpty()) {
      final Set<Point2D> nextVisit = new HashSet<>();
      for (final Point2D point : visiting) {
        if (!visited.contains(point)) {
          visited.add(point);
          grid[point.getY()][point.getX()] = false;
          if ((point.getY() > 0) && grid[point.getY() - 1][point.getX()]) {
            nextVisit.add(new Point2D(point.getX(), point.getY() - 1));
          }
          if ((point.getY() < grid.length - 1) && grid[point.getY() + 1][point.getX()]) {
            nextVisit.add(new Point2D(point.getX(), point.getY() + 1));
          }
          if ((point.getX() > 0) && grid[point.getY()][point.getX() - 1]) {
            nextVisit.add(new Point2D(point.getX() - 1, point.getY()));
          }
          if ((point.getX() < grid[point.getY()].length - 1) && grid[point.getY()][point.getX() + 1]) {
            nextVisit.add(new Point2D(point.getX() + 1, point.getY()));
          }
        }
      }
      visiting = nextVisit;
    }
  }

  /** Convert hashes to a grid of bits. */
  private boolean[][] toGrid(final int[][] hashes) {
    final boolean[][] grid = new boolean[hashes.length][hashes.length];
    for (int y = 0; y < hashes.length; ++y) {
      grid[y] = hashToRow(hashes[y]);
    }
    return grid;
  }

  private boolean[] hashToRow(final int[] hash) {
    boolean[] row = new boolean[ROWS];
    for (int x = 0; x < hash.length; ++x) {
      for (int offset = 0; offset < Byte.SIZE; ++offset) {
        row[offset + (x * Byte.SIZE)] = ((hash[x] >> (Byte.SIZE - offset - 1)) & 1) > 0;
      }
    }
    return row;
  }

  /** Get the input data for this solution. */
  private int[][] getInput(final PuzzleContext pc) {
    final String input = il.fileAsString(pc);
    final int[][] hashes = new int[ROWS][];
    for (int i = 0; i < ROWS; ++i) {
      hashes[i] = knot.hash(input + "-" + i);
    }
    return hashes;
  }

}
