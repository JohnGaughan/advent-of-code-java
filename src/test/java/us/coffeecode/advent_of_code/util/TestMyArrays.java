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

  //
  // int(long)
  //

  @Test
  public void testReverse_ints_null() {
    Assertions.assertThrows(NullPointerException.class, () -> MyArrays.reverse((int[]) null));
  }

  @Test
  public void testReverse_ints_zero() {
    final int[] expected = new int[0];
    final int[] actual = MyArrays.reverse(new int[0]);
    Assertions.assertArrayEquals(expected, actual);
  }

  @Test
  public void testReverse_ints_length_1() {
    final int[] expected = new int[] { 12 };
    final int[] actual = MyArrays.reverse(new int[] { 12 });
    Assertions.assertArrayEquals(expected, actual);
  }

  @Test
  public void testReverse_ints_length_2() {
    final int[] expected = new int[] { -2, 7 };
    final int[] actual = MyArrays.reverse(new int[] { 7, -2 });
    Assertions.assertArrayEquals(expected, actual);
  }

  @Test
  public void testReverse_ints_length_3() {
    final int[] expected = new int[] { -7, 0, 394 };
    final int[] actual = MyArrays.reverse(new int[] { 394, 0, -7 });
    Assertions.assertArrayEquals(expected, actual);
  }

  @Test
  public void testReverse_ints_length_4() {
    final int[] expected = new int[] { Integer.MIN_VALUE, 69, 0, -999 };
    final int[] actual = MyArrays.reverse(new int[] { -999, 0, 69, Integer.MIN_VALUE });
    Assertions.assertArrayEquals(expected, actual);
  }

  //
  // reverse(long)
  //

  @Test
  public void testReverse_longs_null() {
    Assertions.assertThrows(NullPointerException.class, () -> MyArrays.reverse((long[]) null));
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

  //
  // void reverseInPlace(final int[] array, final int start, final int end)
  //

  @Test
  public void test_reverseInPlace_int_1() {
    final int[] expected = new int[] { 0, 1, 2, 3, 4, 5 };
    final int[] actual = new int[] { 0, 1, 2, 5, 4, 3 };
    MyArrays.reverseInPlace(actual, 3, 6);
    Assertions.assertArrayEquals(expected, actual);
  }

  @Test
  public void test_reverseInPlace_int_2() {
    final int[] expected = new int[] { 0, 1, 2, 3, 4, 5 };
    final int[] actual = new int[] { 0, 1, 3, 2, 4, 5 };
    MyArrays.reverseInPlace(actual, 2, 4);
    Assertions.assertArrayEquals(expected, actual);
  }

  @Test
  public void test_reverseInPlace_int_3() {
    final int[] expected = new int[] { 0, 1, 2, 3, 4, 5 };
    final int[] actual = new int[] { 0, 1, 2, 3, 4, 5 };
    MyArrays.reverseInPlace(actual, 0, 0);
    Assertions.assertArrayEquals(expected, actual);
  }

  //
  // void reverseInPlace(final long[] array, final int start, final int end)
  //

  @Test
  public void test_reverseInPlace_long_1() {
    final long[] expected = new long[] { 0, 1, 2, 3, 4, 5 };
    final long[] actual = new long[] { 0, 1, 2, 5, 4, 3 };
    MyArrays.reverseInPlace(actual, 3, 6);
    Assertions.assertArrayEquals(expected, actual);
  }

  @Test
  public void test_reverseInPlace_long_2() {
    final long[] expected = new long[] { 0, 1, 2, 3, 4, 5 };
    final long[] actual = new long[] { 0, 1, 3, 2, 4, 5 };
    MyArrays.reverseInPlace(actual, 2, 4);
    Assertions.assertArrayEquals(expected, actual);
  }

  @Test
  public void test_reverseInPlace_long_3() {
    final long[] expected = new long[] { 0, 1, 2, 3, 4, 5 };
    final long[] actual = new long[] { 0, 1, 2, 3, 4, 5 };
    MyArrays.reverseInPlace(actual, 0, 0);
    Assertions.assertArrayEquals(expected, actual);
  }

  //
  // deepCopyOf(int[][])
  //

  @Test
  public void test_deepCopyOf_int2d_null_1() {
    Assertions.assertThrows(NullPointerException.class, () -> MyArrays.deepCopyOf((int[][]) null));
  }

  @Test
  public void test_deepCopyOf_int2d_null_2() {
    Assertions.assertThrows(NullPointerException.class, () -> MyArrays.deepCopyOf(new int[][] { null }));
  }

  @Test
  public void test_deepCopyOf_int2d_empty() {
    final int[][] expected = new int[][] {};
    final int[][] actual = MyArrays.deepCopyOf(expected);
    Assertions.assertTrue(expected != actual);
    Assertions.assertTrue(Arrays.deepEquals(expected, actual));
  }

  @Test
  public void test_deepCopyOf_int2d_1() {
    final int[][] expected = new int[][] { { 0, 1 }, { 2, 3 } };
    final int[][] actual = MyArrays.deepCopyOf(expected);
    Assertions.assertTrue(expected != actual);
    Assertions.assertTrue(Arrays.deepEquals(expected, actual));
  }

  @Test
  public void test_deepCopyOf_int2d_2() {
    final int[][] expected = new int[][] { { 0 }, { 1, 2, 3 } };
    final int[][] actual = MyArrays.deepCopyOf(expected);
    Assertions.assertTrue(expected != actual);
    Assertions.assertTrue(Arrays.deepEquals(expected, actual));
  }

  @Test
  public void test_deepCopyOf_int2d_3() {
    final int[][] expected = new int[][] { { 0, 1, 2 }, { 3 } };
    final int[][] actual = MyArrays.deepCopyOf(expected);
    Assertions.assertTrue(expected != actual);
    Assertions.assertTrue(Arrays.deepEquals(expected, actual));
  }

  @Test
  public void test_deepCopyOf_int2d_4() {
    final int[][] expected = new int[][] { { 1, 2, 3 }, {} };
    final int[][] actual = MyArrays.deepCopyOf(expected);
    Assertions.assertTrue(expected != actual);
    Assertions.assertTrue(Arrays.deepEquals(expected, actual));
  }

  //
  // deepCopyOf(long[][])
  //

  @Test
  public void test_deepCopyOf_long2d_null_1() {
    Assertions.assertThrows(NullPointerException.class, () -> MyArrays.deepCopyOf((long[][]) null));
  }

  @Test
  public void test_deepCopyOf_long2d_null_2() {
    Assertions.assertThrows(NullPointerException.class, () -> MyArrays.deepCopyOf(new long[][] { null }));
  }

  @Test
  public void test_deepCopyOf_long2d_empty() {
    final long[][] expected = new long[][] {};
    final long[][] actual = MyArrays.deepCopyOf(expected);
    Assertions.assertTrue(expected != actual);
    Assertions.assertTrue(Arrays.deepEquals(expected, actual));
  }

  @Test
  public void test_deepCopyOf_long2d_1() {
    final long[][] expected = new long[][] { { 0, 1 }, { 2, 3 } };
    final long[][] actual = MyArrays.deepCopyOf(expected);
    Assertions.assertTrue(expected != actual);
    Assertions.assertTrue(Arrays.deepEquals(expected, actual));
  }

  @Test
  public void test_deepCopyOf_long2d_2() {
    final long[][] expected = new long[][] { { 0 }, { 1, 2, 3 } };
    final long[][] actual = MyArrays.deepCopyOf(expected);
    Assertions.assertTrue(expected != actual);
    Assertions.assertTrue(Arrays.deepEquals(expected, actual));
  }

  @Test
  public void test_deepCopyOf_long2d_3() {
    final long[][] expected = new long[][] { { 0, 1, 2 }, { 3 } };
    final long[][] actual = MyArrays.deepCopyOf(expected);
    Assertions.assertTrue(expected != actual);
    Assertions.assertTrue(Arrays.deepEquals(expected, actual));
  }

  @Test
  public void test_deepCopyOf_long2d_4() {
    final long[][] expected = new long[][] { { 1, 2, 3 }, {} };
    final long[][] actual = MyArrays.deepCopyOf(expected);
    Assertions.assertTrue(expected != actual);
    Assertions.assertTrue(Arrays.deepEquals(expected, actual));
  }

  //
  // copyOmitting(int[])
  //

  @Test
  public void test_copyOmitting_int_0() {
    final int[] expected = new int[] { 2, 3, 4 };
    final int[] actual = MyArrays.copyOmitting(new int[] { 1, 2, 3, 4 }, 0);
    Assertions.assertTrue(Arrays.equals(expected, actual));
  }

  @Test
  public void test_copyOmitting_int_1() {
    final int[] expected = new int[] { 1, 3, 4 };
    final int[] actual = MyArrays.copyOmitting(new int[] { 1, 2, 3, 4 }, 1);
    Assertions.assertTrue(Arrays.equals(expected, actual));
  }

  @Test
  public void test_copyOmitting_int_2() {
    final int[] expected = new int[] { 1, 2, 4 };
    final int[] actual = MyArrays.copyOmitting(new int[] { 1, 2, 3, 4 }, 2);
    Assertions.assertTrue(Arrays.equals(expected, actual));
  }

  @Test
  public void test_copyOmitting_int_3() {
    final int[] expected = new int[] { 1, 2, 3 };
    final int[] actual = MyArrays.copyOmitting(new int[] { 1, 2, 3, 4 }, 3);
    Assertions.assertTrue(Arrays.equals(expected, actual));
  }

  @Test()
  public void test_copyOmitting_int_Null() {
    Assertions.assertThrows(RuntimeException.class, () -> MyArrays.copyOmitting((int[]) null, 0));
  }

  @Test()
  public void test_copyOmitting_int_ZeroLength() {
    Assertions.assertThrows(RuntimeException.class, () -> MyArrays.copyOmitting(new int[0], 0));
  }

  @Test()
  public void test_copyOmitting_int_NegativeIndex() {
    Assertions.assertThrows(RuntimeException.class, () -> MyArrays.copyOmitting(new int[] { 1, 2, 3, 4 }, -1));
  }

  @Test()
  public void test_copyOmitting_int_IndexTooHigh() {
    final int[] array = new int[] { 1, 2, 3, 4 };
    Assertions.assertThrows(RuntimeException.class, () -> MyArrays.copyOmitting(array, array.length));
  }
  //
  // copyOmitting(long[])
  //

  @Test
  public void test_copyOmitting_long_0() {
    final long[] expected = new long[] { 2, 3, 4 };
    final long[] actual = MyArrays.copyOmitting(new long[] { 1, 2, 3, 4 }, 0);
    Assertions.assertTrue(Arrays.equals(expected, actual));
  }

  @Test
  public void test_copyOmitting_long_1() {
    final long[] expected = new long[] { 1, 3, 4 };
    final long[] actual = MyArrays.copyOmitting(new long[] { 1, 2, 3, 4 }, 1);
    Assertions.assertTrue(Arrays.equals(expected, actual));
  }

  @Test
  public void test_copyOmitting_long_2() {
    final long[] expected = new long[] { 1, 2, 4 };
    final long[] actual = MyArrays.copyOmitting(new long[] { 1, 2, 3, 4 }, 2);
    Assertions.assertTrue(Arrays.equals(expected, actual));
  }

  @Test
  public void test_copyOmitting_long_3() {
    final long[] expected = new long[] { 1, 2, 3 };
    final long[] actual = MyArrays.copyOmitting(new long[] { 1, 2, 3, 4 }, 3);
    Assertions.assertTrue(Arrays.equals(expected, actual));
  }

  @Test()
  public void test_copyOmitting_long_Null() {
    Assertions.assertThrows(RuntimeException.class, () -> MyArrays.copyOmitting((long[]) null, 0));
  }

  @Test()
  public void test_copyOmitting_long_ZeroLength() {
    Assertions.assertThrows(RuntimeException.class, () -> MyArrays.copyOmitting(new long[0], 0));
  }

  @Test()
  public void test_copyOmitting_long_NegativeIndex() {
    Assertions.assertThrows(RuntimeException.class, () -> MyArrays.copyOmitting(new long[] { 1, 2, 3, 4 }, -1));
  }

  @Test()
  public void test_copyOmitting_long_IndexTooHigh() {
    final long[] array = new long[] { 1, 2, 3, 4 };
    Assertions.assertThrows(RuntimeException.class, () -> MyArrays.copyOmitting(array, array.length));
  }
}
