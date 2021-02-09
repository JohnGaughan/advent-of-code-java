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

/**
 * <p>
 * <a href="https://adventofcode.com/2015/day/11">Year 2015, day 11</a>. This problem asks us to increment a password
 * until it meets some validation criteria. Part one wants the next valid password, part two wants the next valid
 * password after that.
 * </p>
 * <p>
 * There are two important points here. First is that there are quite a few increments and validations to perform, and
 * that both Strings and chars are inefficient when used this extensively. Using integer arrays might not be as
 * object-oriented, but it is an order of magnitude faster: roughly 1/30th the time on my hardware. Strings require a
 * ton of construction and deconstruction, allocating many, many extra arrays behind the scenes. Character arrays help
 * somewhat, but not a lot: the incrementing logic has casts in there, and characters are not efficient when doing a lot
 * of casting. Converting to an integer array once at the start and back into a string at the end adds a tremendous
 * amount of speed and efficiency in the middle.
 * </p>
 * <p>
 * Second, the password can be thought of as one big integer that is formatted as base-26 for human consumption.
 * Combining the array elements into a long, incrementing, and converting back is fairly efficient because it is mostly
 * integer arithmetic. This is in contrast to carrying values manually between array elements, which adds control
 * structures that require a lot more CPU and possibly cache misses. This is worse than the division and modulo
 * functions.
 * </p>
 * <p>
 * Even without optimizations this program ran in slightly under a second, but with them, it runs in a few tens of
 * milliseconds.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2015Day11 {

  public String calculatePart1() {
    final int[] password = getNextPassword(getInput());
    return valueOf(password);
  }

  public String calculatePart2() {
    int[] password = getNextPassword(getInput());
    password = getNextPassword(password);
    return valueOf(password);
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
      if (password[i] == 'i' || password[i] == 'o' || password[i] == 'l') {
        return false;
      }
      if (!hasIncreasingSubstring && i > 1) {
        hasIncreasingSubstring = password[i - 2] == password[i - 1] - 1 && password[i - 1] == password[i] - 1;
      }
      if (!hasTwoRepeats && i > 0 && password[i - 1] == password[i] && password[i] != repeat) {
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

  /** Get the input data for this solution. */
  private int[] getInput() {
    return valueOf("vzbxkghb");
  }

  /**
   * Convert code points to a string, because String doesn't have a way to do this despite operating mostly on code
   * points.
   */
  private static String valueOf(final int[] codePoints) {
    final StringBuilder str = new StringBuilder(codePoints.length);
    for (final int codePoint : codePoints) {
      str.appendCodePoint(codePoint);
    }
    return str.toString();
  }

  /**
   * Convert a string to code points, because String doesn't have a way to do this despite operating mostly on code
   * points.
   */
  private static int[] valueOf(final String str) {
    final int[] value = new int[str.length()];
    for (int i = 0; i < str.length(); ++i) {
      value[i] = str.codePointAt(i);
    }
    return value;
  }

}
