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
package net.johngaughan.advent_of_code.y2015;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * <a href="https://adventofcode.com/2015/day/17">Year 2015, day 17</a>. Part one is a basic packing problem: how many
 * different ways can you arrange the containers so their capacity adds up to 150? Part two is a little unclear but it
 * asks us to figure out the minimum number of containers used by any solution in part one, then get the number of
 * solutions that use exactly that minimum number.
 * </p>
 * <p>
 * The solution is simple: iterate through all the combinations of containers, keeping in mind that the input data does
 * have duplicates and both combinations are distinct. From there, do some basic collection and stream operations to
 * process the results and get the number of solutions valid for each part.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@johngaughan.net&gt;
 */
public final class Year2015Day17 {

  public int calculatePart1(final Path path) {
    return solutions(new ArrayList<>(), parse(path), 150).size();
  }

  public long calculatePart2(final Path path) {
    final List<List<Integer>> solutions = solutions(new ArrayList<>(), parse(path), 150);
    final int minimum = solutions.stream().mapToInt(l -> l.size()).min().getAsInt();
    return solutions.stream().filter(l -> l.size() == minimum).count();
  }

  /** Find the number of solutions available for the given state. */
  private List<List<Integer>> solutions(final List<Integer> currentBins, final List<Integer> availableBins, final int leftToPack) {
    final List<List<Integer>> solutions = new ArrayList<>();
    for (int i = 0; i < availableBins.size(); ++i) {
      final Integer nextBin = availableBins.get(i);

      if (leftToPack < nextBin) {
        // This bin and subsequent bins cannot be solutions.
        break;
      }

      final List<Integer> newCurrentBins = new ArrayList<>(currentBins);
      newCurrentBins.add(nextBin);

      final List<Integer> newAvailableBins = new ArrayList<>(availableBins.subList(i + 1, availableBins.size()));

      final int newLeftToPack = leftToPack - nextBin;

      if (leftToPack == nextBin) {
        // Found a solution, but there might be a valid duplicate after this one. Keep going.
        solutions.add(newCurrentBins);
      }
      else {
        solutions.addAll(solutions(newCurrentBins, newAvailableBins, newLeftToPack));
      }
    }
    return solutions;
  }

  /** Parse the file located at the provided path location. */
  private List<Integer> parse(final Path path) {
    try {
      // Important: this logic sorts the numbers.
      return Files.readAllLines(path).stream().map(s -> Integer.valueOf(s)).sorted().collect(Collectors.toList());
    }
    catch (final RuntimeException ex) {
      throw ex;
    }
    catch (final Exception ex) {
      throw new RuntimeException(ex);
    }
  }

}
