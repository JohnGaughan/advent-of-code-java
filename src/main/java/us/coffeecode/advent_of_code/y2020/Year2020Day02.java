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
package us.coffeecode.advent_of_code.y2020;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2020, day = 2, title = "Password Philosophy")
@Component
public final class Year2020Day02 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return il.linesAsObjects(pc, Input::make)
             .stream()
             .filter(t -> {
               final long count = t.password.codePoints()
                                            .filter(c -> c == t.ch)
                                            .count();
               return (t.n1 <= count) && (count <= t.n2);
             })
             .count();
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return il.linesAsObjects(pc, Input::make)
             .stream()
             .filter(t -> (t.password.charAt(t.n1 - 1) == t.ch) ^ (t.password.charAt(t.n2 - 1) == t.ch))
             .count();
  }

  private static record Input(int n1, int n2, char ch, String password) {

    /** This pattern separates the policy and password on an input line. */
    private static final Pattern SPLIT = Pattern.compile(": ");

    static Input make(final String line) {
      // Separate the policy from the password
      final String[] pieces = SPLIT.split(line);

      // Get the character to test and the positions to look at.
      final int split1 = pieces[0].indexOf('-');
      final int split2 = pieces[0].indexOf(' ', split1);
      return new Input(Integer.parseInt(pieces[0].substring(0, split1)),
        Integer.parseInt(pieces[0].substring(split1 + 1, split2)), pieces[0].charAt(split2 + 1), pieces[1]);
    }
  }

}
