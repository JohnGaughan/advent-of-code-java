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

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2017, day = 17, title = "Spinlock")
@Component
public final class Year2017Day17 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final int bound = 2_018;
    final int input = il.fileAsInt(pc);
    final List<Long> spinlock = new ArrayList<>(bound);
    spinlock.add(Long.valueOf(0));
    int index = 0;
    for (int i = 1; i < bound; ++i) {
      index = (index + input) % spinlock.size() + 1;
      spinlock.add(index, Long.valueOf(i));
    }
    ++index;
    if (index == spinlock.size()) {
      index = 0;
    }
    return spinlock.get(index).longValue();
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final long input = il.fileAsInt(pc);
    long index = 0;
    long answer = 0;
    for (long i = 1; i <= 50_000_001; ++i) {
      index = (index + input) % i;
      if (index == 0) {
        answer = i;
      }
      ++index;
    }
    return answer;
  }

}
