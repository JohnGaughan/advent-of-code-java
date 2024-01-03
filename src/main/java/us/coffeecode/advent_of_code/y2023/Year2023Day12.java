/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2023  John Gaughan
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
package us.coffeecode.advent_of_code.y2023;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2023, day = 12, title = "Hot Springs")
@Component
public class Year2023Day12 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return getInput(pc).stream().mapToLong(this::arrangements).sum();
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return getInput(pc).stream().mapToLong(this::arrangements).sum();
  }

  /** Count the number of arrangements are valid for the given input. */
  private long arrangements(final Input in) {
    return arrangements(in.state, in.gearGroups, new HashMap<>(1 << 11));
  }

  /**
   * Count the number of arrangements are valid for the given state, gear groups, and using the provided cache to avoid
   * redundant calculations.
   */
  private long arrangements(final String state, final int[] gearGroups, final Map<Integer, Long> cache) {
    // Calculate the unique cache key for the current state.
    final Integer cacheKey = Integer.valueOf(Objects.hash(state, Integer.valueOf(Arrays.hashCode(gearGroups))));

    // If this state is already visited, return the previous result.
    if (cache.containsKey(cacheKey)) {
      return cache.get(cacheKey).longValue();
    }

    final long unknownGearsRemaining = state.codePoints().filter(ch -> (ch == '?')).count();
    final long brokenGearsRemaining = state.codePoints().filter(ch -> (ch == '#')).count();
    final long gearsRemaining = Arrays.stream(gearGroups).sum();

    // This cannot possibly result in success: bail early.
    if ((gearsRemaining < brokenGearsRemaining) || (gearsRemaining > unknownGearsRemaining + brokenGearsRemaining)) {
      cache.put(cacheKey, Long.valueOf(0));
      return 0;
    }

    // There are no more gears to match. The above check already handled the impossible case, so we have a solution.
    else if (gearGroups.length == 0) {
      final long count = 1;
      cache.put(cacheKey, Long.valueOf(count));
      return count;
    }

    // Get the theoretical last state location where we can try to match the next group: remaining state minus the total
    // of all remaining groups minus the minimal gap between those groups.
    final int lastBegin = state.length() - Arrays.stream(gearGroups).sum() - Math.max(0, gearGroups.length - 1);

    // Do the array copy once for all loop iterations.
    final int[] nextGroups = Arrays.stream(gearGroups).skip(1).toArray();
    long count = 0;
    for (int begin = 0; begin <= lastBegin; ++begin) {
      // [begin, end) is the range for the current group.
      final int end = begin + gearGroups[0];
      // Check that this region is valid by not containing any working gears.
      if (state.indexOf('.', begin, end) < 0) {
        // Each end of this region must either be at an end of the string, or up against a possibly working gear.
        boolean startValid = ((begin == 0) || (state.charAt(begin - 1) != '#'));
        boolean endValid = ((end == state.length()) || (state.charAt(end) != '#'));
        // Cannot skip any broken gears.
        boolean skipsBrokenGears = state.indexOf('#', 0, begin) >= 0;
        if (startValid && endValid && !skipsBrokenGears) {
          // Consume the current group, one boundary character, plus any working gears.
          int nextBegin = end + 1;
          for (; nextBegin < state.length() && state.codePointAt(nextBegin) == '.'; ++nextBegin) {}
          count += arrangements(state.substring(Math.min(nextBegin, state.length())), nextGroups, cache);
        }
      }
    }
    // Store the count for this state in the cache before returning it so if we find this state again we shortcut.
    cache.put(cacheKey, Long.valueOf(count));
    return count;
  }

  private Collection<Input> getInput(final PuzzleContext pc) {
    return il.lines(pc).stream().map(s -> parse(LINE_SPLIT.split(s), pc.getInt("repeat"))).toList();
  }

  /** Given an input line split into two tokens of gears and an integer list, parse it into an Input record. */
  private Input parse(final String[] tokens, final int repeat) {
    final String string = IntStream.range(0, repeat).mapToObj(i -> tokens[0]).collect(Collectors.joining("?"));
    final int[] array0 = Arrays.stream(INT_SPLIT.split(tokens[1])).mapToInt(Integer::parseInt).toArray();
    final int[] array = combine(IntStream.range(0, repeat).mapToObj(i -> array0).toArray(int[][]::new));
    return new Input(string, array);
  }

  /** Combine any number of integer arrays contained in an array: in other words, flatten a 2D array to 1D. */
  public static int[] combine(final int[][] arrays) {
    final int[] array = new int[Arrays.stream(arrays).mapToInt(a -> a.length).sum()];
    int start = 0;
    for (int i = 0; i < arrays.length; ++i) {
      System.arraycopy(arrays[i], 0, array, start, arrays[i].length);
      start += arrays[i].length;
    }
    return array;
  }

  /** Split the string and integer list portions of an input line. */
  private static final Pattern LINE_SPLIT = Pattern.compile(" ");

  /** Split a comma-delimited list of integers into individual numbers in string format. */
  private static final Pattern INT_SPLIT = Pattern.compile(",");

  private record Input(String state, int[] gearGroups) {}
}
