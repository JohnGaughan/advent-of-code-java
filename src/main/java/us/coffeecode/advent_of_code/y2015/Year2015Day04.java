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
package us.coffeecode.advent_of_code.y2015;

import java.nio.file.Files;

import us.coffeecode.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2015/day/4">Year 2015, day 4</a>. This problem asks us to generate MD5 hashes using
 * a constant prefix appended with a monotonically increasing suffix. The goal is to generate a hash that starts with a
 * number of zeros. Part one asks for five zeros, part two asks for six.
 * </p>
 * <p>
 * No way about it, brute force is the optimal solution here. MD5 is quite broken but not in a way that provides any
 * easy shortcuts for this problem. It was probably chosen because of the specific weakness that it is very fast: not a
 * desirable trait for cryptography, where you want to put a speed limit on password cracking. My Ryzen 3700X takes
 * around 900ms to solve part two: any visitors from 2030 or so want to laugh at how slow that was?
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2015Day04 {

  public int calculatePart1() {
    return calculate("00000");
  }

  public int calculatePart2() {
    return calculate("000000");
  }

  public int calculate(final String prefix) {
    final String input = getInput();
    for (int i = 0; i <= Long.MAX_VALUE; ++i) {
      final String plaintext = input + i;
      final String ciphertext = Utils.md5ToHex(plaintext);
      if (ciphertext.startsWith(prefix)) {
        return i;
      }
    }
    return Integer.MIN_VALUE;
  }

  /** Get the input data for this solution. */
  private String getInput() {
    try {
      return Files.readString(Utils.getInput(2015, 4)).trim();
    }
    catch (final RuntimeException ex) {
      throw ex;
    }
    catch (final Exception ex) {
      throw new RuntimeException(ex);
    }
  }

}
