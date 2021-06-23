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
import java.util.List;
import java.util.regex.Pattern;

import us.coffeecode.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2017/day/15">Year 2017, day 15</a>. This problem asks us to run two pseudo-random
 * number generators that use different seeds and factors, and compare the values they produce to see when the lower 16
 * bits match. Part two adds a constraint that some values produced need to be skipped.
 * </p>
 * <p>
 * This is a brute-force algorithm. Run the PRNGs over and over and check the results. I was not able to find any way to
 * speed this up. Not even optimizing out the modulo operator using bit tricks made any difference. I opted to keep the
 * code simple since nothing more complex made a dent in the runtime which is an order of magnitude greater than any
 * problems in 2017 so far. My theory is that between the JVM and CPU, something sees what is going on and has built-in
 * optimizations that code changes cannot beat.
 * </p>
 * <p>
 * Copyright (c) 2021 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2017Day15 {

  private static final long MASK = 0xFFFF;

  public long calculatePart1() {
    int matches = 0;
    State state = getInput();
    for (int i = 0; i < 40_000_000; ++i) {
      state.a = state.a * state.a_factor % state.quotient;
      state.b = state.b * state.b_factor % state.quotient;
      if ((state.a & MASK) == (state.b & MASK)) {
        ++matches;
      }
    }
    return matches;
  }

  public long calculatePart2() {
    int matches = 0;
    State state = getInput();
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

  /** Get the input data for this solution. */
  private State getInput() {
    try {
      return new State(Files.readAllLines(Utils.getInput(2017, 15)));
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
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
