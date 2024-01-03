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

import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import us.coffeecode.advent_of_code.AbstractTests;

public class TestRange2D
extends AbstractTests {

  @Test
  public void testX1() {
    final Range2D range = new Range2D(1, 2, 3, 4);
    Assertions.assertEquals(1, range.getX1());
  }

  @Test
  public void testX2() {
    final Range2D range = new Range2D(1, 2, 3, 4);
    Assertions.assertEquals(3, range.getX2());
  }

  @Test
  public void testY1() {
    final Range2D range = new Range2D(1, 2, 3, 4);
    Assertions.assertEquals(2, range.getY1());
  }

  @Test
  public void testY2() {
    final Range2D range = new Range2D(1, 2, 3, 4);
    Assertions.assertEquals(4, range.getY2());
  }

  @Test
  public void testContainsExclusiveYes() {
    final Range2D range = new Range2D(1, 2, 3, 4);
    Assertions.assertTrue(range.containsExclusive(new Point2D(1, 3)));
  }

  @Test
  public void testContainsExclusiveNo() {
    final Range2D range = new Range2D(1, 2, 3, 4);
    Assertions.assertFalse(range.containsExclusive(new Point2D(1, 4)));
  }

  @Test
  public void testContainsInclusiveYes() {
    final Range2D range = new Range2D(1, 2, 3, 4);
    Assertions.assertTrue(range.containsInclusive(new Point2D(1, 4)));
  }

  @Test
  public void testContainsInclusiveNo() {
    final Range2D range = new Range2D(1, 2, 3, 4);
    Assertions.assertFalse(range.containsInclusive(new Point2D(2, 5)));
  }

  @Test
  public void testContainsExclusiveMutableYes() {
    final Range2D range = new Range2D(1, 2, 3, 4);
    Assertions.assertTrue(range.containsExclusive(new MutablePoint2D(1, 3)));
  }

  @Test
  public void testContainsExclusiveMutableNo() {
    final Range2D range = new Range2D(1, 2, 3, 4);
    Assertions.assertFalse(range.containsExclusive(new MutablePoint2D(1, 4)));
  }

  @Test
  public void testContainsInclusiveMutableYes() {
    final Range2D range = new Range2D(1, 2, 3, 4);
    Assertions.assertTrue(range.containsInclusive(new MutablePoint2D(1, 4)));
  }

  @Test
  public void testContainsInclusiveMutableNo() {
    final Range2D range = new Range2D(1, 2, 3, 4);
    Assertions.assertFalse(range.containsInclusive(new MutablePoint2D(2, 5)));
  }

  @Test
  public void testContainsAnyExclusiveYes() {
    final Range2D range = new Range2D(1, 2, 3, 4);
    final Collection<Point2D> points = List.of(new Point2D(1, 3), new Point2D(1, 1), new Point2D(1, 3));
    Assertions.assertTrue(range.containsAnyExclusive(points));
  }

  @Test
  public void testContainsAnyExclusiveNo() {
    final Range2D range = new Range2D(1, 2, 3, 4);
    final Collection<Point2D> points = List.of(new Point2D(1, 4), new Point2D(2, 1));
    Assertions.assertFalse(range.containsAnyExclusive(points));
  }

  @Test
  public void testContainsAnyInclusiveYes() {
    final Range2D range = new Range2D(1, 2, 3, 4);
    final Collection<Point2D> points = List.of(new Point2D(1, 4), new Point2D(2, 3));
    Assertions.assertTrue(range.containsAnyInclusive(points));
  }

  @Test
  public void testContainsAnyInclusiveNo() {
    final Range2D range = new Range2D(1, 2, 3, 4);
    final Collection<Point2D> points = List.of(new Point2D(2, 5), new Point2D(2, 0));
    Assertions.assertFalse(range.containsAnyInclusive(points));
  }

  @Test
  public void testOverlapsYes() {
    final Range2D range1 = new Range2D(1, 1, 8, 8);
    final Range2D range2 = new Range2D(6, 7, 10, 10);
    Assertions.assertTrue(range1.overlaps(range2));
    Assertions.assertTrue(range2.overlaps(range1));
  }

  @Test
  public void testOverlapsNo() {
    final Range2D range1 = new Range2D(1, 1, 8, 8);
    final Range2D range2 = new Range2D(9, 9, 12, 12);
    Assertions.assertFalse(range1.overlaps(range2));
    Assertions.assertFalse(range2.overlaps(range1));
  }

  @Test
  public void testIntersectionYes() {
    final Range2D range1 = new Range2D(1, 1, 8, 8);
    final Range2D range2 = new Range2D(6, 7, 10, 10);
    final Range2D expected = new Range2D(6, 7, 8, 8);
    Assertions.assertEquals(expected, range1.intersection(range2));
    Assertions.assertEquals(expected, range2.intersection(range1));
  }

  @Test
  public void testIntersectionReflective() {
    final Range2D range1 = new Range2D(1, 1, 8, 8);
    final Range2D intersection = range1.intersection(range1);
    Assertions.assertEquals(range1, intersection);
  }

  @Test
  public void testIntersectionNo() {
    final Range2D range1 = new Range2D(1, 1, 8, 8);
    final Range2D range2 = new Range2D(9, 9, 12, 12);
    Assertions.assertNull(range1.intersection(range2));
    Assertions.assertNull(range2.intersection(range1));
  }

  @Test
  public void testRangeXExclusive() {
    final Range2D range = new Range2D(0, 1, 3, 4);
    Assertions.assertArrayEquals(new int[] { 0, 1, 2 }, range.getRangeXExclusive());
  }

  @Test
  public void testRangeXInclusive() {
    final Range2D range = new Range2D(0, 1, 3, 4);
    Assertions.assertArrayEquals(new int[] { 0, 1, 2, 3 }, range.getRangeXInclusive());
  }

  @Test
  public void testRangeYExclusive() {
    final Range2D range = new Range2D(0, 1, 3, 4);
    Assertions.assertArrayEquals(new int[] { 1, 2, 3 }, range.getRangeYExclusive());
  }

  @Test
  public void testRangeYInclusive() {
    final Range2D range = new Range2D(0, 1, 3, 4);
    Assertions.assertArrayEquals(new int[] { 1, 2, 3, 4 }, range.getRangeYInclusive());
  }

  @Test
  public void testSizeInclusive() {
    final Range2D range = new Range2D(0, 1, 3, 4);
    Assertions.assertEquals(16, range.sizeInclusive());
  }

  @Test
  public void testSizeExclusive() {
    final Range2D range = new Range2D(0, 1, 3, 4);
    Assertions.assertEquals(9, range.sizeExclusive());
  }

  @Test
  public void testEquals() {
    final Range2D range1 = new Range2D(1, 2, 3, 4);
    final Range2D range2 = new Range2D(1, 2, 3, 4);
    Assertions.assertTrue(range1.equals(range2));
    Assertions.assertTrue(range2.equals(range1));
  }

  @Test
  public void testNotEquals() {
    final Range2D range1 = new Range2D(1, 3, 2, 4);
    final Range2D range2 = new Range2D(1, 2, 3, 4);
    Assertions.assertFalse(range1.equals(range2));
    Assertions.assertFalse(range2.equals(range1));
  }

  @Test
  public void testHashCode() {
    final Range2D range1 = new Range2D(1, 2, 3, 4);
    final Range2D range2 = new Range2D(1, 2, 3, 4);
    Assertions.assertTrue(range1.hashCode() == range2.hashCode());
  }

}
