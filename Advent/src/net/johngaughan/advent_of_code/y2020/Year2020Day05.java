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
package net.johngaughan.advent_of_code.y2020;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * <p>
 * <a href="https://adventofcode.com/2020/day/5">Year 2020, day 5</a>. This problem deals with airplane boarding passes.
 * Each pass uses binary space partitioning to specify the row and column, with seven bits for the row and three for the
 * column. Each pass has an ID that is 8*row+col. This means that each boarding pass is essentially a regular binary
 * number, except it uses F and B for the row digits and L and R for the column digits.
 * </p>
 * <p>
 * Part one wants the highest boarding pass ID, that is, the highest number in the input file. Part two wants to find
 * the only gap between two digits in the input.
 * </p>
 * <p>
 * The key here is understanding that the input is simply a binary number encoded as a string using alternate characters
 * than zero and one. The next step is to convert it to something Java understands via string replacement, then parse it
 * into a number using base two. Once that is done, the actual processing is trivial. The hardest part was understanding
 * the problem statement well enough to make that logical leap. Sometimes, problems are so complex it is difficult to
 * see the trees instead of the forest.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@johngaughan.net&gt;
 */
public final class Year2020Day05 {

  public long calculatePart1(final Path path) {
    final int[] values = parse(path);
    return values[values.length - 1];
  }

  public long calculatePart2(final Path path) {
    final int[] ids = parse(path);
    for (int i = 0; i < ids.length; ++i) {
      final int next = ids[i] + 1;
      if (ids[i + 1] != next) {
        return next;
      }
    }
    return Long.MIN_VALUE;
  }

  /** Parse the file located at the provided path location. */
  private int[] parse(final Path path) {
    try {
      return Files.readAllLines(path).stream().mapToInt(s -> Integer.parseInt(
        s.replace('F', '0').replace('B', '1').replace('L', '0').replace('R', '1'), 2)).sorted().toArray();
    }
    catch (final RuntimeException ex) {
      throw ex;
    }
    catch (final Exception ex) {
      throw new RuntimeException(ex);
    }
  }

}
