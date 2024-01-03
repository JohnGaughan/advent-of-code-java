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
import org.junit.jupiter.api.Test;

public class TestRange3D {

  @Test
  public void testArrayConstructor() {
    final Range3D expected = new Range3D(1, 2, 3, 4, 5, 6);
    final Range3D actual = new Range3D(new int[] { 1, 2, 3, 4, 5, 6 });
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void testX1() {
    final Range3D range = new Range3D(1, 2, 3, 4, 5, 6);
    Assertions.assertEquals(1, range.getX1());
  }

  @Test
  public void testX2() {
    final Range3D range = new Range3D(1, 2, 3, 4, 5, 6);
    Assertions.assertEquals(4, range.getX2());
  }

  @Test
  public void testY1() {
    final Range3D range = new Range3D(1, 2, 3, 4, 5, 6);
    Assertions.assertEquals(2, range.getY1());
  }

  @Test
  public void testY2() {
    final Range3D range = new Range3D(1, 2, 3, 4, 5, 6);
    Assertions.assertEquals(5, range.getY2());
  }

  @Test
  public void testZ1() {
    final Range3D range = new Range3D(1, 2, 3, 4, 5, 6);
    Assertions.assertEquals(3, range.getZ1());
  }

  @Test
  public void testZ2() {
    final Range3D range = new Range3D(1, 2, 3, 4, 5, 6);
    Assertions.assertEquals(6, range.getZ2());
  }

  @Test
  public void testContainsExclusiveYes() {
    final Range3D range = new Range3D(1, 2, 3, 4, 5, 6);
    Assertions.assertTrue(range.containsExclusive(new Point3D(1, 3, 5)));
  }

  @Test
  public void testContainsExclusiveNo() {
    final Range3D range = new Range3D(1, 2, 3, 4, 5, 6);
    Assertions.assertFalse(range.containsExclusive(new Point3D(1, 4, 6)));
  }

  @Test
  public void testContainsInclusiveYes() {
    final Range3D range = new Range3D(1, 2, 3, 4, 5, 6);
    Assertions.assertTrue(range.containsInclusive(new Point3D(1, 4, 5)));
  }

  @Test
  public void testContainsInclusiveNo() {
    final Range3D range = new Range3D(1, 2, 3, 4, 5, 6);
    Assertions.assertFalse(range.containsInclusive(new Point3D(2, 1, 5)));
  }

  @Test
  public void testContainsExclusiveMutableYes() {
    final Range3D range = new Range3D(1, 2, 3, 4, 5, 6);
    Assertions.assertTrue(range.containsExclusive(new MutablePoint3D(1, 3, 5)));
  }

  @Test
  public void testContainsExclusiveMutableNo() {
    final Range3D range = new Range3D(1, 2, 3, 4, 5, 6);
    Assertions.assertFalse(range.containsExclusive(new MutablePoint3D(1, 4, 6)));
  }

  @Test
  public void testContainsInclusiveMutableYes() {
    final Range3D range = new Range3D(1, 2, 3, 4, 5, 6);
    Assertions.assertTrue(range.containsInclusive(new MutablePoint3D(1, 4, 5)));
  }

  @Test
  public void testContainsInclusiveMutableNo() {
    final Range3D range = new Range3D(1, 2, 3, 4, 5, 6);
    Assertions.assertFalse(range.containsInclusive(new MutablePoint3D(2, 6, 6)));
  }

  @Test
  public void testOverlapsYes() {
    final Range3D range1 = new Range3D(1, 1, 1, 8, 8, 8);
    final Range3D range2 = new Range3D(6, 7, 6, 10, 10, 10);
    Assertions.assertTrue(range1.overlaps(range2));
    Assertions.assertTrue(range2.overlaps(range1));
  }

  @Test
  public void testOverlapsNo() {
    final Range3D range1 = new Range3D(1, 1, 1, 8, 8, 8);
    final Range3D range2 = new Range3D(9, 9, 9, 12, 12, 12);
    Assertions.assertFalse(range1.overlaps(range2));
    Assertions.assertFalse(range2.overlaps(range1));
  }

  @Test
  public void testIntersectionYes() {
    final Range3D range1 = new Range3D(1, 1, 1, 8, 8, 8);
    final Range3D range2 = new Range3D(6, 7, 7, 10, 10, 10);
    final Range3D expected = new Range3D(6, 7, 7, 8, 8, 8);
    Assertions.assertEquals(expected, range1.intersection(range2));
    Assertions.assertEquals(expected, range2.intersection(range1));
  }

  @Test
  public void testIntersectionReflective() {
    final Range3D range1 = new Range3D(1, 1, 1, 8, 8, 8);
    final Range3D intersection = range1.intersection(range1);
    Assertions.assertEquals(range1, intersection);
  }

  @Test
  public void testIntersectionNo() {
    final Range3D range1 = new Range3D(1, 1, 1, 8, 8, 8);
    final Range3D range2 = new Range3D(9, 9, 9, 12, 12, 12);
    Assertions.assertNull(range1.intersection(range2));
    Assertions.assertNull(range2.intersection(range1));
  }

  @Test
  public void testRangeXExclusive() {
    final Range3D range = new Range3D(0, 1, 3, 4, 5, 6);
    Assertions.assertArrayEquals(new int[] { 0, 1, 2, 3 }, range.getRangeXExclusive());
  }

  @Test
  public void testRangeXInclusive() {
    final Range3D range = new Range3D(0, 1, 3, 4, 5, 6);
    Assertions.assertArrayEquals(new int[] { 0, 1, 2, 3, 4 }, range.getRangeXInclusive());
  }

  @Test
  public void testRangeYExclusive() {
    final Range3D range = new Range3D(0, 1, 3, 4, 5, 6);
    Assertions.assertArrayEquals(new int[] { 1, 2, 3, 4 }, range.getRangeYExclusive());
  }

  @Test
  public void testRangeYInclusive() {
    final Range3D range = new Range3D(0, 1, 3, 4, 5, 6);
    Assertions.assertArrayEquals(new int[] { 1, 2, 3, 4, 5 }, range.getRangeYInclusive());
  }

  @Test
  public void testRangeZExclusive() {
    final Range3D range = new Range3D(0, 1, 3, 4, 5, 6);
    Assertions.assertArrayEquals(new int[] { 3, 4, 5 }, range.getRangeZExclusive());
  }

  @Test
  public void testRangeZInclusive() {
    final Range3D range = new Range3D(0, 1, 3, 4, 5, 6);
    Assertions.assertArrayEquals(new int[] { 3, 4, 5, 6 }, range.getRangeZInclusive());
  }

  @Test
  public void testSizeInclusive() {
    final Range3D range = new Range3D(0, 1, 2, 3, 4, 5);
    Assertions.assertEquals(64, range.sizeInclusive());
  }

  @Test
  public void testSizeExclusive() {
    final Range3D range = new Range3D(0, 1, 2, 3, 4, 5);
    Assertions.assertEquals(27, range.sizeExclusive());
  }

  @Test
  public void testEquals() {
    final Range3D range1 = new Range3D(1, 2, 3, 4, 5, 6);
    final Range3D range2 = new Range3D(1, 2, 3, 4, 5, 6);
    Assertions.assertTrue(range1.equals(range2));
    Assertions.assertTrue(range2.equals(range1));
  }

  @Test
  public void testNotEquals() {
    final Range3D range1 = new Range3D(1, 3, 2, 4, 5, 6);
    final Range3D range2 = new Range3D(1, 2, 3, 4, 5, 6);
    Assertions.assertFalse(range1.equals(range2));
    Assertions.assertFalse(range2.equals(range1));
  }

  @Test
  public void testHashCode() {
    final Range3D range1 = new Range3D(1, 2, 3, 4, 5, 6);
    final Range3D range2 = new Range3D(1, 2, 3, 4, 5, 6);
    Assertions.assertTrue(range1.hashCode() == range2.hashCode());
  }

}
