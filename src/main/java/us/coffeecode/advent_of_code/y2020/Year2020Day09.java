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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2020, day = 9)
@Component
public final class Year2020Day09 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return getPart1Answer(pc, il.linesAsObjects(pc, Long::valueOf));
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final List<Long> numbers = il.linesAsObjects(pc, Long::valueOf);
    final long n = getPart1Answer(pc, numbers);
    int lower = 0;
    int upper = 1;
    long sum = numbers.get(lower)
                      .longValue()
      + numbers.get(upper)
               .longValue();
    // Alternative between growing the upper bound and shrinking the lower bound until we find a range.
    while (upper < numbers.size()) {
      if (sum == n) {
        final List<Long> range = numbers.subList(lower, upper + 1);
        final long min = range.stream()
                              .mapToLong(Long::longValue)
                              .min()
                              .getAsLong();
        final long max = range.stream()
                              .mapToLong(Long::longValue)
                              .max()
                              .getAsLong();
        return min + max;
      }

      if (sum < n) {
        // Sum is too low, grab more numbers.
        ++upper;
        if (upper < numbers.size()) {
          // Technically, upper can be out of bounds here. This check will prevent an exception and allow the program to
          // return incorrect results instead.
          sum += numbers.get(upper)
                        .longValue();
        }
      }
      else {
        // Sum is too high, let go of numbers.
        sum -= numbers.get(lower)
                      .longValue();
        ++lower;
      }
    }
    return 0;
  }

  /** Get the answer to part 1, which is reused in part 2. */
  private long getPart1Answer(final PuzzleContext pc, final List<Long> numbers) {
    final int window = pc.getInt("window");
    for (int i = window; i < numbers.size(); ++i) {
      final long n = numbers.get(i)
                            .longValue();
      if (!canSum(n, numbers.subList(i - window, i))) {
        return n;
      }
    }
    return 0;
  }

  /** Get whether any two of the provided numbers can add up to the number given. */
  private boolean canSum(final long n, final List<Long> numbers) {
    for (int i = 0; i < numbers.size() - 1; ++i) {
      for (int j = i + 1; j < numbers.size(); ++j) {
        final long n_i = numbers.get(i)
                                .longValue();
        final long n_j = numbers.get(j)
                                .longValue();
        if (n == n_i + n_j) {
          return true;
        }
      }
    }
    return false;
  }

}
