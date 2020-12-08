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
package net.johngaughan.advent_of_code.y2015;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * <p>
 * <a href="https://adventofcode.com/2015/day/4">Year 2015, day 4</a>. This problem asks us to generate MD5 hashes using
 * a constant prefix appended with a monotonically increasing suffix. The goal is to generate a hash that starts with a
 * number of zeros. Part one asks for five zeros, part two asks for six.
 * </p>
 * <p>
 * No way about it, brute force is the optimal solution here. MD5 is quite broken but not in a way that provides any
 * easy shortcuts for this problem. It was probably chosen because of the specific weakness that it is very fast: not a
 * desirable trait for cryptography, where you want to put a speed limit on password cracking. My Ryzen 3700X takes
 * around 900ms to solve part two: any visitors from 2030 or so want to laugh at how slow that was?
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@johngaughan.net&gt;
 */
public final class Year2015Day04 {

  /** The prefix for each plaintext to digest. */
  private static final String PLAINTEXT_PREFIX = "ckczppom";

  public long calculatePart1() {
    for (long i = 0; i <= Long.MAX_VALUE; ++i) {
      final String plaintext = PLAINTEXT_PREFIX + i;
      final byte[] cipher = MD5.digest(plaintext.getBytes(ASCII));
      // Cipher must start with 0x00000, or the first 20 bits must be zero. This is the first two and a half bytes.
      if ((cipher[0] == 0) && (cipher[1] == 0) && (cipher[2] >= 0) && (cipher[2] < 16)) {
        return i;
      }
    }
    return Long.MIN_VALUE;
  }

  public long calculatePart2() {
    for (long i = 0; i <= Long.MAX_VALUE; ++i) {
      final String plaintext = PLAINTEXT_PREFIX + i;
      final byte[] cipher = MD5.digest(plaintext.getBytes(ASCII));
      // Cipher must start with 0x000000, or the first 24 bits must be zero. This is the first three bytes.
      if ((cipher[0] == 0) && (cipher[1] == 0) && (cipher[2] == 0)) {
        return i;
      }
    }
    return Long.MIN_VALUE;
  }

  /** ASCII character set used to encode the plaintext into a byte array suitable for digesting. */
  private static final Charset ASCII = Charset.forName("US-ASCII");

  /** MD5 message digest. */
  private static final MessageDigest MD5;

  static {
    try {
      MD5 = MessageDigest.getInstance("MD5");
    }
    catch (final NoSuchAlgorithmException ex) {
      throw new ExceptionInInitializerError(ex);
    }
  }

}
