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
package us.coffeecode.advent_of_code.y2019;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import us.coffeecode.advent_of_code.AbstractTests;

public class TestDynamicLongArray
extends AbstractTests {

  @Test
  public void testArrayConstructor() {
    final long[] array = new long[] { 74, -1, 99, 0, 1_023 };
    final DynamicLongArray dla = new DynamicLongArray(array);
    for (int i = 0; i < array.length; ++i) {
      Assertions.assertEquals(array[i], dla.get(i));
    }
  }

  @Test
  public void testCopyConstructor() {
    final DynamicLongArray dla1 = new DynamicLongArray(new long[] { 1, 1, 1 });
    final DynamicLongArray dla2 = new DynamicLongArray(dla1);
    dla2.set(0, -999);
    Assertions.assertEquals(1, dla1.get(0));
  }

  @Test
  public void testGetFirst1() {
    final DynamicLongArray dla = new DynamicLongArray(new long[] {});
    Assertions.assertEquals(0, dla.getFirst());
  }

  @Test
  public void testGetFirst2() {
    final DynamicLongArray dla = new DynamicLongArray(new long[] { 0, 0 });
    Assertions.assertEquals(0, dla.getFirst());
  }

  @Test
  public void testGetFirst3() {
    final DynamicLongArray dla = new DynamicLongArray(new long[] { 3, 0 });
    Assertions.assertEquals(3, dla.getFirst());
  }

  @Test
  public void testGetAndSet() {
    final DynamicLongArray dla = new DynamicLongArray(new long[] { 1, 1, 1 });
    Assertions.assertEquals(1, dla.get(0));
    dla.set(1, 37);
    Assertions.assertEquals(37, dla.get(1));
  }

  @Test
  public void testGetOutsideBoundsIsZero() {
    final DynamicLongArray dla = new DynamicLongArray(new long[] { 1, 1, 1 });
    Assertions.assertEquals(0, dla.get(1_000_000));
  }

  @Test
  public void testEquals() {
    final DynamicLongArray dla1 = new DynamicLongArray(new long[] { 1, 1, 1 });
    final DynamicLongArray dla2 = new DynamicLongArray(new long[] { 1, 1, 1 });
    Assertions.assertTrue(dla1.equals(dla2));
    Assertions.assertTrue(dla2.equals(dla1));
  }

  @Test
  public void testEqualsIgnoreTrailingZeros() {
    final DynamicLongArray dla1 = new DynamicLongArray(new long[] { 1, 1, 1, 0, 0, 0, 0, 0, 0, 0 });
    final DynamicLongArray dla2 = new DynamicLongArray(new long[] { 1, 1, 1 });
    Assertions.assertTrue(dla1.equals(dla2));
    Assertions.assertTrue(dla2.equals(dla1));
  }

  @Test
  public void testEqualsIdentity() {
    final DynamicLongArray dla = new DynamicLongArray(new long[] { 1, 1, 1 });
    Assertions.assertTrue(dla.equals(dla));
  }

  @Test
  public void testNotEquals() {
    final DynamicLongArray dla1 = new DynamicLongArray(new long[] { 1, 2, 1 });
    final DynamicLongArray dla2 = new DynamicLongArray(new long[] { 1, 1, 1 });
    Assertions.assertFalse(dla1.equals(dla2));
    Assertions.assertFalse(dla2.equals(dla1));
  }

}
