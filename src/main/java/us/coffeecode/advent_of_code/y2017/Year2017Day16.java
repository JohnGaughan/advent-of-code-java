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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import us.coffeecode.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2017/day/16">Year 2017, day 16</a>. This is another array scrambling problem where
 * we need to apply transformations to an array then get its final state. The difference between parts one and two is
 * the number of times we apply the transformations.
 * </p>
 * <p>
 * The number of iterations in part two is simply too large for a purely brute force algorithm to work. Instead, use
 * memoization and look for cycles. Once we identify the cycle length, simply take the modulus of the total number of
 * iterations to the iteration count so far and we have the offset of the answer in the cache.
 * </p>
 * <p>
 * Copyright (c) 2021 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2017Day16 {

  public String calculatePart1() {
    return doIterations(1);
  }

  public String calculatePart2() {
    return doIterations(1_000_000_000);
  }

  private String doIterations(final int iterations) {
    final char[] programs = initialState();
    final Iterable<DanceMove> input = getInput();
    final List<String> cache = new ArrayList<>(48);
    for (int i = 0; i < iterations; ++i) {
      final String answer = String.valueOf(programs);
      if (cache.contains(answer)) {
        // Found a cycle. Short-circuit.
        int remainder = iterations % i;
        return cache.get(remainder);
      }
      cache.add(answer);
      for (final DanceMove move : input) {
        apply(programs, move);
      }
    }
    return String.valueOf(programs);
  }

  private char[] initialState() {
    final char[] programs = new char[16];
    for (int i = 0; i < programs.length; ++i) {
      programs[i] = (char) ('a' + i);
    }
    return programs;
  }

  private void apply(final char[] programs, final DanceMove move) {
    switch (move.op) {
      case p:
        swap(programs, (char) move.arg1, (char) move.arg2);
        break;
      case s:
        spin(programs, move.arg1);
        break;
      case x:
        swap(programs, move.arg1, move.arg2);
        break;
    }
  }

  private void spin(final char[] programs, final int positions) {
    for (int rotation = 0; rotation < positions; ++rotation) {
      final char temp = programs[programs.length - 1];
      for (int i = programs.length - 1; i > 0; --i) {
        programs[i] = programs[i - 1];
      }
      programs[0] = temp;
    }
  }

  private void swap(final char[] programs, final int position1, final int position2) {
    final char temp = programs[position1];
    programs[position1] = programs[position2];
    programs[position2] = temp;
  }

  private void swap(final char[] programs, final char program1, final char program2) {
    final int position1 = find(programs, program1);
    final int position2 = find(programs, program2);
    swap(programs, position1, position2);
  }

  private int find(final char[] programs, final char program) {
    for (int i = 0; i < programs.length; ++i) {
      if (programs[i] == program) {
        return i;
      }
    }
    throw new IllegalArgumentException(
      "Illegal character [" + program + "] not found in array " + Arrays.toString(programs));
  }

  private static final Pattern SEPARATOR = Pattern.compile(",");

  /** Get the input data for this solution. */
  private List<DanceMove> getInput() {
    try {
      return Arrays.stream(SEPARATOR.split(Files.readString(Utils.getInput(2017, 16)).trim())).map(
        DanceMove::new).collect(Collectors.toList());
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private static enum Operation {
    p,
    s,
    x;
  }

  private static final class DanceMove {

    final Operation op;

    final int arg1;

    final int arg2;

    DanceMove(final String input) {
      op = Operation.valueOf(new StringBuilder().appendCodePoint(input.codePointAt(0)).toString());
      final int slash = input.indexOf('/');
      if (op == Operation.s) {
        arg1 = Integer.parseInt(input.substring(1));
        arg2 = -1;
      }
      else if (op == Operation.x) {
        arg1 = Integer.parseInt(input.substring(1, slash));
        arg2 = Integer.parseInt(input.substring(slash + 1));
      }
      else {
        arg1 = input.codePointAt(slash - 1);
        arg2 = input.codePointAt(slash + 1);
      }
    }

    @Override
    public String toString() {
      if (op == Operation.p) {
        return op.name() + "[" + Character.toString(arg1) + "," + Character.toString(arg2) + "]";
      }
      return op.name() + "[" + arg1 + "," + arg2 + "]";
    }

  }

}
