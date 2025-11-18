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

@AdventOfCodeSolution(year = 2019, day = 2)
@Component
public final class Year2019Day02 {

  @Autowired
  private IntCodeFactory icf;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final IntCode state = icf.make(pc);
    state.getMemory()
         .set(1, 12);
    state.getMemory()
         .set(2, 2);
    state.exec();
    return state.getMemory()
                .getFirst();
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final IntCode original = icf.make(pc);
    for (int x = 0; x < 99; ++x) {
      for (int y = 0; y < 99; ++y) {
        final IntCode state = icf.make(original);
        state.getMemory()
             .set(1, x);
        state.getMemory()
             .set(2, y);
        state.exec();
        if (state.getMemory()
                 .getFirst() == 19_690_720) {
          return (100 * x) + y;
        }
      }
    }
    return 0;
  }

}
