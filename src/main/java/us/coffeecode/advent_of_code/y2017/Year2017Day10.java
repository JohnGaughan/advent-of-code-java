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
import us.coffeecode.advent_of_code.y2017.knothash.KnotHash;

/**
 * <p>
 * <a href="https://adventofcode.com/2017/day/10">Year 2017, day 10</a>. This problem asks us to implement a hash
 * function. Part one implements a single round. Part two repeats the single round while also modifying the input and
 * processing the output.
 * </p>
 * <p>
 * There are no special tricks here. This solution is a little different than others because the two parts only reuse
 * the code for calculating a single round. The input and output are different.
 * </p>
 * <p>
 * The hashing code is in a separate class because it is reused. See KnotHash for more information.
 * </p>
 * <p>
 * Copyright (c) 2021 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2017Day10 {

  public long calculatePart1() {
    final int[] array = new KnotHash().hashOneRound(getPart1Input());
    return array[0] * array[1];
  }

  public String calculatePart2() {
    final int[] hash = new KnotHash().hash(getInput());
    return Utils.toHexString(hash);
  }

  private static final Pattern SEPARATOR = Pattern.compile(",");

  private int[] getPart1Input() {
    return Arrays.stream(SEPARATOR.split(getInput())).mapToInt(Integer::parseInt).toArray();
  }

  /** Get the input data for this solution. */
  private String getInput() {
    try {
      return Files.readString(Utils.getInput(2017, 10)).trim();
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

}
