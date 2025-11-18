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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.MD5;

@AdventOfCodeSolution(year = 2016, day = 5)
@Component
public final class Year2016Day05 {

  @Autowired
  private MD5 md5;

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public String calculatePart1(final PuzzleContext pc) {
    final String salt = il.fileAsString(pc);
    final StringBuilder password = new StringBuilder(8);
    int index = 0;
    while (password.length() < 8) {
      final byte[] cipher = md5.md5(salt + index);
      // Cipher must start with 0x00000, or the first 20 bits must be zero. This is the first two and a half bytes.
      if ((cipher[0] == 0) && (cipher[1] == 0) && (cipher[2] >= 0) && (cipher[2] < 16)) {
        // Append the second half of the third byte.
        password.append(Integer.toHexString(cipher[2] & 0xF));
      }
      ++index;
    }
    return password.toString();
  }

  @Solver(part = 2)
  public String calculatePart2(final PuzzleContext pc) {
    final String salt = il.fileAsString(pc);
    final StringBuilder password = new StringBuilder("________");
    int index = 0;
    while (password.indexOf("_") >= 0) {
      String plaintext = salt + index;
      final byte[] cipher = md5.md5(plaintext);
      // Cipher must start with 0x00000, or the first 20 bits must be zero. This is the first two and a half bytes.
      if ((cipher[0] == 0) && (cipher[1] == 0) && (cipher[2] >= 0) && (cipher[2] < 16)) {
        // Second half of the third byte is the position to update
        final int position = cipher[2] & 0xF;
        if (position >= password.length() || password.charAt(position) != '_') {
          ++index;
          continue;
        }
        // First half of the fourth byte is the character to set.
        final int value = cipher[3] & 0xF0;
        final char ch = Integer.toHexString(value >> 4)
                               .charAt(0);
        password.setCharAt(position, ch);
      }
      ++index;
    }
    return password.toString();
  }

}
