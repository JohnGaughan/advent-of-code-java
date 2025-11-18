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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2022, day = 25)
@Component
public class Year2022Day25 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public String calculatePart1(final PuzzleContext pc) {
    return il.lines(pc)
             .stream()
             .reduce("", this::add);
  }

  private String add(final String a, final String b) {
    final StringBuilder result = new StringBuilder(32);
    char carry = ZERO;
    for (int i = 0; i < Math.max(a.length(), b.length() + 1); ++i) {
      final int a0 = a.length() - i - 1;
      final int b0 = b.length() - i - 1;
      // There is still some of string A to consume.
      if (a0 >= 0) {
        // There is also some of string B.
        if (b0 >= 0) {
          final char[] added = add(a.charAt(a0), b.charAt(b0), carry);
          carry = added[0];
          result.insert(0, added[1]);
        }
        // No string B left, and no carry digit.
        else if (carry == ZERO) {
          result.insert(0, a.charAt(a0));
        }
        // No string B left, but there is a carry digit to add to A.
        else {
          final char[] added = add(a.charAt(a0), ZERO, carry);
          carry = added[0];
          result.insert(0, added[1]);
        }
      }
      // There is no more of string A, but there is some of string B.
      else if (b0 >= 0) {
        // Nothing to carry, copy from B.
        if (carry == ZERO) {
          result.insert(0, b.charAt(b0));
        }
        // Add the carry digit to B.
        else {
          final char[] added = add(ZERO, b.charAt(b0), carry);
          carry = added[0];
          result.insert(0, added[1]);
        }
      }
      // Nothing left of A or B, but there is a carry digit.
      else if (carry != ZERO) {
        result.insert(0, carry);
      }
    }
    return result.toString();
  }

  private char[] add(final char a, final char b, final char c) {
    int value = fromSnafu(a) + fromSnafu(b) + fromSnafu(c);
    int carry = 0;
    if (value > 2) {
      carry = 1;
      value -= 5;
    }
    else if (value < -2) {
      carry = -1;
      value += 5;
    }
    return new char[] { toSnafu(carry), toSnafu(value) };
  }

  private char toSnafu(final int i) {
    return SNAFU.charAt(i + 2);
  }

  private int fromSnafu(final char ch) {
    return SNAFU.indexOf(ch) - 2;
  }

  private static final char ZERO = '0';

  private static final String SNAFU = "=-012";
}
