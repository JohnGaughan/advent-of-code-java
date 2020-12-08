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
package net.johngaughan.advent.y2020;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import net.johngaughan.advent.AdventProblem;

/**
 * <p>
 * <a href="https://adventofcode.com/2020/day/1">Year 2020, day 1</a>. This problem asks for the product of numbers that
 * sum to 2020. Part one asks for the product of two numbers that sum to 2020, and part two asks for the product of
 * three numbers.
 * </p>
 * <p>
 * Simple problem with a simple solution. Start with sorted input, then iterate over the input numbers and brute force
 * it. I added some short-circuits to avoid numbers that we know cannot be solutions.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@johngaughan.net&gt;
 */
public final class Year2020Day1
implements AdventProblem {

  /** {@inheritDoc} */
  @Override
  public long calculatePart1(final Path path) {
    final List<Integer> values = parse(path);
    for (int i = 0; i < values.size(); ++i) {
      final int i1 = values.get(i).intValue();
      for (int j = i + 1; j < values.size(); ++j) {
        final int j1 = values.get(j).intValue();
        final int sum = i1 + j1;
        if (sum == 2020) {
          return i1 * j1;
        }
        else if (sum > 2020) {
          continue;
        }
      }
    }
    return -1;
  }

  /** {@inheritDoc} */
  @Override
  public long calculatePart2(final Path path) {
    final List<Integer> values = parse(path);
    for (int i = 0; i < values.size(); ++i) {
      final int i1 = values.get(i).intValue();
      for (int j = i + 1; j < values.size(); ++j) {
        final int j1 = values.get(j).intValue();
        for (int k = j + 1; k < values.size(); ++k) {
          final int k1 = values.get(k).intValue();
          final int sum = i1 + j1 + k1;
          if (sum == 2020) {
            return Integer.valueOf(i1 * j1 * k1);
          }
          else if (sum > 2020) {
            continue;
          }
        }
        if ((i1 + j1) > 2020) {
          continue;
        }
      }
    }
    return -1;
  }

  /** Parse the file located at the provided path location. */
  private List<Integer> parse(final Path path) {
    try {
      return Files.readAllLines(path).stream().map(s -> Integer.parseInt(s)).sorted().collect(Collectors.toList());
    }
    catch (final RuntimeException ex) {
      throw ex;
    }
    catch (final Exception ex) {
      throw new RuntimeException(ex);
    }
  }

}
