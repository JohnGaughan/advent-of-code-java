/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2024  John Gaughan
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
package us.coffeecode.advent_of_code.y2024;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2024, day = 13, title = "Claw Contraption")
@Component
public class Year2024Day13 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return calculate(pc);
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return calculate(pc);
  }

  private long calculate(final PuzzleContext pc) {
    return il.groupsAsObjects(pc, c -> parse(pc, c))
             .stream()
             .mapToLong(this::solve)
             .sum();
  }

  /**
   * Solve the system of equations represented by the coefficients, returning either zero for no solution, or the score
   * as defined in the problem if there is.
   */
  private long solve(final Coefficients c) {
    // Get the common divisor.
    final long d = (c.by * c.ax) - (c.bx * c.ay);
    // No solution: lines do not intersect.
    if (d == 0) {
      return 0;
    }
    // Get the coefficients for button presses for both A and B.
    final long a = (c.px * c.by - c.py * c.bx) / d;
    final long b = (c.py * c.ax - c.px * c.ay) / d;
    // Coefficients might not actually be integers and division could have truncated them, so plug them into the
    // original equations to check.
    if ((c.ax * a + c.bx * b == c.px) && (c.ay * a + c.by * b == c.py)) {
      return 3 * a + b;
    }
    // No integer solution.
    return 0;
  }

  private Coefficients parse(final PuzzleContext pc, final List<String> group) {
    final long[][] v = new long[3][2];
    for (int i = 0; i < v.length; ++i) {
      final Matcher m = MATCH.matcher(group.get(i));
      m.find();
      v[i][0] = Long.parseLong(m.group(1));
      v[i][1] = Long.parseLong(m.group(2));
    }
    // Part two's shift is stored in year2024/day13/parameters.properties.
    final long shift = pc.getLong("shift");
    return new Coefficients(v[0][0], v[0][1], v[1][0], v[1][1], shift + v[2][0], shift + v[2][1]);

  }

  /** Reluctantly skips non-numeric characters, greedily matches digits in a group, then repeats once. */
  private static final Pattern MATCH = Pattern.compile(".*?(\\d+).*?(\\d+)");

  /** Represents coefficients in a series of two linear equations with two variables. */
  private record Coefficients(long ax, long ay, long bx, long by, long px, long py) {}
}
