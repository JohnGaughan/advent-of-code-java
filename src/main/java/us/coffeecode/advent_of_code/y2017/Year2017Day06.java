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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import us.coffeecode.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2017/day/6">Year 2017, day 6</a>. This problem asks us to crunch an integer array
 * using an algorithm repeatedly, until the array is in a state seen before. Part one and the first half of part two ask
 * us to process the array until any state is seen again. The second half of part two asks us to repeat the algorithm
 * until that duplicated state is seen again. In both cases, return the number of iterations.
 * </p>
 * <p>
 * This solution uses a simple integer array along with a wrapper class which makes it trivial to compare two arrays
 * both inside of a set as well as in regular code. There are two calculate methods that differ slightly because the
 * exit conditions are different, but they leverage a common method for the distribution code. Part two has an
 * additional step on top of what part one does.
 * </p>
 * <p>
 * Copyright (c) 2021 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2017Day06 {

  public long calculatePart1() {
    final State state = calculate();
    return state.iterations;
  }

  public long calculatePart2() {
    final State state1 = calculate();
    final State state2 = calculate(state1.banks);
    return state2.iterations;
  }

  private State calculate() {
    final int[] banks = getInput();
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

  /** Get the input data for this solution. */
  private int[] getInput() {
    try {
      // return new int[] { 0, 2, 7, 0 };
      return Arrays.stream(SEPARATOR.split(Files.readString(Utils.getInput(2017, 6)).trim())).mapToInt(
        Integer::parseInt).toArray();
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private static final Pattern SEPARATOR = Pattern.compile("\\t");

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
      array = new int[input.length];
      // Make a copy because the calling code will modify the original.
      System.arraycopy(input, 0, array, 0, input.length);
      hashCode = Arrays.hashCode(array);
    }

    @Override
    public boolean equals(final Object obj) {
      if (this == obj) {
        return true;
      }
      else if (!(obj instanceof Wrapper)) {
        return false;
      }
      return Arrays.equals(array, ((Wrapper) obj).array);
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
