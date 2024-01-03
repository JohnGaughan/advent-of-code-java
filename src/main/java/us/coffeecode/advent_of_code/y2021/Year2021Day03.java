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
package us.coffeecode.advent_of_code.y2021;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.SequencedCollection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.function.BiIntPredicate;

@AdventOfCodeSolution(year = 2021, day = 3, title = "Binary Diagnostic")
@Component
public final class Year2021Day03 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final int[][] numbers = il.linesAs2dIntArrayFromDigits(pc);
    final int[] oneBits = countOneBitsByPosition(numbers);
    final int[] gammaBits = getGamma(numbers.length, oneBits);
    final long gamma = toInt(gammaBits);
    final long epsilon = toInt(negate(gammaBits));
    return gamma * epsilon;
  }

  private int[] countOneBitsByPosition(final int[][] numbers) {
    final int[] ones = new int[numbers[0].length];
    for (final int[] number : numbers) {
      for (int bitPosition = 0; bitPosition < number.length; ++bitPosition) {
        if (number[bitPosition] == 1) {
          ++ones[bitPosition];
        }
      }
    }
    return ones;
  }

  private int[] getGamma(final int numberQuantity, final int[] ones) {
    final int[] gamma = new int[ones.length];
    for (int bit = 0; bit < ones.length; ++bit) {
      final int zeroes = numberQuantity - ones[bit];
      gamma[bit] = ones[bit] > zeroes ? 1 : 0;
    }
    return gamma;
  }

  private int[] negate(final int[] binary) {
    final int[] array = new int[binary.length];
    for (int bitPosition = 0; bitPosition < binary.length; ++bitPosition) {
      array[bitPosition] = binary[bitPosition] == 1 ? 0 : 1;
    }
    return array;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final Collection<int[]> numbers = il.linesAsIntsFromDigits(pc);
    // "z" and "o" reference the "zero" and "one" bit count.
    final long ogr = toInt(filter(numbers, 1, (z, o) -> o > z));
    final long csr = toInt(filter(numbers, 0, (z, o) -> z > o));
    return ogr * csr;
  }

  /**
   * Filter numbers down to a single number based on how their bits compare to the frequency of bits across all numbers,
   * using the supplied comparison function.
   */
  private int[] filter(final Collection<int[]> numbers, final int preferBit, final BiIntPredicate compare) {
    // Make a copy of the input. We need to reuse it later, and it is most likely immutable anyway.
    final SequencedCollection<int[]> integers = new ArrayList<>(numbers);
    // Loop over each bit position until there are no bits left OR there is a single number remaining.
    for (int bitPosition = 0; bitPosition < (integers.getFirst().length) && (integers.size() > 1); ++bitPosition) {
      final int keepBit = getBit(integers, bitPosition, preferBit, compare);
      final Iterator<int[]> iter = integers.iterator();
      while (iter.hasNext()) {
        final int[] number = iter.next();
        // If this number does not match the required bit in the current position, remove it. Future iterations of the
        // outer loop should not look at this number when counting later bits.
        if (number[bitPosition] != keepBit) {
          iter.remove();
        }
      }
    }
    return integers.getFirst();
  }

  /**
   * Given some numbers, figure out the bit value at the given position. Use the provided predicate to compare which bit
   * to use, and in a tie, use the provided tie-breaker bit value.
   */
  private int getBit(final Collection<int[]> numbers, final int bitPosition, final int tiebreakerBit, final BiIntPredicate predicate) {
    int oneCount = 0;
    for (final int[] number : numbers) {
      if (number[bitPosition] == 1) {
        ++oneCount;
      }
    }
    final int zeroCount = numbers.size() - oneCount;
    if (oneCount == zeroCount) {
      return tiebreakerBit;
    }
    return predicate.test(zeroCount, oneCount) ? 1 : 0;
  }

  private long toInt(final int[] number) {
    long i = 0;
    for (final int bitValue : number) {
      i <<= 1;
      i += bitValue;
    }
    return i;
  }

}
