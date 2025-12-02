/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2025  John Gaughan
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
package us.coffeecode.advent_of_code.y2025;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2025, day = 2)
@Component
public class Year2025Day02 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return calculate(pc, this::isInvalidPartOne);
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return calculate(pc, this::isInvalidPartTwo);
  }

  /** Perform the common logic for the puzzle, delegating to a predicate to test if a string is invalid. */
  private long calculate(final PuzzleContext pc, final Predicate<String> isInvalid) {
    long result = 0;
    for (final long[] range : getInput(pc)) {
      for (long n = range[0]; n <= range[1]; ++n) {
        if (isInvalid.test(Long.toString(n))) {
          result += n;
        }
      }
    }
    return result;
  }

  /**
   * Checks if a value is invalid per part one's requirements. This chops a string in half and checks if both halves are
   * equal.
   */
  private boolean isInvalidPartOne(final String value) {
    if (value.length() % 2 == 1) {
      return false;
    }
    final int pivot = (value.length() >> 1);
    return value.substring(0, pivot)
                .equals(value.substring(pivot));
  }

  /**
   * Checks if a value is invalid per part two's requirements. This uses a regular expression because it is much simpler
   * than brute forcing with loops.
   */
  private boolean isInvalidPartTwo(final String value) {
    for (int tokenLength = 1; tokenLength <= (value.length() >> 1); ++tokenLength) {
      if (value.length() % tokenLength != 0) {
        // Value cannot be divided evenly into tokens of this length: skip this length.
        continue;
      }
      final String token = value.substring(0, tokenLength);
      boolean invalid = true;
      for (int i = tokenLength; invalid && (i < value.length()); i += tokenLength) {
        if (!token.equals(value.substring(i, i + tokenLength))) {
          invalid = false;
        }
      }
      if (invalid) {
        return true;
      }
    }
    return false;
  }

  /** Splits the file into individual range tokens. */
  private static final Pattern FILE_SPLIT = Pattern.compile(",");

  /** Splits a range token into begin and end values. */
  private static final Pattern RANGE_SPLIT = Pattern.compile("-");

  /**
   * Get the program input where each range is in a two-element long array, and all ranges are stored in an object that
   * can be iterated in a loop.
   */
  private Iterable<long[]> getInput(final PuzzleContext pc) {
    return il.fileAsObjectsFromSplit(pc, FILE_SPLIT, s -> Arrays.stream(RANGE_SPLIT.split(s))
                                                                .mapToLong(Long::parseLong)
                                                                .toArray());
  }
}
