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

import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2017, day = 15)
@Component
public final class Year2017Day15 {

  private static final long MASK = 0xFFFF;

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    long matches = 0;
    final State state = new State(il.lines(pc));
    for (int i = 0; i < 40_000_000; ++i) {
      state.a = state.a * state.a_factor % state.quotient;
      state.b = state.b * state.b_factor % state.quotient;
      if ((state.a & MASK) == (state.b & MASK)) {
        ++matches;
      }
    }
    return matches;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    long matches = 0;
    final State state = new State(il.lines(pc));
    for (int i = 0; i < 5_000_000; ++i) {
      do {
        state.a = state.a * state.a_factor % state.quotient;
      } while ((state.a & 0x03) > 0);
      do {
        state.b = state.b * state.b_factor % state.quotient;
      } while ((state.b & 0x07) > 0);
      if ((state.a & MASK) == (state.b & MASK)) {
        ++matches;
      }
    }
    return matches;
  }

  private static final class State {

    private static final Pattern SEPARATOR = Pattern.compile(" ");

    long a;

    final long a_factor = 16_807;

    long b;

    final long b_factor = 48_271;

    final long quotient = Integer.MAX_VALUE;

    State(final List<String> input) {
      for (final String s : input) {
        final String[] tokens = SEPARATOR.split(s);
        if (s.contains("A")) {
          a = Integer.parseInt(tokens[tokens.length - 1]);
        }
        else {
          b = Integer.parseInt(tokens[tokens.length - 1]);
        }
      }
    }

  }

}
