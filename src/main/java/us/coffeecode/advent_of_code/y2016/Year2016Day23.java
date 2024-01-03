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
package us.coffeecode.advent_of_code.y2016;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2016, day = 23, title = "Safe Cracking")
@Component
public final class Year2016Day23 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final State state = State.load(il.lines(pc));
    state.reg[0] = 7;
    new Interpreter().execute(state);
    return state.reg[0];
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final State state = State.load(il.lines(pc));
    state.reg[0] = 12;
    // Patch the input program to use multiplication instead of repeated incrementing.
    state.instructions[4] = new Instruction("mul b d a");
    state.instructions[5] =
      state.instructions[6] = state.instructions[7] = state.instructions[8] = state.instructions[9] = new Instruction("nop");
    new Interpreter().execute(state);
    return state.reg[0];
  }

}
