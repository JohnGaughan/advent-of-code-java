/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2022  John Gaughan
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
package us.coffeecode.advent_of_code.y2022;

import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.DigitConverter;

@AdventOfCodeSolution(year = 2022, day = 10)
@Component
public class Year2022Day10 {

  private static final int WIDTH = 40;

  @Autowired
  private InputLoader il;

  @Autowired
  private DigitConverter dc;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    int x = 1;
    int cycle = 1;
    long answer = 0;
    for (final String line : il.lines(pc)) {
      if ("noop".equals(line)) {
        answer += signalStrength(x, cycle);
        ++cycle;
      }
      else {
        int delta = Integer.parseInt(line.substring(5));
        answer += signalStrength(x, cycle);
        ++cycle;
        answer += signalStrength(x, cycle);
        ++cycle;
        x += delta;
      }
    }
    return answer;
  }

  private long signalStrength(final int x, final int cycle) {
    return ((cycle + 20) % WIDTH == 0) ? x * cycle : 0;
  }

  @Solver(part = 2)
  public String calculatePart2(final PuzzleContext pc) {
    int x = 1;
    int cycle = 0;
    boolean[][] display = new boolean[6][WIDTH];
    for (final String line : il.lines(pc)) {
      if ("noop".equals(line)) {
        final int xpos = cycle % WIDTH;
        if (Math.abs(xpos - x) < 2) {
          final int ypos = cycle / WIDTH;
          display[ypos][xpos] = true;
        }
        ++cycle;
      }
      else {
        int delta = Integer.parseInt(line.substring(5));
        for (int i = 0; i < 2; ++i) {
          final int xpos = cycle % WIDTH;
          if (Math.abs(xpos - x) < 2) {
            final int ypos = cycle / WIDTH;
            display[ypos][xpos] = true;
          }
          ++cycle;
        }
        x += delta;
      }
    }
    return toString(display);
  }

  private String toString(final boolean[][] display) {
    return IntStream.iterate(0, i -> i < WIDTH, i -> i + 5)
                    .map(i -> dc.toCodePoint(display, i, 4))
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
  }

}
