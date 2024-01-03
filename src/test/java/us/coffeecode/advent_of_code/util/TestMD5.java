/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2021  John Gaughan
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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import us.coffeecode.advent_of_code.AbstractTests;

public class TestMD5
extends AbstractTests {

  private MD5 md5;

  @BeforeEach
  public void beforeAll() {
    md5 = context.getBean(MD5.class);
  }

  @Test
  public void testMd5_1() {
    final byte[] expected = new byte[] { 0, 0, 1, 85, -8, 16, 93, -1, 127, 86, -18, 16, -6, -101, -102, -67 };
    final byte[] actual = md5.md5("abc3231929");
    Assertions.assertArrayEquals(expected, actual);
  }

  @Test
  public void testMd5_2() {
    final byte[] expected = new byte[] { 0, 0, 8, -8, 44, 91, 57, 36, -95, -20, -66, -65, 96, 52, 78, 0 };
    final byte[] actual = md5.md5("abc5017308");
    Assertions.assertArrayEquals(expected, actual);
  }

  @Test
  public void testMd5_3() {
    final byte[] expected = new byte[] { 0, 0, 15, -102, 44, 48, -104, 117, -32, 92, 90, 93, 9, -15, -72, -60 };
    final byte[] actual = md5.md5("abc5278568");
    Assertions.assertArrayEquals(expected, actual);
  }

}
