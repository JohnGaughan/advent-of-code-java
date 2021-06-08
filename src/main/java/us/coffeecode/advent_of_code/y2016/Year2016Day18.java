/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2020  John Gaughan
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
package us.coffeecode.advent_of_code.y2016;

import java.nio.file.Files;

import us.coffeecode.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2016/day/18">Year 2016, day 18</a>. This problem asks us to translate a string
 * repeatedly using a set of rules, and count certain characters in it. The two parts only differ based on how many
 * iterations there are.
 * </p>
 * <p>
 * The simple approach is to perform string operations. This worked but was too slow for my preference. Instead, operate
 * on boolean arrays. Further, we can do all of the calculations in one pass without translating (and storing!) all of
 * the rows. The final result is an optimized, but readable, algorithm that performs well. Another simplification is
 * that the transformation rules are more complicated than needed. The tile directly above the current tile does not
 * matter. All that matters are the diagonal tiles, and there is only one diagonal for the end tiles. This probably will
 * not affect runtime much, but it simplifies the logic a little bit.
 * </p>
 * <p>
 * Copyright (c) 2021 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2016Day18 {

  public long calculatePart1() {
    return calculate(40);
  }

  public long calculatePart2() {
    return calculate(400_000);
  }

  public long calculate(final int iterations) {
    boolean[] row = getInput();
    long safe = count(row);
    // Start at one: first iteration is the preceding line of code.
    for (int i = 1; i < iterations; ++i) {
      row = translate(row);
      safe += count(row);
    }
    return safe;
  }

  private long count(final boolean[] array) {
    long count = 0;
    for (boolean element : array) {
      // Safe tiles are false, traps are true. Count safe tiles.
      if (!element) {
        ++count;
      }
    }
    return count;
  }

  private boolean[] translate(final boolean[] input) {
    final boolean[] output = new boolean[input.length];
    for (int i = 0; i < input.length; ++i) {
      if (i == 0) {
        // Leftmost tile. Don't care about center - matches tile above and to the right.
        output[i] = input[i + 1];
      }
      else if (i == input.length - 1) {
        // Rightmost tile. Don't care about center - matches tile above and to the left.
        output[i] = input[i - 1];
      }
      else {
        // Somewhere in the center: if the diagonal tiles are different, this is a trap.
        output[i] = input[i - 1] != input[i + 1];
      }
    }

    return output;
  }

  /** Get the input data for this solution. */
  private boolean[] getInput() {
    try {
      return convert(Files.readString(Utils.getInput(2016, 18)).trim());
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  /** Convert the input string to a boolean array where "true" represents a trap. */
  private boolean[] convert(final String input) {
    final boolean[] result = new boolean[input.length()];
    for (int i = 0; i < result.length; ++i) {
      final char ch = input.charAt(i);
      result[i] = ch == '^';
    }
    return result;
  }

}
