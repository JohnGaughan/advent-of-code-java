/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2020  John Gaughan
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
package us.coffeecode.advent_of_code.y2015;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2015, day = 17, title = "No Such Thing as Too Much")
@Component
public final class Year2015Day17 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return solutions(new ArrayList<>(), getInput(pc), pc.getInt("liters")).size();
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final List<List<Integer>> solutions = solutions(new ArrayList<>(), getInput(pc), pc.getInt("liters"));
    final int minimum = solutions.stream()
                                 .mapToInt(l -> l.size())
                                 .min()
                                 .getAsInt();
    return solutions.stream()
                    .filter(l -> l.size() == minimum)
                    .count();
  }

  /** Find the number of solutions available for the given state. */
  private List<List<Integer>> solutions(final List<Integer> currentBins, final int[] availableBins, final int leftToPack) {
    final List<List<Integer>> solutions = new ArrayList<>();
    for (int i = 0; i < availableBins.length; ++i) {
      if (leftToPack < availableBins[i]) {
        // This bin and subsequent bins cannot be solutions.
        break;
      }

      final List<Integer> newCurrentBins = new ArrayList<>(currentBins);
      newCurrentBins.add(Integer.valueOf(availableBins[i]));

      final int[] newAvailableBins = Arrays.copyOfRange(availableBins, i + 1, availableBins.length);

      final int newLeftToPack = leftToPack - availableBins[i];

      if (leftToPack == availableBins[i]) {
        // Found a solution, but there might be a valid duplicate after this one. Keep going.
        solutions.add(newCurrentBins);
      }
      else {
        solutions.addAll(solutions(newCurrentBins, newAvailableBins, newLeftToPack));
      }
    }
    return solutions;
  }

  /** Get the input data for this solution. */
  private int[] getInput(final PuzzleContext pc) {
    // Important: this logic sorts the numbers.
    return il.lines(pc)
             .stream()
             .mapToInt(Integer::parseInt)
             .sorted()
             .toArray();
  }

}
