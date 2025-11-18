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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2017, day = 6)
@Component
public final class Year2017Day06 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final State state = calculate(pc);
    return state.iterations;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final State state1 = calculate(pc);
    final State state2 = calculate(state1.banks);
    return state2.iterations;
  }

  private State calculate(final PuzzleContext pc) {
    final int[] banks = il.fileAsIntsFromSplit(pc, SEPARATOR);
    final Set<Wrapper> seen = new HashSet<>();
    long iterations = 0;
    while (true) {
      final Wrapper w = new Wrapper(banks);
      if (seen.contains(w)) {
        break;
      }
      distribute(banks);
      seen.add(w);
      ++iterations;
    }
    return new State(iterations, banks);
  }

  private State calculate(final int[] banks) {
    final Wrapper first = new Wrapper(banks);
    long iterations = 0;
    while (true) {
      distribute(banks);
      ++iterations;
      if (first.equals(new Wrapper(banks))) {
        break;
      }
    }
    return new State(iterations, banks);
  }

  private void distribute(final int[] banks) {
    final int start = selectIndex(banks);
    int distribute = banks[start];
    banks[start] = 0;
    for (int i = start + 1; distribute > 0; ++i) {
      if (i >= banks.length) {
        i -= banks.length;
      }
      ++banks[i];
      --distribute;
    }
  }

  private int selectIndex(final int[] array) {
    // Get the index with the highest value in the array, where the lower index wins a tie.
    int i = 0;
    for (int j = 1; j < array.length; ++j) {
      if (array[j] > array[i]) {
        i = j;
      }
    }
    return i;
  }

  private static final Pattern SEPARATOR = Pattern.compile("\\s+");

  private static final class State {

    final long iterations;

    final int[] banks;

    State(final long _iterations, final int[] _banks) {
      iterations = _iterations;
      banks = new int[_banks.length];
      System.arraycopy(_banks, 0, banks, 0, _banks.length);
    }
  }

  private static final class Wrapper {

    private final int[] array;

    private final int hashCode;

    Wrapper(final int[] input) {
      // Make a copy because the input will change.
      array = Arrays.copyOf(input, input.length);
      hashCode = Arrays.hashCode(array);
    }

    @Override
    public boolean equals(final Object obj) {
      if (this == obj) {
        return true;
      }
      else if (obj instanceof Wrapper o) {
        return Arrays.equals(array, o.array);
      }
      return false;
    }

    @Override
    public int hashCode() {
      return hashCode;
    }

    @Override
    public String toString() {
      return Arrays.toString(array);
    }

  }

}
