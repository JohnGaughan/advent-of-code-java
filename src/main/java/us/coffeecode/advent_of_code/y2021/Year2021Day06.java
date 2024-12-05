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

import java.util.regex.Pattern;
import java.util.stream.LongStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2021, day = 6, title = "Lanternfish")
@Component
public final class Year2021Day06 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return calculate(pc, 80);
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return calculate(pc, 256);
  }

  private long calculate(final PuzzleContext pc, final int iterations) {
    // Map timer values (array index) to the number of fish with that timer value.
    long[] fish = new long[9];
    for (final int t : il.fileAsIntsFromSplit(pc, SPLIT)) {
      ++fish[t];
    }
    for (int i = 0; i < iterations; ++i) {
      // Process each timer value and put the results in a new array, to avoid double processing fish.
      final long[] newFish = new long[fish.length];
      for (int time = 0; time < fish.length; ++time) {
        if (time == 0) {
          // Time to spawn a new generation: reset the current generation in the new array and add the new generation.
          newFish[8] = fish[0];
          newFish[6] = fish[0];
        }
        else {
          // Copy the current generation to the new array while ticking the timer down by one.
          newFish[time - 1] += fish[time];
        }
      }
      fish = newFish;
    }
    // Number of fish is simply the sum of the fish across all timer values.
    return LongStream.of(fish)
                     .sum();
  }

  private static final Pattern SPLIT = Pattern.compile(",");
}
