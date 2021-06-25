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
 * <a href="https://adventofcode.com/2016/day/16">Year 2016, day 16</a>. This problem asks us to generate a string, then
 * process that string by reducing it according to a rule.
 * </p>
 * <p>
 * On modern hardware, this is achievable using brute force. However, there are faster ways to do this. First, a naive
 * approach to string generation would be to double it iteratively until there are enough digits. However, this involves
 * a lot of redundant string copying. Constructing the string in-place using a StringBuilder chops off about 20% of the
 * time. Similar to the generation approach, reduction can take a shortcut. Effectively, each digit in the answer
 * represents one partition of the dragon string. We know how big each partition is by computing the integer log base 2
 * of the length of the dragon string. The "checksum" algorithm is really just calculating parity: each digit is the
 * parity of one partition of the dragon string. Doing this instead of the naive "reduce one step at a time" approach
 * cuts execution time by around 65%.
 * </p>
 * <p>
 * There are further reductions in complexity. For example: we can calculate any digit of the dragon string directly
 * since it does not rely on any other digits, only its position in the string. However, the current implementation
 * executes fairly quickly after making the only other optimization that really matters: working on a boolean array
 * instead of a string. Instead of a string of ones and zeros, boolean arrays are much faster. However, the answer must
 * still be a string because part two has a leading zero. Returning a boolean array or an integer formatted as a binary
 * string will not produce the correct answer.
 * </p>
 * <p>
 * Copyright (c) 2021 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2016Day16 {

  public String calculatePart1() {
    return calculate(272);
  }

  public String calculatePart2() {
    return calculate(35_651_584);
  }

  private String calculate(final int length) {
    return checksum(dragon(getInput(), length));
  }

  private boolean[] dragon(final String input, final int length) {
    final boolean[] str = new boolean[length];
    // First "a" string.
    for (int i = 0; i < input.length(); ++i) {
      str[i] = input.charAt(i) == '1';
    }
    int nextZero = input.length();
    // Tack on zeros and "b" strings iteratively.
    for (int i = nextZero, j = nextZero - 1; i < length; ++i) {
      if (i == nextZero) {
        str[i] = false;
        j = nextZero - 1;
        nextZero = (nextZero << 1) + 1;
      }
      else {
        str[i] = !str[j];
        --j;
      }
    }
    return str;
  }

  private String checksum(final boolean[] input) {
    int partitionSize = 1;
    while (input.length % partitionSize == 0) {
      partitionSize <<= 1;
    }
    partitionSize >>= 1;
    final StringBuilder checksum = new StringBuilder(input.length / partitionSize);
    for (int i = 0; i < input.length; i += partitionSize) {
      int ones = 0;
      for (int j = i; j < i + partitionSize; ++j) {
        if (input[j]) {
          ++ones;
        }
      }
      checksum.append(ones % 2 == 0 ? '1' : '0');
    }
    // Note: the checksum may have a leading zero and yes, this matters.
    return checksum.toString();
  }

  /** Get the input data for this solution. */
  private String getInput() {
    try {
      return Files.readString(Utils.getInput(2016, 16)).trim();
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

}
