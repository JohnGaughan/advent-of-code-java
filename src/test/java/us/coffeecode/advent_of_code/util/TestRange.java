/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2022  John Gaughan
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import us.coffeecode.advent_of_code.AbstractTests;

public class TestRange
extends AbstractTests {

  @Test
  public void testStringConstructorGood() {
    final Range expected = new Range(3, 9);
    final Range actual = new Range("3", "9");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void testStringConstructorGoodNotTrimmed() {
    final Range expected = new Range(3, 9);
    final Range actual = new Range(" 3", "9 ");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void testStringConstructorBlankX1_throws() {
    Assertions.assertThrows(NumberFormatException.class, () -> new Range("", "9"));
  }

  @Test
  public void testStringConstructorBlankX2_throws() {
    Assertions.assertThrows(NumberFormatException.class, () -> new Range("3", ""));
  }

  @Test
  public void testMerge1() {
    final List<Range> input = List.of(new Range(6, 8), new Range(7, 9));
    final List<Range> expected = List.of(new Range(6, 9));
    Assertions.assertEquals(expected, Range.merge(input));
  }

  @Test
  public void testMerge2() {
    final List<Range> input = List.of(new Range(3, 10), new Range(5, 15), new Range(16, 20));
    final List<Range> expected = List.of(new Range(3, 20));
    Assertions.assertEquals(expected, Range.merge(input));
  }

  @Test
  public void testMerge3() {
    final List<Range> input = List.of(new Range(3, 10), new Range(5, 15), new Range(20, 30));
    final List<Range> expected = List.of(new Range(3, 15), new Range(20, 30));
    Assertions.assertEquals(expected, Range.merge(input));
  }

  @Test
  public void testMergeNullList() {
    Assertions.assertThrows(RuntimeException.class, () -> Range.merge(null));
  }

  @Test
  public void testMergeNullRange1() {
    final List<Range> input = new ArrayList<>();
    input.add(null);
    Assertions.assertEquals(Collections.emptyList(), Range.merge(input));
  }

  @Test
  public void testMergeNullRange2() {
    final List<Range> input = new ArrayList<>();
    input.add(null);
    input.add(null);
    Assertions.assertEquals(Collections.emptyList(), Range.merge(input));
  }

  @Test
  public void testMergeNullRange3() {
    final List<Range> input = new ArrayList<>();
    input.add(new Range(2, 4));
    input.add(null);
    final List<Range> expected = List.of(new Range(2, 4));
    Assertions.assertEquals(expected, Range.merge(input));
  }

  @Test
  public void testMergeNullRange4() {
    final List<Range> input = new ArrayList<>();
    input.add(null);
    input.add(new Range(7, 14));
    final List<Range> expected = List.of(new Range(7, 14));
    Assertions.assertEquals(expected, Range.merge(input));
  }

  @Test
  public void testMergeNullRange5() {
    final List<Range> input = new ArrayList<>();
    input.add(new Range(2, 10));
    input.add(null);
    input.add(new Range(7, 14));
    final List<Range> expected = List.of(new Range(2, 14));
    Assertions.assertEquals(expected, Range.merge(input));
  }

  @Test
  public void testMergeNullRange6() {
    final List<Range> input = new ArrayList<>();
    input.add(new Range(1, 3));
    input.add(null);
    input.add(new Range(7, 14));
    final List<Range> expected = List.of(new Range(1, 3), new Range(7, 14));
    Assertions.assertEquals(expected, Range.merge(input));
  }

  @Test
  public void testMergeOnlyOneRange() {
    final List<Range> input = List.of(new Range(-4, 7));
    final List<Range> expected = List.of(new Range(-4, 7));
    Assertions.assertEquals(expected, Range.merge(input));
  }

  @Test
  public void testMergeSameRange() {
    final List<Range> input = List.of(new Range(6, 8), new Range(6, 8));
    final List<Range> expected = List.of(new Range(6, 8));
    Assertions.assertEquals(expected, Range.merge(input));
  }

  @Test
  public void testMergeWhollyContains() {
    final List<Range> input = List.of(new Range(5, 34), new Range(10, 14));
    final List<Range> expected = List.of(new Range(5, 34));
    Assertions.assertEquals(expected, Range.merge(input));
  }

  @Test
  public void testMergeDisjoint() {
    final List<Range> input = List.of(new Range(-7, 8), new Range(10, 14));
    final List<Range> expected = List.of(new Range(-7, 8), new Range(10, 14));
    Assertions.assertEquals(expected, Range.merge(input));
  }

  @Test
  public void testMergeAdjacent() {
    final List<Range> input = List.of(new Range(-7, 8), new Range(9, 14));
    final List<Range> expected = List.of(new Range(-7, 14));
    Assertions.assertEquals(expected, Range.merge(input));
  }

  @Test
  public void testMergeEmpty() {
    Assertions.assertEquals(List.of(), Range.merge(List.of()));
  }

  @Test
  public void testX1() {
    final Range range = new Range(1, 3);
    Assertions.assertEquals(1, range.getX1());
  }

  @Test
  public void testX2() {
    final Range range = new Range(1, 3);
    Assertions.assertEquals(3, range.getX2());
  }

  @Test
  public void testContainsExclusiveYes() {
    final Range range1 = new Range(1, 3);
    final Range range2 = new Range(1, 2);
    Assertions.assertTrue(range1.containsExclusive(range2));
  }

  @Test
  public void testContainsExclusiveNo() {
    final Range range1 = new Range(1, 3);
    final Range range2 = new Range(1, 3);
    Assertions.assertFalse(range1.containsExclusive(range2));
  }

  @Test
  public void testContainsInclusiveYes() {
    final Range range1 = new Range(1, 3);
    final Range range2 = new Range(1, 3);
    Assertions.assertTrue(range1.containsInclusive(range2));
  }

  @Test
  public void testContainsInclusiveNo() {
    final Range range1 = new Range(1, 3);
    final Range range2 = new Range(0, 3);
    Assertions.assertFalse(range1.containsInclusive(range2));
  }

  @Test
  public void testContainsIntExclusiveYes() {
    final Range range = new Range(1, 3);
    Assertions.assertTrue(range.containsExclusive(1));
  }

  @Test
  public void testContainsIntExclusiveNo() {
    final Range range = new Range(1, 3);
    Assertions.assertFalse(range.containsExclusive(3));
  }

  @Test
  public void testContainsIntInclusiveYes() {
    final Range range = new Range(1, 3);
    Assertions.assertTrue(range.containsInclusive(3));
  }

  @Test
  public void testContainsIntInclusiveNo() {
    final Range range = new Range(1, 3);
    Assertions.assertFalse(range.containsInclusive(4));
  }

  @Test
  public void testOverlapsYes() {
    final Range range1 = new Range(1, 8);
    final Range range2 = new Range(6, 10);
    Assertions.assertTrue(range1.overlaps(range2));
    Assertions.assertTrue(range2.overlaps(range1));
  }

  @Test
  public void testOverlapsNo() {
    final Range range1 = new Range(1, 8);
    final Range range2 = new Range(9, 12);
    Assertions.assertFalse(range1.overlaps(range2));
    Assertions.assertFalse(range2.overlaps(range1));
  }

  @Test
  public void testUnion1() {
    final Range range1 = new Range(3, 8);
    final Range range2 = new Range(5, 12);
    final Range range3 = new Range(3, 12);
    Assertions.assertEquals(range1.union(range2), range3);
  }

  @Test
  public void testUnion2() {
    final Range range1 = new Range(3, 8);
    final Range range2 = new Range(-4, 6);
    final Range range3 = new Range(-4, 8);
    Assertions.assertEquals(range1.union(range2), range3);
  }

  @Test
  public void testUnionDisjoint() {
    final Range range1 = new Range(3, 8);
    final Range range2 = new Range(12, 22);
    Assertions.assertNull(range1.union(range2));
  }

  @Test
  public void testUnionReflexive() {
    final Range range1 = new Range(3, 8);
    final Range range2 = new Range(5, 12);
    Assertions.assertEquals(range1.union(range2), range2.union(range1));
  }

  @Test
  public void testUnionAtEdge() {
    final Range range1 = new Range(3, 8);
    final Range range2 = new Range(8, 12);
    final Range range3 = new Range(3, 12);
    Assertions.assertEquals(range1.union(range2), range3);
  }

  @Test
  public void testUnionAdjacent() {
    final Range range1 = new Range(3, 8);
    final Range range2 = new Range(9, 12);
    final Range range3 = new Range(3, 12);
    Assertions.assertEquals(range1.union(range2), range3);
  }

  @Test
  public void testIntersectionYes() {
    final Range range1 = new Range(1, 8);
    final Range range2 = new Range(6, 10);
    final Range expected = new Range(6, 8);
    Assertions.assertEquals(expected, range1.intersection(range2));
    Assertions.assertEquals(expected, range2.intersection(range1));
  }

  @Test
  public void testIntersectionReflective() {
    final Range range1 = new Range(1, 8);
    final Range intersection = range1.intersection(range1);
    Assertions.assertEquals(range1, intersection);
  }

  @Test
  public void testIntersectionNo() {
    final Range range1 = new Range(1, 8);
    final Range range2 = new Range(9, 12);
    Assertions.assertNull(range1.intersection(range2));
    Assertions.assertNull(range2.intersection(range1));
  }

  @Test
  public void testRangeExclusive() {
    final Range range = new Range(0, 3);
    Assertions.assertArrayEquals(new int[] { 0, 1, 2 }, range.getRangeXExclusive());
  }

  @Test
  public void testRangeInclusive() {
    final Range range = new Range(0, 3);
    Assertions.assertArrayEquals(new int[] { 0, 1, 2, 3 }, range.getRangeXInclusive());
  }

  @Test
  public void testSizeInclusive() {
    final Range range = new Range(0, 3);
    Assertions.assertEquals(4, range.sizeInclusive());
  }

  @Test
  public void testSizeExclusive() {
    final Range range = new Range(0, 3);
    Assertions.assertEquals(3, range.sizeExclusive());
  }

  @Test
  public void testEquals() {
    final Range range1 = new Range(1, 3);
    final Range range2 = new Range(1, 3);
    Assertions.assertTrue(range1.equals(range2));
    Assertions.assertTrue(range2.equals(range1));
  }

  @Test
  public void testNotEquals() {
    final Range range1 = new Range(1, 2);
    final Range range2 = new Range(1, 3);
    Assertions.assertFalse(range1.equals(range2));
    Assertions.assertFalse(range2.equals(range1));
  }

  @Test
  public void testHashCode() {
    final Range range1 = new Range(1, 3);
    final Range range2 = new Range(1, 3);
    Assertions.assertTrue(range1.hashCode() == range2.hashCode());
  }

  @Test
  public void testCompareToEqual() {
    final Range range1 = new Range(0, 0);
    final Range range2 = new Range(0, 0);
    Assertions.assertTrue(range1.compareTo(range2) == 0);
    Assertions.assertTrue(range2.compareTo(range1) == 0);
  }

  @Test
  public void testCompareToContains() {
    final Range range1 = new Range(1, 6);
    final Range range2 = new Range(2, 4);
    Assertions.assertTrue(range1.compareTo(range2) < 0);
    Assertions.assertTrue(range2.compareTo(range1) > 0);
  }

  @Test
  public void testCompareTo1() {
    final Range range1 = new Range(0, 0);
    final Range range2 = new Range(1, 2);
    Assertions.assertTrue(range1.compareTo(range2) < 0);
    Assertions.assertTrue(range2.compareTo(range1) > 0);
  }

  @Test
  public void testCompareTo2() {
    final Range range1 = new Range(0, 5);
    final Range range2 = new Range(3, 7);
    Assertions.assertTrue(range1.compareTo(range2) < 0);
    Assertions.assertTrue(range2.compareTo(range1) > 0);
  }

  @Test
  public void testCompareTo3() {
    final Range range1 = new Range(1, 5);
    final Range range2 = new Range(1, 7);
    Assertions.assertTrue(range1.compareTo(range2) < 0);
    Assertions.assertTrue(range2.compareTo(range1) > 0);
  }

  @Test
  public void testCompareTo4() {
    final Range range1 = new Range(1, 7);
    final Range range2 = new Range(1, 5);
    Assertions.assertTrue(range1.compareTo(range2) > 0);
    Assertions.assertTrue(range2.compareTo(range1) < 0);
  }

  @Test
  public void testCompareTo5() {
    final Range range1 = new Range(4, 6);
    final Range range2 = new Range(9, 12);
    Assertions.assertTrue(range1.compareTo(range2) < 0);
    Assertions.assertTrue(range2.compareTo(range1) > 0);
  }

}
