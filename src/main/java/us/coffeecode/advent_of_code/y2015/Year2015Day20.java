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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2015, day = 20)
@Component
public final class Year2015Day20 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final int input = il.fileAsInt(pc);
    final int[] presents = new int[900_000];
    for (int i = 1; i < presents.length; ++i) {
      for (int j = i; j < presents.length; j += i) {
        presents[j] += i * 10;
      }
    }
    for (int i = 1; i < presents.length; ++i) {
      if (presents[i] >= input) {
        return i;
      }
    }
    return 0;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final int input = il.fileAsInt(pc);
    final int[] presents = new int[1_000_000];
    for (int i = 1; i < presents.length; ++i) {
      for (int j = i, visited = 0; (j < presents.length) && (visited < 50); j += i, ++visited) {
        presents[j] += i * 11;
      }
    }
    for (int i = 1; i < presents.length; ++i) {
      if (presents[i] >= input) {
        return i;
      }
    }
    return 0;
  }

}
