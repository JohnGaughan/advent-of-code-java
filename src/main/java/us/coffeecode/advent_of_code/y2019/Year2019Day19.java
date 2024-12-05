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

@AdventOfCodeSolution(year = 2019, day = 19, title = "Tractor Beam")
@Component
public final class Year2019Day19 {

  @Autowired
  private IntCodeFactory icf;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final long SIZE = 50;
    final IntCode state = icf.make(pc);
    long count = 0;
    // Check every pixel in a 50x50 square.
    for (int y = 0; y < SIZE; ++y) {
      for (int x = 0; x < SIZE; ++x) {
        if (check(state, x, y)) {
          ++count;
        }
      }
    }
    return count;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    // Off by one because the bounds are closed, not open.
    final long SIZE = 99;
    final IntCode state = icf.make(pc);
    // Give a head start. The cone is narrow, and the first few rows might not even have any pixels in the cone.
    long x = SIZE << 2;
    long y = SIZE << 3;
    // Find the left+bottom edge to start.
    while (!check(state, x, y)) {
      ++x;
    }
    // While either the lower-left or the top-right corner is out of the cone...
    while (!check(state, x, y) || !check(state, x + SIZE, y - SIZE)) {
      // Move down one row, then right one column if needed to move along the left+bottom edge of the cone.
      ++y;
      if (!check(state, x, y)) {
        ++x;
      }
    }
    return (10_000 * x) + (y - SIZE);
  }

  private boolean check(final IntCode state, final long x, final long y) {
    // This program provides a single output, then it no longer works. Make a fresh copy each time.
    final IntCode copy = icf.make(state);
    copy.getInput()
        .add(new long[] { x, y });
    copy.exec();
    final long answer = copy.getOutput()
                            .remove();
    return answer == 1;
  }

}
