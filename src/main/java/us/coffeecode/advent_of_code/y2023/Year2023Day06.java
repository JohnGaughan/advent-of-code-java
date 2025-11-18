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
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2023, day = 6)
@Component
public class Year2023Day06 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    // Get a 2D array containing only the numbers in the input. Then build races, count each race's number of
    // distance improvements, and multiply the counts together.
    final long[][] input = il.linesAs2dLongArray(pc, s -> Arrays.stream(SPLIT.split(s))
                                                                .skip(1)
                                                                .mapToLong(Long::parseLong)
                                                                .toArray());
    return IntStream.range(0, input[0].length)
                    .mapToObj(i -> new Race(input[0][i], input[1][i]))
                    .mapToLong(this::countWinners)
                    .reduce(1, (a, b) -> a * b);
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    // Get an array containing both smashed together values, then count that single race's number of ways to beat the
    // previous best distance.
    final long[] array = il.lines(pc)
                           .stream()
                           .mapToLong(s -> Long.parseLong(Arrays.stream(SPLIT.split(s))
                                                                .skip(1)
                                                                .collect(Collectors.joining())))
                           .toArray();
    return countWinners(new Race(array[0], array[1]));
  }

  /**
   * Count how many ways there are to beat the previous best time for the given race. Do this by converting the time
   * spent holding the boat into the distance traveled. If it beats the previous record, count it. Distance is the time
   * spent holding the boat multiplied by the remaining time after letting go.
   */
  private long countWinners(final Race race) {
    return LongStream.range(1, race.time)
                     .filter(i -> (i * (race.time - i)) > race.distance)
                     .count();
  }

  /** Represents one race: the time available, and the previous farthest distance traveled. */
  private record Race(long time, long distance) {}

  /** Regex that splits tokens in the input file. */
  private static final Pattern SPLIT = Pattern.compile("\\s+");
}
