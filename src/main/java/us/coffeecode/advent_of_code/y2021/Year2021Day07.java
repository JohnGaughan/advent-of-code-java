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
package us.coffeecode.advent_of_code.y2021;

import java.util.function.Function;
import java.util.function.IntUnaryOperator;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2021, day = 7, title = "The Treachery of Whales")
@Component
public final class Year2021Day07 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return calculate(pc, IntUnaryOperator.identity(), this::median);
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return calculate(pc, n -> n * (n + 1) >> 1, this::mean);
  }

  private long calculate(final PuzzleContext pc, final IntUnaryOperator scoringAlgorithm, final Function<int[], int[]> reduction) {
    final int[] array = il.fileAsSortedIntsFromSplit(pc, SPLIT);
    long lowestScore = Long.MAX_VALUE;
    for (final int loc : reduction.apply(array)) {
      final long score = IntStream.of(array).map(i -> i - loc).map(Math::abs).map(scoringAlgorithm).sum();
      if (score < lowestScore) {
        lowestScore = score;
      }
    }
    return lowestScore;
  }

  private int[] mean(final int[] a) {
    final int sum = IntStream.of(a).sum();
    final int avg = sum / a.length;
    if (avg * a.length == sum) {
      return new int[] { avg };
    }
    return new int[] { avg, avg + 1 };
  }

  private int[] median(final int[] array) {
    final int mid = (array.length >> 1);
    if (((array.length & 1) == 1) || (array[mid - 1] == array[mid])) {
      return new int[] { array[mid] };
    }
    return IntStream.rangeClosed(array[mid - 1], array[mid]).toArray();
  }

  private static final Pattern SPLIT = Pattern.compile(",");
}
