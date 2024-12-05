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
package us.coffeecode.advent_of_code.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Component;

import com.google.common.base.Charsets;

/**
 * This component encapsulates logic to convert a string to MD5 bytes, including exception handling and thread-safety.
 * Several early problems required using the MD5 algorithm, which has some setup required in Java and is not
 * thread-safe. This class handles those issues and provides an easy code interface.
 */
@Component
public class MD5 {

  public byte[] md5(final String input) {
    return MD5.get()
              .digest(input.getBytes(Charsets.US_ASCII));
  }

  /** MD5 message digest. */
  private static final ThreadLocal<MessageDigest> MD5 = new ThreadLocal<>() {

    protected MessageDigest initialValue() {
      try {
        return MessageDigest.getInstance("MD5");
      }
      catch (final NoSuchAlgorithmException ex) {
        throw new RuntimeException(ex);
      }
    }
  };

}
