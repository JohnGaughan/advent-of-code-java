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
package us.coffeecode.advent_of_code.y2019;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2019, day = 21, title = "Springdroid Adventure")
@Component
public final class Year2019Day21 {

  @Autowired
  private IntCodeFactory icf;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return calculate(pc, "NOT B J\n" // J = !B: jump if B is a hole.
      + "NOT C T\n" // T = !C: jump is C is a hole.
      + "OR T J\n" // J = J or T: jump if either of the previous conditions are true.
      + "AND D J\n" // J = J and D: cancel previous jump if D is a hole.
      + "NOT A T\n" // T = !A: jump is A is a hole.
      + "OR T J\n" // J = J or T: always jump if A is a hole.
      + "WALK\n");
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return calculate(pc, "NOT B J\n" // J = !B: jump if B is a hole.
      + "NOT C T\n"// T = !C: jump is C is a hole.
      + "OR T J\n" // J = J or T: jump if either of the previous conditions are true.
      + "AND D J\n" // J = J and D: cancel previous jump if D is a hole.
      + "AND H J\n" // J = J and H: cancel previous jump is H is a hole.
      + "NOT A T\n"// T = !A: jump is A is a hole.
      + "OR T J\n"// J = J or T: always jump if A is a hole.
      + "RUN\n");
  }

  private long calculate(final PuzzleContext pc, final String program) {
    final IntCode state = icf.make(pc);
    final IntCodeIoQueue input = state.getInput();
    program.codePoints().forEach(input::add);

    state.exec();
    final long[] output = state.getOutput().removeAll();
    return output[output.length - 1];
  }

}
