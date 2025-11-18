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
package us.coffeecode.advent_of_code.y2015;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2015, day = 11)
@Component
public final class Year2015Day11 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public String calculatePart1(final PuzzleContext pc) {
    final int[] password = getNextPassword(il.fileAsCodePoints(pc));
    return new String(password, 0, password.length);
  }

  @Solver(part = 2)
  public String calculatePart2(final PuzzleContext pc) {
    int[] password = getNextPassword(il.fileAsCodePoints(pc));
    password = getNextPassword(password);
    return new String(password, 0, password.length);
  }

  /** Get the next valid password. */
  private int[] getNextPassword(final int[] input) {
    int[] password = input;
    do {
      password = increment(password);
    } while (!isValid(password));
    return password;
  }

  /** Determine if the given password is valid. */
  private boolean isValid(final int[] password) {
    // 1. Must have an increasing substring, e.g. abc or xyz.
    // 2. Must not contain i, o, or l.
    // 3. Must contain 2+ different, non-overlapping pairs of characters like aa or ww.
    boolean hasIncreasingSubstring = false;
    boolean hasTwoRepeats = false;
    int repeat = 0;
    for (int i = 0; i < password.length; ++i) {
      if ((password[i] == 'i') || (password[i] == 'o') || (password[i] == 'l')) {
        return false;
      }
      if (!hasIncreasingSubstring && i > 1) {
        hasIncreasingSubstring = (password[i - 2] == (password[i - 1] - 1)) && (password[i - 1] == (password[i] - 1));
      }
      if (!hasTwoRepeats && (i > 0) && (password[i - 1] == password[i]) && (password[i] != repeat)) {
        if (repeat == 0) {
          repeat = password[i];
        }
        else {
          hasTwoRepeats = true;
        }
      }
    }

    return hasIncreasingSubstring && hasTwoRepeats;
  }

  private static final int A = 'a';

  /** Increment the password, returning the next one. */
  private int[] increment(final int[] password) {
    // This is basically an eight digit, radix-26 number. Encode it into a long: int will overflow.
    long encoded = 0;
    for (final int element : password) {
      encoded *= 26;
      encoded += element - A;
    }
    ++encoded;

    // Now parse it back into a string.
    final int[] str = new int[password.length];
    for (int i = 0; i < password.length; ++i) {
      str[str.length - i - 1] = (int) (encoded % 26 + A);
      encoded /= 26;
    }
    return str;
  }

}
