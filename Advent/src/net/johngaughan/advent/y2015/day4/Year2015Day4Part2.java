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
package net.johngaughan.advent.y2015.day4;

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import net.johngaughan.advent.AdventProblem;

/**
 * <p>
 * Year 2015, day 4, part 2.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@johngaughan.net&gt;
 */
public final class Year2015Day4Part2
implements AdventProblem {

  private static final String INPUT = "ckczppom";

  /** {@inheritDoc} */
  @Override
  public long calculate(final Path path) {
    try {
      final MessageDigest md = MessageDigest.getInstance("MD5");
      final Charset ASCII = Charset.forName("US-ASCII");
      for (long i = 0; i <= Long.MAX_VALUE; ++i) {
        final String plaintext = INPUT + i;
        final byte[] cipher = md.digest(plaintext.getBytes(ASCII));
        // Cipher must start with 0x000000, or the first 24 bits must be zero. This is the first three bytes.
        if ((cipher[0] == 0) && (cipher[1] == 0) && (cipher[2] == 0)) {
          return i;
        }
      }
    }
    catch (NoSuchAlgorithmException ex) {
      throw new RuntimeException(ex);
    }
    return Long.MIN_VALUE;
  }

}
