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
import java.util.Arrays;
import java.util.List;
import java.util.function.IntPredicate;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2021, day = 8)
@Component
public final class Year2021Day08 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    long result = 0;
    for (final Line line : getInput(pc)) {
      for (final int i : line.output) {
        final int len = Integer.bitCount(i);
        if ((len == 2) || (len == 3) || (len == 4) || (len == 7)) {
          ++result;
        }
      }
    }
    return result;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return getInput(pc).stream()
                       .mapToLong(this::getOutputValue)
                       .sum();
  }

  private long getOutputValue(final Line line) {
    final int[] digits = mapDigits(line);
    return getOutputValue(line, digits);
  }

  private int[] mapDigits(final Line line) {
    final int[] digits = new int[10];

    // Get all numbers that have a unique number of bits set.
    digits[1] = digit(line.input, i -> bits(i, 2));
    digits[4] = digit(line.input, i -> bits(i, 4));
    digits[7] = digit(line.input, i -> bits(i, 3));
    digits[8] = digit(line.input, i -> bits(i, 7));

    // 9 is the only six-segment number that completely overlaps with 4.
    digits[9] = digit(line.input, i -> bits(i, 6) && ((i & digits[4]) == digits[4]));

    // 0 is the only six-segment number other than 9 that completely overlaps with 1.
    digits[0] = digit(line.input, i -> bits(i, 6) && (i != digits[9]) && ((i & digits[1]) == digits[1]));

    // 6 is the remaining six-segment number.
    digits[6] = digit(line.input, i -> bits(i, 6) && (i != digits[0]) && (i != digits[9]));

    // 3 is the only five-segment number that completely overlaps with 1.
    digits[3] = digit(line.input, i -> bits(i, 5) && (i & digits[1]) == digits[1]);

    // 5 is the only five-segment number that completely overlays with 6.
    digits[5] = digit(line.input, i -> bits(i, 5) && ((i | digits[6]) == digits[6]));

    // 2 is the remaining five-segment number.
    digits[2] = digit(line.input, i -> bits(i, 5) && (i != digits[3]) && (i != digits[5]));

    return digits;
  }

  private int digit(final int[] array, final IntPredicate predicate) {
    return Arrays.stream(array)
                 .filter(predicate)
                 .findFirst()
                 .getAsInt();
  }

  /** Get whether the number of bits set in i matches the desired bit count. */
  private boolean bits(final int i, final int bits) {
    return Integer.bitCount(i) == bits;
  }

  private long getOutputValue(final Line line, final int[] digits) {
    // Build the output number. IntStream.reduce() doesn't work here because it is non-sequential.
    long number = 0;
    for (final int out : line.output) {
      number *= 10;
      for (int i = 0; i < digits.length; ++i) {
        if (digits[i] == out) {
          number += i;
          break;
        }
      }
    }
    return number;
  }

  private static final Pattern SPLIT_IO = Pattern.compile("\\s\\|\\s");

  private static final Pattern SPLIT_STR = Pattern.compile("\\s");

  private List<Line> getInput(final PuzzleContext pc) {
    final List<String> lineStr = il.lines(pc);
    final List<Line> lines = new ArrayList<>(lineStr.size());
    for (final String line : lineStr) {
      final String[] ioParts = SPLIT_IO.split(line);
      final int[] input = Arrays.stream(SPLIT_STR.split(ioParts[0]))
                                .mapToInt(this::toInt)
                                .toArray();
      final int[] output = Arrays.stream(SPLIT_STR.split(ioParts[1]))
                                 .mapToInt(this::toInt)
                                 .toArray();
      for (final String str : SPLIT_STR.split(ioParts[1])) {
        toInt(str);
      }
      lines.add(new Line(input, output));
    }
    return lines;
  }

  /** Convert a segment mapping to an integer whose bits represent those segments. */
  private int toInt(final String segments) {
    // Segment a is bit 1, b is bit 2, and so on. Segments are not guaranteed to be in any order.
    int value = 0;
    for (final int ch : segments.codePoints()
                                .map(i -> i - 'a')
                                .toArray()) {
      value |= (1 << ch);
    }
    return value;
  }

  private record Line(int[] input, int[] output) {}
}
