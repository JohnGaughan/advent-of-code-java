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

import java.util.SequencedCollection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.function.BiIntPredicate;

@AdventOfCodeSolution(year = 2016, day = 6, title = "Signals and Noise")
@Component
public final class Year2016Day06 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public String calculatePart1(final PuzzleContext pc) {
    return calculate(pc, (a, b) -> (a < b));
  }

  @Solver(part = 2)
  public String calculatePart2(final PuzzleContext pc) {
    return calculate(pc, (a, b) -> (a > b));
  }

  private String calculate(final PuzzleContext pc, final BiIntPredicate test) {
    final SequencedCollection<String> lines = il.lines(pc);
    int[][] frequency = new int[lines.getFirst().length()][26];
    for (final String line : lines) {
      for (int i = 0; i < line.length(); ++i) {
        frequency[i][line.codePointAt(i) - 'a']++;
      }
    }
    final StringBuilder message = new StringBuilder(frequency.length);
    for (int i = 0; i < frequency.length; ++i) {
      int candidate = -1;
      for (int ch = 0; ch < frequency[i].length; ++ch) {
        if ((frequency[i][ch] > 0) && (candidate < 0)) {
          candidate = ch;
        }
        else if ((frequency[i][ch] > 0) && test.test(frequency[i][candidate], frequency[i][ch])) {
          candidate = ch;
        }
      }
      message.appendCodePoint(candidate + 'a');
    }
    return message.toString();
  }

}
