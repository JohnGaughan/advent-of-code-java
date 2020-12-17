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

import net.johngaughan.advent_of_code.Utils;

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
public final class Year2020Day01 {

  public int calculatePart1() {
    final int[] values = getInput();
    for (int i = 0; i < values.length; ++i) {
      final int i1 = values[i];
      for (int j = i + 1; j < values.length; ++j) {
        final int j1 = values[j];
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

  public int calculatePart2() {
    final int[] values = getInput();
    for (int i = 0; i < values.length; ++i) {
      final int i1 = values[i];
      for (int j = i + 1; j < values.length; ++j) {
        final int j1 = values[j];
        for (int k = j + 1; k < values.length; ++k) {
          final int k1 = values[k];
          final int sum = i1 + j1 + k1;
          if (sum == 2020) {
            return i1 * j1 * k1;
          }
          else if (sum > 2020) {
            continue;
          }
        }
        if (i1 + j1 > 2020) {
          continue;
        }
      }
    }
    return -1;
  }

  /** Get the input data for this solution. */
  private int[] getInput() {
    try {
      return Files.readAllLines(Utils.getInput(2020, 1)).stream().mapToInt(Integer::parseInt).sorted().toArray();
    }
    catch (final RuntimeException ex) {
      throw ex;
    }
    catch (final Exception ex) {
      throw new RuntimeException(ex);
    }
  }

}
