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
package us.coffeecode.advent_of_code.y2020;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2020, day = 3, title = "Toboggan Trajectory")
@Component
public final class Year2020Day03 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final boolean[][] slope = il.linesAs2dBooleanArray(pc, '#');
    return countTrees(slope, 1, 3);
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final boolean[][] slope = il.linesAs2dBooleanArray(pc, '#');
    long answer = countTrees(slope, 1, 1);
    answer *= countTrees(slope, 1, 3);
    answer *= countTrees(slope, 1, 5);
    answer *= countTrees(slope, 1, 7);
    answer *= countTrees(slope, 2, 1);
    return answer;
  }

  /** Count the number of trees encountered for the provided row and column changes. */
  public long countTrees(final boolean[][] slope, final long rowChange, final long colChange) {
    long trees = 0;
    int row = 0;
    int col = 0;
    while (row < slope.length) {
      if (isTree(slope, row, col)) {
        ++trees;
      }
      row += rowChange;
      col += colChange;
    }
    return trees;
  }

  /** Get whether there is a tree at the given coordinate. */
  private boolean isTree(final boolean[][] slope, final int row, final int col) {
    final int colMod = col % slope[row].length;
    return slope[row][colMod];
  }

}
