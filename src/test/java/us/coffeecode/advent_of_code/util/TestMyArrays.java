/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2023  John Gaughan
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

import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import us.coffeecode.advent_of_code.AbstractTests;

public class TestMyArrays
extends AbstractTests {

  // reverse(long)

  @Test
  public void testReverse_longs_null() {
    Assertions.assertThrows(NullPointerException.class, () -> MyArrays.reverse(null));
  }

  @Test
  public void testReverse_longs_zero() {
    final long[] expected = new long[0];
    final long[] actual = MyArrays.reverse(new long[0]);
    Assertions.assertArrayEquals(expected, actual);
  }

  @Test
  public void testReverse_longs_length_1() {
    final long[] expected = new long[] { 12 };
    final long[] actual = MyArrays.reverse(new long[] { 12 });
    Assertions.assertArrayEquals(expected, actual);
  }

  @Test
  public void testReverse_longs_length_2() {
    final long[] expected = new long[] { -2, 7 };
    final long[] actual = MyArrays.reverse(new long[] { 7, -2 });
    Assertions.assertArrayEquals(expected, actual);
  }

  @Test
  public void testReverse_longs_length_3() {
    final long[] expected = new long[] { -7, 0, 394 };
    final long[] actual = MyArrays.reverse(new long[] { 394, 0, -7 });
    Assertions.assertArrayEquals(expected, actual);
  }

  @Test
  public void testReverse_longs_length_4() {
    final long[] expected = new long[] { Long.MIN_VALUE, 69, 0, -999 };
    final long[] actual = MyArrays.reverse(new long[] { -999, 0, 69, Long.MIN_VALUE });
    Assertions.assertArrayEquals(expected, actual);
  }

  // copy(int[][])

  @Test
  public void testCopy_int2d_null_1() {
    Assertions.assertThrows(NullPointerException.class, () -> MyArrays.copy(null));
  }

  @Test
  public void testCopy_int2d_null_2() {
    Assertions.assertThrows(NullPointerException.class, () -> MyArrays.copy(new int[][] { null }));
  }

  @Test
  public void testCopy_int2d_empty() {
    final int[][] expected = new int[][] {};
    final int[][] actual = MyArrays.copy(expected);
    Assertions.assertTrue(expected != actual);
    Assertions.assertTrue(Arrays.deepEquals(expected, actual));
  }

  @Test
  public void testCopy_int2d_1() {
    final int[][] expected = new int[][] { { 0, 1 }, { 2, 3 } };
    final int[][] actual = MyArrays.copy(expected);
    Assertions.assertTrue(expected != actual);
    Assertions.assertTrue(Arrays.deepEquals(expected, actual));
  }

  @Test
  public void testCopy_int2d_2() {
    final int[][] expected = new int[][] { { 0 }, { 1, 2, 3 } };
    final int[][] actual = MyArrays.copy(expected);
    Assertions.assertTrue(expected != actual);
    Assertions.assertTrue(Arrays.deepEquals(expected, actual));
  }

  @Test
  public void testCopy_int2d_3() {
    final int[][] expected = new int[][] { { 0, 1, 2 }, { 3 } };
    final int[][] actual = MyArrays.copy(expected);
    Assertions.assertTrue(expected != actual);
    Assertions.assertTrue(Arrays.deepEquals(expected, actual));
  }

  @Test
  public void testCopy_int2d_4() {
    final int[][] expected = new int[][] { { 1, 2, 3 }, {} };
    final int[][] actual = MyArrays.copy(expected);
    Assertions.assertTrue(expected != actual);
    Assertions.assertTrue(Arrays.deepEquals(expected, actual));
  }

}
