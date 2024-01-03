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
package us.coffeecode.advent_of_code.y2017;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2017, day = 5, title = "A Maze of Twisty Trampolines, All Alike")
@Component
public final class Year2017Day05 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final int[] instructions = il.linesAsInts(pc);
    int pointer = 0;
    long jumps = 0;
    while ((pointer >= 0) && (pointer < instructions.length)) {
      final int oldPointer = pointer;
      pointer += instructions[pointer];
      ++jumps;
      ++instructions[oldPointer];
    }
    return jumps;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final int[] instructions = il.linesAsInts(pc);
    int pointer = 0;
    long jumps = 0;
    while ((pointer >= 0) && (pointer < instructions.length)) {
      final int oldPointer = pointer;
      pointer += instructions[pointer];
      ++jumps;
      if (instructions[oldPointer] < 3) {
        ++instructions[oldPointer];
      }
      else {
        --instructions[oldPointer];
      }
    }
    return jumps;
  }

}
