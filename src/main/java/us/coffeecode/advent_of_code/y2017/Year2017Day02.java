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
 * <a href="https://adventofcode.com/2017/day/2">Year 2017, day 2</a>. This problem asks us to perform calculations on
 * arrays of integers (rows in a spread sheet). For part one, we need to calculate the difference between the maximum
 * and minimum value in each row, and add up these differences for all rows. For part two, we need to find the two
 * values in each row where one value evenly divides the other, and add up the quotients for each row.
 * </p>
 * <p>
 * This is a simple brute force algorithm. For part one, let the streams library do the heavy lifting. For part two,
 * divide each unique pair of values and see which pair divides evenly. To simplify the logic, sort first.
 * </p>
 * <p>
 * Copyright (c) 2021 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2017Day02 {

  public long calculatePart1() {
    long checksum = 0;
    for (final int[] row : getInput()) {
      final int min = Arrays.stream(row).min().getAsInt();
      final int max = Arrays.stream(row).max().getAsInt();
      checksum += max - min;
    }
    return checksum;
  }

  public long calculatePart2() {
    long checksum = 0;
    for (final int[] row : getInput()) {
      Arrays.sort(row);
      outer: for (int i = 0; i < row.length - 1; ++i) {
        for (int j = i + 1; j < row.length; ++j) {
          if (row[j] % row[i] == 0) {
            checksum += row[j] / row[i];
            break outer;
          }
        }
      }
    }
    return checksum;
  }

  /** Get the input data for this solution. */
  private int[][] getInput() {
    try {
      return Files.readAllLines(Utils.getInput(2017, 2)).stream().map(
        s -> Arrays.stream(SEPARATOR.split(s)).mapToInt(Integer::parseInt).toArray()).toArray(int[][]::new);
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private static final Pattern SEPARATOR = Pattern.compile("\\t");
}
