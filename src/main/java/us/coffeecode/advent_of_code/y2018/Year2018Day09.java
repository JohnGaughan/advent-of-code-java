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
package us.coffeecode.advent_of_code.y2018;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2018, day = 9, title = "Marble Mania")
@Component
public final class Year2018Day09 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return calculate(pc, 1);
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return calculate(pc, 100);
  }

  private long calculate(final PuzzleContext pc, final int multiplier) {
    final Input input = getInput(pc);

    // The initial state is after the first turn, placing marble 0.
    int currentPlayer = 0;
    final int marbles = multiplier * (input.maxMarble + 1);
    final long[] scores = new long[input.players];

    Marble current = new Marble(0);
    current.next = current.previous = current;

    // i is the next marble to place.
    for (int i = 1; i < marbles; ++i) {
      ++currentPlayer;
      if (currentPlayer == scores.length) {
        currentPlayer = 0;
      }

      if (i % 23 == 0) {
        current = current.previous.previous.previous.previous.previous.previous;
        scores[currentPlayer] += i + current.previous.value;
        current.previous.previous.next = current;
        current.previous = current.previous.previous;
      }
      else {
        current = current.next;
        Marble inserted = new Marble(i);
        inserted.next = current.next;
        inserted.previous = current;
        inserted.previous.next = inserted;
        inserted.next.previous = inserted;
        current = inserted;
      }
    }

    return Arrays.stream(scores)
                 .max()
                 .getAsLong();
  }

  /** Get the input data for this solution. */
  private Input getInput(final PuzzleContext pc) {
    final int[] i = il.fileAsIntsFromDigitGroups(pc);
    return new Input(i[0], i[1]);
  }

  private static record Input(int players, int maxMarble) {}

  private static final class Marble {

    final int value;

    Marble next;

    Marble previous;

    Marble(final int _value) {
      value = _value;
    }

  }

}
