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

import us.coffeecode.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2017/day/1">Year 2017, day 1</a>. This problem asks us to add up a checksum for an
 * integer array following rules: add up numbers when they are equal to the next number (part 1) or the opposite number
 * (part 2) thinking of it as a cyclic buffer.
 * </p>
 * <p>
 * This is a fairly simple and self-explanatory array iteration.
 * </p>
 * <p>
 * Copyright (c) 2021 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2017Day01 {

  public long calculatePart1() {
    final int[] input = getInput();
    long sum = 0;
    for (int i = 0; i < input.length; ++i) {
      final int next = i == input.length - 1 ? 0 : i + 1;
      if (input[i] == input[next]) {
        sum += input[i];
      }
    }
    return sum;
  }

  public long calculatePart2() {
    final int[] input = getInput();
    final int half = input.length >> 1;
    long sum = 0;
    for (int i = 0; i < input.length; ++i) {
      final int next = i < half ? i + half : i - half;
      if (input[i] == input[next]) {
        sum += input[i];
      }
    }
    return sum;
  }

  /** Get the input data for this solution. */
  private int[] getInput() {
    try {
      return Files.readString(Utils.getInput(2017, 1)).trim().codePoints().map(i -> i - '0').toArray();
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

}
