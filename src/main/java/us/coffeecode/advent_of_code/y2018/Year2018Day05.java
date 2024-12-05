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
package us.coffeecode.advent_of_code.y2018;

import java.util.ArrayDeque;
import java.util.Deque;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2018, day = 5, title = "Alchemical Reduction")
@Component
public final class Year2018Day05 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return getInput(pc).length;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final int[] input = getInput(pc);
    long best = Long.MAX_VALUE;
    for (char i = 'A'; i <= 'Z'; ++i) {
      final int[] output = reduce(input, Integer.valueOf(i));
      best = Math.min(output.length, best);
    }
    return best;
  }

  /** Get the input data for this solution. */
  private int[] getInput(final PuzzleContext pc) {
    return reduce(il.fileAsCodePoints(pc));
  }

  private int[] reduce(final int[] input) {
    return reduce(input, null);
  }

  private int[] reduce(final int[] input, final Integer ignore) {
    final int _ignore = ignore == null ? 0 : ignore.intValue();
    final Deque<Integer> stack = new ArrayDeque<>(input.length);
    for (int ch : input) {
      if ((ch & 0xDF) == _ignore) {
        continue;
      }
      if (stack.isEmpty()) {
        stack.addFirst(Integer.valueOf(ch));
      }
      else {
        final int top = stack.peekFirst()
                             .intValue();
        if ((top ^ ch) == 0x20) {
          stack.removeFirst();
        }
        else {
          stack.addFirst(Integer.valueOf(ch));
        }
      }
    }
    final int[] result = new int[stack.size()];
    for (int i = 0; i < result.length; ++i) {
      result[i] = stack.removeLast()
                       .intValue();
    }
    return result;
  }

}
