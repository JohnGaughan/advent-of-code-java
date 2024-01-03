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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestRange4D {

  @Test
  public void testW1() {
    final Range4D range = new Range4D(1, 2, 3, 4, 5, 6, 7, 8);
    Assertions.assertEquals(1, range.getW1());
  }

  @Test
  public void testW2() {
    final Range4D range = new Range4D(1, 2, 3, 4, 5, 6, 7, 8);
    Assertions.assertEquals(5, range.getW2());
  }

  @Test
  public void testX1() {
    final Range4D range = new Range4D(1, 2, 3, 4, 5, 6, 7, 8);
    Assertions.assertEquals(2, range.getX1());
  }

  @Test
  public void testX2() {
    final Range4D range = new Range4D(1, 2, 3, 4, 5, 6, 7, 8);
    Assertions.assertEquals(6, range.getX2());
  }

  @Test
  public void testY1() {
    final Range4D range = new Range4D(1, 2, 3, 4, 5, 6, 7, 8);
    Assertions.assertEquals(3, range.getY1());
  }

  @Test
  public void testY2() {
    final Range4D range = new Range4D(1, 2, 3, 4, 5, 6, 7, 8);
    Assertions.assertEquals(7, range.getY2());
  }

  @Test
  public void testZ1() {
    final Range4D range = new Range4D(1, 2, 3, 4, 5, 6, 7, 8);
    Assertions.assertEquals(4, range.getZ1());
  }

  @Test
  public void testZ2() {
    final Range4D range = new Range4D(1, 2, 3, 4, 5, 6, 7, 8);
    Assertions.assertEquals(8, range.getZ2());
  }

  @Test
  public void testOverlapsYes() {
    final Range4D range1 = new Range4D(1, 1, 1, 1, 8, 8, 8, 8);
    final Range4D range2 = new Range4D(6, 7, 6, 7, 10, 10, 10, 10);
    Assertions.assertTrue(range1.overlaps(range2));
    Assertions.assertTrue(range2.overlaps(range1));
  }

  @Test
  public void testOverlapsNo() {
    final Range4D range1 = new Range4D(1, 1, 1, 1, 8, 8, 8, 8);
    final Range4D range2 = new Range4D(9, 9, 9, 9, 12, 12, 12, 12);
    Assertions.assertFalse(range1.overlaps(range2));
    Assertions.assertFalse(range2.overlaps(range1));
  }

  @Test
  public void testIntersectionYes() {
    final Range4D range1 = new Range4D(1, 1, 1, 1, 8, 8, 8, 8);
    final Range4D range2 = new Range4D(6, 7, 7, 7, 10, 10, 10, 10);
    final Range4D expected = new Range4D(6, 7, 7, 7, 8, 8, 8, 8);
    Assertions.assertEquals(expected, range1.intersection(range2));
    Assertions.assertEquals(expected, range2.intersection(range1));
  }

  @Test
  public void testIntersectionReflective() {
    final Range4D range1 = new Range4D(1, 1, 1, 1, 8, 8, 8, 8);
    final Range4D intersection = range1.intersection(range1);
    Assertions.assertEquals(range1, intersection);
  }

  @Test
  public void testIntersectionNo() {
    final Range4D range1 = new Range4D(1, 1, 1, 1, 8, 8, 8, 8);
    final Range4D range2 = new Range4D(9, 9, 9, 9, 12, 12, 12, 12);
    Assertions.assertNull(range1.intersection(range2));
    Assertions.assertNull(range2.intersection(range1));
  }

  @Test
  public void testRangeWExclusive() {
    final Range4D range = new Range4D(0, 1, 3, -2, 4, 5, 6, 12);
    Assertions.assertArrayEquals(new int[] { 0, 1, 2, 3 }, range.getRangeWExclusive());
  }

  @Test
  public void testRangeWInclusive() {
    final Range4D range = new Range4D(0, -4, 1, 3, 4, 5, 8, 6);
    Assertions.assertArrayEquals(new int[] { 0, 1, 2, 3, 4 }, range.getRangeWInclusive());
  }

  @Test
  public void testRangeXExclusive() {
    final Range4D range = new Range4D(1, 0, 1, 2, 3, 4, 5, 6);
    Assertions.assertArrayEquals(new int[] { 0, 1, 2, 3 }, range.getRangeXExclusive());
  }

  @Test
  public void testRangeXInclusive() {
    final Range4D range = new Range4D(2, 0, 1, 3, 9, 4, 5, 6);
    Assertions.assertArrayEquals(new int[] { 0, 1, 2, 3, 4 }, range.getRangeXInclusive());
  }

  @Test
  public void testRangeYExclusive() {
    final Range4D range = new Range4D(11, 0, 1, 3, 14, 83, 5, 6);
    Assertions.assertArrayEquals(new int[] { 1, 2, 3, 4 }, range.getRangeYExclusive());
  }

  @Test
  public void testRangeYInclusive() {
    final Range4D range = new Range4D(-1, 0, 1, 3, 4, 50, 5, 6);
    Assertions.assertArrayEquals(new int[] { 1, 2, 3, 4, 5 }, range.getRangeYInclusive());
  }

  @Test
  public void testRangeZExclusive() {
    final Range4D range = new Range4D(0, -1, 1, 3, 4, 9, 5, 6);
    Assertions.assertArrayEquals(new int[] { 3, 4, 5 }, range.getRangeZExclusive());
  }

  @Test
  public void testRangeZInclusive() {
    final Range4D range = new Range4D(0, 1, 5, 3, 4, 2, 7, 6);
    Assertions.assertArrayEquals(new int[] { 3, 4, 5, 6 }, range.getRangeZInclusive());
  }

  @Test
  public void testSizeInclusive() {
    final Range4D range = new Range4D(0, 1, 2, 3, 4, 5, 6, 7);
    Assertions.assertEquals(625, range.sizeInclusive());
  }

  @Test
  public void testSizeExclusive() {
    final Range4D range = new Range4D(0, 1, 2, 3, 4, 5, 6, 7);
    Assertions.assertEquals(256, range.sizeExclusive());
  }

  @Test
  public void testEquals() {
    final Range4D range1 = new Range4D(1, 2, 3, 4, 5, 6, 7, 8);
    final Range4D range2 = new Range4D(1, 2, 3, 4, 5, 6, 7, 8);
    Assertions.assertTrue(range1.equals(range2));
    Assertions.assertTrue(range2.equals(range1));
  }

  @Test
  public void testNotEquals() {
    final Range4D range1 = new Range4D(1, 3, 2, 4, 5, 6, 7, 8);
    final Range4D range2 = new Range4D(1, 2, 3, 4, 5, 6, 7, 8);
    Assertions.assertFalse(range1.equals(range2));
    Assertions.assertFalse(range2.equals(range1));
  }

  @Test
  public void testHashCode() {
    final Range4D range1 = new Range4D(1, 2, 3, 4, 5, 6, 7, 8);
    final Range4D range2 = new Range4D(1, 2, 3, 4, 5, 6, 7, 8);
    Assertions.assertTrue(range1.hashCode() == range2.hashCode());
  }

}
