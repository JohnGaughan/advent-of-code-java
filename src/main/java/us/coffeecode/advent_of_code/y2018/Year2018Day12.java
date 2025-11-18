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

import java.util.List;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2018, day = 12)
@Component
public final class Year2018Day12 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return calculate(pc, 20);
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return calculate(pc, 50_000_000_000L);
  }

  public long calculate(final PuzzleContext pc, final long iterations) {
    final Input input = new Input(il.groups(pc));
    final int padding = 150;
    boolean[] state = new boolean[padding * 2 + input.start.length];
    System.arraycopy(input.start, 0, state, padding, input.start.length);

    // Track how the count changes over time.
    long previousCount = count(state, padding);
    long previousDiff = Long.MIN_VALUE;
    int consecutive = 0;

    for (long i = 0; i < iterations; ++i) {
      state = generation(state, input.rules);

      // Compare how the count and difference changed.
      long thisCount = count(state, padding);
      long thisDiff = thisCount - previousCount;
      if (thisDiff == previousDiff) {
        ++consecutive;
        // Two consecutive equal differences is not enough: seems the magic number is three before it stays the same
        // forever.
        if (consecutive == 3) {
          // Found the equilibrium point: stop here and take a short cut.
          return thisCount + (iterations - i - 1) * previousDiff;
        }
      }
      else {
        consecutive = 0;
      }
      previousDiff = thisDiff;
      previousCount = thisCount;
    }
    return count(state, padding);
  }

  long count(final boolean[] state, final int padding) {
    long sum = 0;
    for (int i = 0; i < state.length; ++i) {
      if (state[i]) {
        sum += i - padding;
      }
    }
    return sum;
  }

  private boolean[] generation(final boolean[] inputState, final Rule[] rules) {
    final boolean[] output = new boolean[inputState.length];
    for (int i = 2; i < output.length - 2; ++i) {
      rule: for (final Rule rule : rules) {
        for (int j = 0; j < rule.match.length; ++j) {
          if (rule.match[j] != inputState[i + j - (rule.match.length >> 1)]) {
            continue rule;
          }
        }
        output[i] = rule.result;
        break;
      }
    }
    return output;
  }

  private static final class Rule {

    final boolean[] match = new boolean[5];

    final boolean result;

    Rule(final String input) {
      for (int i = 0; i < match.length; ++i) {
        match[i] = input.charAt(i) == '#';
      }
      result = input.endsWith("#");
    }
  }

  private static final class Input {

    final boolean[] start;

    final Rule[] rules;

    Input(final List<List<String>> groups) {
      final String s = groups.getFirst()
                             .getFirst()
                             .substring(15);
      start = new boolean[s.length()];
      IntStream.range(0, s.length())
               .forEach(i -> start[i] = (s.codePointAt(i) == '#'));
      rules = groups.get(1)
                    .stream()
                    .map(Rule::new)
                    .toArray(Rule[]::new);
    }

  }

}
