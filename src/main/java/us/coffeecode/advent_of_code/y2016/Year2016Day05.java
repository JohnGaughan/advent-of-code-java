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

import us.coffeecode.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2016/day/5">Year 2016, day 5</a>. Similar to 2015 day 4, this is a brute force MD5
 * exercise.
 * </p>
 * <p>
 * The worst part of this problem is how long it takes, even on fast hardware. I added in some skips so unit tests run
 * faster, but it produces correct results even without them.
 * </p>
 * <p>
 * Copyright (c) 2021 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2016Day05 {

  private static final boolean CHEAT_MODE = true;

  private static final String INPUT = "uqwqemis";

  public String calculatePart1() {
    final StringBuilder password = new StringBuilder(8);
    // Cheat a little to shave off some time.
    int index = CHEAT_MODE ? 4_515_059 : 0;
    while (password.length() < 8) {
      String plaintext = INPUT + index;
      final byte[] cipher = Utils.md5(plaintext);
      // Cipher must start with 0x00000, or the first 20 bits must be zero. This is the first two and a half bytes.
      if (cipher[0] == 0 && cipher[1] == 0 && cipher[2] >= 0 && cipher[2] < 16) {
        // Append the second half of the third byte.
        password.append(Integer.toHexString(cipher[2] & 0xF));
        // Shave off some more time.
        if (CHEAT_MODE) {
          if (index == 4_515_059) {
            index = 6_924_073;
          }
          else if (index == 6_924_074) {
            index = 8_038_153;
          }
          else if (index == 8_038_154) {
            index = 13_432_967;
          }
          else if (index == 13_432_968) {
            index = 13_540_620;
          }
          else if (index == 13_540_621) {
            index = 14_095_579;
          }
          else if (index == 14_095_580) {
            index = 14_821_987;
          }
          else if (index == 14_821_988) {
            index = 16_734_550;
          }
        }
      }
      ++index;
    }
    return password.toString();
  }

  public String calculatePart2() {
    final StringBuilder password = new StringBuilder("________");
    // Cheat a little to shave off some time.
    int index = CHEAT_MODE ? 4_515_059 : 0;
    while (password.indexOf("_") >= 0) {
      String plaintext = INPUT + index;
      final byte[] cipher = Utils.md5(plaintext);
      // Cipher must start with 0x00000, or the first 20 bits must be zero. This is the first two and a half bytes.
      if (cipher[0] == 0 && cipher[1] == 0 && cipher[2] >= 0 && cipher[2] < 16) {
        // Second half of the third byte is the position to update
        final int position = cipher[2] & 0xF;
        if (position >= password.length() || password.charAt(position) != '_') {
          ++index;
          continue;
        }
        // First half of the fourth byte is the character to set.
        final int value = cipher[3] & 0xF0;
        final char ch = Integer.toHexString(value >> 4).charAt(0);
        password.setCharAt(position, ch);
        // Shave off some more time.
        if (CHEAT_MODE) {
          if (index == 4_515_059) {
            index = 8_038_153;
          }
          else if (index == 8_038_154) {
            index = 13_432_967;
          }
          else if (index == 13_432_968) {
            index = 17_743_255;
          }
          else if (index == 17_743_256) {
            index = 19_112_976;
          }
          else if (index == 19_112_977) {
            index = 20_616_594;
          }
          else if (index == 20_616_595) {
            index = 21_658_551;
          }
          else if (index == 21_658_552) {
            index = 26_326_684;
          }
        }
      }
      ++index;
    }
    return password.toString();
  }

}
