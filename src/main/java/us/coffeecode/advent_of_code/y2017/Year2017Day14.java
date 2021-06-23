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

import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;

import us.coffeecode.advent_of_code.Utils;
import us.coffeecode.advent_of_code.y2017.knothash.KnotHash;

/**
 * <p>
 * <a href="https://adventofcode.com/2017/day/14">Year 2017, day 14</a>. This problem asks us to analyze knot hashes,
 * reused from day 10. Part one is a sanity check that the code is reused correctly, by counting the bits set to true.
 * Part two asks us to group bits based on them being adjacent in a matrix.
 * </p>
 * <p>
 * Part one is trivial: iterate the integers in the hash and use Java's built-in function to count the bits. Part two
 * iterates over all bits in the grid. Once we find a bit set to true, erase that group by setting its bits to false,
 * then keep iterating. To erase a group, perform a breadth-first search of all neighboring bits set to true, and set
 * them to false.
 * </p>
 * <p>
 * Copyright (c) 2021 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2017Day14 {

  private static final int ROWS = 128;

  public long calculatePart1() {
    int ones = 0;
    for (final int[] hash : getInput()) {
      for (final int i : hash) {
        ones += Integer.bitCount(i);
      }
    }
    return ones;
  }

  public long calculatePart2() {
    final boolean[][] grid = toGrid(getInput());
    int groups = 0;
    for (int y = 0; y < grid.length; ++y) {
      for (int x = 0; x < grid[y].length; ++x) {
        if (grid[y][x]) {
          eraseGroup(grid, new Point(x, y));
          ++groups;
        }
      }
    }
    return groups;
  }

  private void eraseGroup(final boolean[][] grid, final Point p) {
    final Set<Point> visited = new HashSet<>();
    Set<Point> visiting = new HashSet<>();
    visiting.add(p);
    while (!visiting.isEmpty()) {
      final Set<Point> nextVisit = new HashSet<>();
      for (final Point point : visiting) {
        if (!visited.contains(point)) {
          visited.add(point);
          grid[point.y][point.x] = false;
          if (point.y > 0 && grid[point.y - 1][point.x]) {
            nextVisit.add(new Point(point.x, point.y - 1));
          }
          if (point.y < grid.length - 1 && grid[point.y + 1][point.x]) {
            nextVisit.add(new Point(point.x, point.y + 1));
          }
          if (point.x > 0 && grid[point.y][point.x - 1]) {
            nextVisit.add(new Point(point.x - 1, point.y));
          }
          if (point.x < grid[point.y].length - 1 && grid[point.y][point.x + 1]) {
            nextVisit.add(new Point(point.x + 1, point.y));
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
        row[offset + x * Byte.SIZE] = (hash[x] >> Byte.SIZE - offset - 1 & 1) > 0;
      }
    }
    return row;
  }

  /** Get the input data for this solution. */
  private int[][] getInput() {
    try {
      final String input = Files.readString(Utils.getInput(2017, 14)).trim();
      int[][] hashes = new int[ROWS][];
      for (int i = 0; i < ROWS; ++i) {
        hashes[i] = new KnotHash().hash(input + "-" + i);
      }
      return hashes;
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private static final class Point {

    final int x;

    final int y;

    final int hashCode;

    Point(final int _x, final int _y) {
      x = _x;
      y = _y;
      hashCode = (x << 16) + y;
    }

    @Override
    public boolean equals(final Object obj) {
      if (this == obj) {
        return true;
      }
      else if (!(obj instanceof Point)) {
        return false;
      }
      Point p = (Point) obj;
      return x == p.x && y == p.y;
    }

    @Override
    public int hashCode() {
      return hashCode;
    }

  }

}
