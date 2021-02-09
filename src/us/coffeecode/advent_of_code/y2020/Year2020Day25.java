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
package us.coffeecode.advent_of_code.y2020;

import java.nio.file.Files;

import us.coffeecode.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2020/day/25">Year 2020, day 25</a>. The final, one-part puzzle is a fairly simple
 * brute force algorithm. Using two public keys, reverse engineer an algorithm and determine an encryption key.
 * </p>
 * <p>
 * The algorithm is spelled out pretty much verbatim in the problem statement, although I found it a little confusing.
 * Generally, the example problem includes different data from the real problem. This time, some of the data was the
 * same, namely, the multiplier used to figure out the number of iterations. Aside from that, this is a simple and
 * straightforward problem without any short cuts.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public class Year2020Day25 {

  private static final long MODULO = 20201227;

  public long calculatePart1() {
    int[] input = getInput();

    final int cardKey = input[0];
    final int doorKey = input[1];

    // Get the loop size.
    long value = 1;
    int doorLoops = 0;
    while (value != doorKey) {
      value *= 7;
      value %= MODULO;
      ++doorLoops;
    }

    // Get the encryption key.
    long key = 1;
    for (int i = 0; i < doorLoops; ++i) {
      key *= cardKey;
      key %= MODULO;
    }

    return key;
  }

  /** Get the input data for this solution. */
  private int[] getInput() {
    try {
      return Files.readAllLines(Utils.getInput(2020, 25)).stream().mapToInt(Integer::parseInt).toArray();
    }
    catch (final RuntimeException ex) {
      throw ex;
    }
    catch (final Exception ex) {
      throw new RuntimeException(ex);
    }
  }

}
