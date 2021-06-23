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
import java.util.Arrays;
import java.util.regex.Pattern;

import us.coffeecode.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2017/day/13">Year 2017, day 13</a>. At a given point in time, is an oscillating
 * scanner at position zero? Part one verifies the calculation is correct by asking for a weight of all scanners where
 * this occurs, while part two asks us to find the first time offset where we can pass by all scanners without being
 * caught.
 * </p>
 * <p>
 * This is a remainder problem. Each scanner has a period, and at the start of each period, it will catch any packet
 * going through. However, this is flipped from how these problems usually go: instead of trying to find a value where
 * the remainders are all zero, we need to fine a value where none of them are zero. The solution works, but optimizing
 * is problematic. The Chinese Remainder Theorem suggests techniques to optimize a program when we need remainders to be
 * zero. In this case, however, we need the remainders to be anything except zero. The only low-hanging fruit is that
 * the time must be an even value for the specific inputs provided. Other than that, we could optimize by checking if
 * the cumulative forbidden intervals block off all but two of a given scanner (inner loop in part two), and the current
 * time value is invalid. Then we know the other unchecked value is valid, and can increment by the interval of that
 * scanner, skipping other values. However, tracking this was a bit tricky so I decided to leave it as-is. It runs
 * fairly quickly as it is, so I think clarity of code is more important than esoteric optimizations that buy little
 * improvement.
 * </p>
 * <p>
 * Copyright (c) 2021 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2017Day13 {

  public long calculatePart1() {
    int severity = 0;
    for (final int[] scanner : getInput()) {
      if (scanner[0] % (2 * (scanner[1] - 1)) == 0) {
        severity += scanner[0] * scanner[1];
      }
    }
    return severity;
  }

  public long calculatePart2() {
    final int[][] input = getInput();
    for (int t = 0; t < Integer.MAX_VALUE; t += 2) {
      boolean caught = false;
      for (final int[] scanner : input) {
        if ((t + scanner[0]) % (2 * (scanner[1] - 1)) == 0) {
          caught = true;
          break;
        }
      }
      if (!caught) {
        return t;
      }
    }
    return Integer.MIN_VALUE;
  }

  /** Get the input data for this solution. */
  private int[][] getInput() {
    try {
      return Files.readAllLines(Utils.getInput(2017, 13)).stream().map(
        s -> Arrays.stream(SEPARATOR.split(s)).mapToInt(Integer::parseInt).toArray()).toArray(int[][]::new);
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private static final Pattern SEPARATOR = Pattern.compile(": ");
}
