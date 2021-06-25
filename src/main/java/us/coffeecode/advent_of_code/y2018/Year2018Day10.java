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
import java.util.HashMap;
import java.util.Map;

import us.coffeecode.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2018/day/10">Year 2018, day 10</a>. This puzzle defines a group of points scattered
 * all over, and asks us to figure out the message they form when they move together. Part one asks for the message,
 * part two asks for the amount of time it takes to form the message.
 * </p>
 * <p>
 * This is a simple simulation. I use heuristics to determine when the points converge to their tightest grouping: there
 * is no reason this must be when they form a message, but it turned out to be that point. Rather than hard-coding the
 * iterations, the code determines when the points diverge and stops there. I also noted that the points move very
 * slowly, so I took a short cut and started at the closest order of magnitude to the answer to save some time.
 * </p>
 * <p>
 * The other other task is to convert points into strings. This is similar to
 * {@link us.coffeecode.advent_of_code.y2016.Year2016Day08}. Characters are 6 wide by 10 tall, so 60 bits. This fits
 * into a long integer, so the code translates portions of the text into integers which it then maps to characters. From
 * there it is trivial to construct a string.
 * </p>
 * <p>
 * Copyright (c) 2021 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2018Day10 {

  public String calculatePart1() {
    return calculate().message;
  }

  public long calculatePart2() {
    return calculate().iterations;
  }

  private Solution calculate() {
    final Point[] points = getInput();
    int xVariance = -1;
    int yVariance = -1;
    int iterations = -1;

    int boost = 10_000;
    for (final Point p : points) {
      p.x += boost * p.dx;
      p.y += boost * p.dy;
    }

    for (int i = 10_000; i < Integer.MAX_VALUE; ++i) {
      int minX = Integer.MAX_VALUE;
      int minY = Integer.MAX_VALUE;
      int maxX = Integer.MIN_VALUE;
      int maxY = Integer.MIN_VALUE;
      for (final Point p : points) {
        p.x += p.dx;
        p.y += p.dy;
        minX = Math.min(p.x, minX);
        minY = Math.min(p.y, minY);
        maxX = Math.max(p.x, maxX);
        maxY = Math.max(p.y, maxY);
      }
      int newXvariance = Math.abs(maxX - minX);
      int newYvariance = Math.abs(maxY - minY);
      if ((newXvariance > xVariance || newYvariance > yVariance) && i > boost) {
        // Points are starting to move further apart: undo this iteration and quit.
        for (final Point p : points) {
          p.x -= p.dx;
          p.y -= p.dy;
        }
        iterations = i;
        break;
      }
      xVariance = newXvariance;
      yVariance = newYvariance;
    }
    return new Solution(toString(points), iterations);
  }

  private String toString(final Point[] points) {
    int minX = Integer.MAX_VALUE;
    int minY = Integer.MAX_VALUE;
    int maxX = Integer.MIN_VALUE;
    int maxY = Integer.MIN_VALUE;
    for (final Point p : points) {
      minX = Math.min(p.x, minX);
      minY = Math.min(p.y, minY);
      maxX = Math.max(p.x, maxX);
      maxY = Math.max(p.y, maxY);
    }
    boolean[][] grid = new boolean[maxY - minY + 1][maxX - minX + 1];
    for (final Point p : points) {
      grid[p.y - minY][p.x - minX] = true;
    }
    final StringBuilder str = new StringBuilder();
    for (int x_offset = 0; x_offset < grid[0].length; x_offset += 8) {
      final long encoded = toLong(grid, x_offset, x_offset + 6);
      str.append(MAP.get(Long.valueOf(encoded)));
    }
    return str.toString();
  }

  private long toLong(final boolean[][] grid, final int x1, final int x2) {
    long result = 0;
    for (int y = 0; y < grid.length; ++y) {
      for (int x = x1; x < x2; ++x) {
        result <<= 1;
        if (grid[y][x]) {
          ++result;
        }
      }
    }
    return result;
  }

  /** Get the input data for this solution. */
  private Point[] getInput() {
    try {
      return Files.readAllLines(Utils.getInput(2018, 10)).stream().map(Point::new).toArray(Point[]::new);
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private static final class Solution {

    String message;

    int iterations;

    Solution(final String _message, final int _iterations) {
      message = _message;
      iterations = _iterations;
    }
  }

  private static final class Point {

    int x;

    int y;

    final int dx;

    final int dy;

    Point(final String input) {
      x = Integer.parseInt(input.substring(10, 16).trim());
      y = Integer.parseInt(input.substring(18, 24).trim());
      dx = Integer.parseInt(input.substring(36, 38).trim());
      dy = Integer.parseInt(input.substring(40, 42).trim());
    }
  }

  private static final Map<Long, String> MAP = new HashMap<>(32);

  static {
    // All letters from my own input and whatever I could find on the Internet.
    MAP.put(toLong("  ##   #  # #    ##    ##    ########    ##    ##    ##    #"), "A");
    MAP.put(toLong("##### #    ##    ##    ###### #    ##    ##    ##    ###### "), "B");
    MAP.put(toLong(" #### #    ##     #     #     #     #     #     #    # #### "), "C");
    MAP.put(toLong("#######     #     #     ##### #     #     #     #     ######"), "E");
    MAP.put(toLong("#######     #     #     ##### #     #     #     #     #     "), "F");
    MAP.put(toLong(" #### #    ##     #     #     #  ####    ##    ##   ## ### #"), "G");
    MAP.put(toLong("#    ##    ##    ##    ########    ##    ##    ##    ##    #"), "H");
    MAP.put(toLong("   ###    #     #     #     #     #     #     # #   #  ###  "), "J");
    MAP.put(toLong("#    ##   # #  #  # #   ##    ##    # #   #  #  #   # #    #"), "K");
    MAP.put(toLong("#     #     #     #     #     #     #     #     #     ######"), "L");
    MAP.put(toLong("#    ###   ###   ## #  ## #  ##  # ##  # ##   ###   ###    #"), "N");
    MAP.put(toLong("##### #    ##    ##    ###### #     #     #     #     #     "), "P");
    MAP.put(toLong("##### #    ##    ##    ###### #  #  #   # #   # #    ##    #"), "R");
    MAP.put(toLong("#    ##    # #  #  #  #   ##    ##   #  #  #  # #    ##    #"), "X");
    MAP.put(toLong("######     #     #    #    #    #    #    #     #     ######"), "Z");
  }

  private static Long toLong(final String input) {
    long value = 0;
    for (char ch : input.toCharArray()) {
      value <<= 1;
      if (ch == '#') {
        ++value;
      }
    }
    return Long.valueOf(value);
  }

}
