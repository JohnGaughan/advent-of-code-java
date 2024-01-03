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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import us.coffeecode.advent_of_code.AbstractTests;

public class TestLongRange
extends AbstractTests {

  @Test
  public void testStringConstructorGood() {
    final LongRange expected = new LongRange(3, 9);
    final LongRange actual = new LongRange("3", "9");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void testStringConstructorGoodNotTrimmed() {
    final LongRange expected = new LongRange(3, 9);
    final LongRange actual = new LongRange(" 3", "9 ");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void testStringConstructorBlankX1() {
    Assertions.assertThrows(NumberFormatException.class, () -> new LongRange("", "9"));
  }

  @Test
  public void testStringConstructorBlankX2() {
    Assertions.assertThrows(NumberFormatException.class, () -> new LongRange("3", ""));
  }

  @Test
  public void testMerge1() {
    final List<LongRange> input = List.of(new LongRange(6, 8), new LongRange(7, 9));
    final List<LongRange> expected = List.of(new LongRange(6, 9));
    Assertions.assertEquals(expected, LongRange.merge(input));
  }

  @Test
  public void testMerge2() {
    final List<LongRange> input = List.of(new LongRange(3, 10), new LongRange(5, 15), new LongRange(16, 20));
    final List<LongRange> expected = List.of(new LongRange(3, 20));
    Assertions.assertEquals(expected, LongRange.merge(input));
  }

  @Test
  public void testMerge3() {
    final List<LongRange> input = List.of(new LongRange(3, 10), new LongRange(5, 15), new LongRange(20, 30));
    final List<LongRange> expected = List.of(new LongRange(3, 15), new LongRange(20, 30));
    Assertions.assertEquals(expected, LongRange.merge(input));
  }

  @Test
  public void testMergeNullList() {
    Assertions.assertThrows(RuntimeException.class, () -> LongRange.merge(null));
  }

  @Test
  public void testMergeNullrange1() {
    final List<LongRange> input = new ArrayList<>();
    input.add(null);
    Assertions.assertEquals(Collections.emptyList(), LongRange.merge(input));
  }

  @Test
  public void testMergeNullrange2() {
    final List<LongRange> input = new ArrayList<>();
    input.add(null);
    input.add(null);
    Assertions.assertEquals(Collections.emptyList(), LongRange.merge(input));
  }

  @Test
  public void testMergeNullLongRange3() {
    final List<LongRange> input = new ArrayList<>();
    input.add(new LongRange(2, 4));
    input.add(null);
    final List<LongRange> expected = List.of(new LongRange(2, 4));
    Assertions.assertEquals(expected, LongRange.merge(input));
  }

  @Test
  public void testMergeNullLongRange4() {
    final List<LongRange> input = new ArrayList<>();
    input.add(null);
    input.add(new LongRange(7, 14));
    final List<LongRange> expected = List.of(new LongRange(7, 14));
    Assertions.assertEquals(expected, LongRange.merge(input));
  }

  @Test
  public void testMergeNullLongRange5() {
    final List<LongRange> input = new ArrayList<>();
    input.add(new LongRange(2, 10));
    input.add(null);
    input.add(new LongRange(7, 14));
    final List<LongRange> expected = List.of(new LongRange(2, 14));
    Assertions.assertEquals(expected, LongRange.merge(input));
  }

  @Test
  public void testMergeNullLongRange6() {
    final List<LongRange> input = new ArrayList<>();
    input.add(new LongRange(1, 3));
    input.add(null);
    input.add(new LongRange(7, 14));
    final List<LongRange> expected = List.of(new LongRange(1, 3), new LongRange(7, 14));
    Assertions.assertEquals(expected, LongRange.merge(input));
  }

  @Test
  public void testMergeOnlyOneLongRange() {
    final List<LongRange> input = List.of(new LongRange(-4, 7));
    final List<LongRange> expected = List.of(new LongRange(-4, 7));
    Assertions.assertEquals(expected, LongRange.merge(input));
  }

  @Test
  public void testMergeSameLongRange() {
    final List<LongRange> input = List.of(new LongRange(6, 8), new LongRange(6, 8));
    final List<LongRange> expected = List.of(new LongRange(6, 8));
    Assertions.assertEquals(expected, LongRange.merge(input));
  }

  @Test
  public void testMergeWhollyContains() {
    final List<LongRange> input = List.of(new LongRange(5, 34), new LongRange(10, 14));
    final List<LongRange> expected = List.of(new LongRange(5, 34));
    Assertions.assertEquals(expected, LongRange.merge(input));
  }

  @Test
  public void testMergeDisjoint() {
    final List<LongRange> input = List.of(new LongRange(-7, 8), new LongRange(10, 14));
    final List<LongRange> expected = List.of(new LongRange(-7, 8), new LongRange(10, 14));
    Assertions.assertEquals(expected, LongRange.merge(input));
  }

  @Test
  public void testMergeAdjacent() {
    final List<LongRange> input = List.of(new LongRange(-7, 8), new LongRange(9, 14));
    final List<LongRange> expected = List.of(new LongRange(-7, 14));
    Assertions.assertEquals(expected, LongRange.merge(input));
  }

  @Test
  public void testMergeEmpty() {
    Assertions.assertEquals(List.of(), LongRange.merge(List.of()));
  }

  @Test
  public void testX1() {
    final LongRange range = new LongRange(1, 3);
    Assertions.assertEquals(1, range.getX1());
  }

  @Test
  public void testX2() {
    final LongRange range = new LongRange(1, 3);
    Assertions.assertEquals(3, range.getX2());
  }

  @Test
  public void testContainsExclusiveYes() {
    final LongRange range1 = new LongRange(1, 3);
    final LongRange range2 = new LongRange(1, 2);
    Assertions.assertTrue(range1.containsExclusive(range2));
  }

  @Test
  public void testContainsExclusiveNo() {
    final LongRange range1 = new LongRange(1, 3);
    final LongRange range2 = new LongRange(1, 3);
    Assertions.assertFalse(range1.containsExclusive(range2));
  }

  @Test
  public void testContainsInclusiveYes() {
    final LongRange range1 = new LongRange(1, 3);
    final LongRange range2 = new LongRange(1, 3);
    Assertions.assertTrue(range1.containsInclusive(range2));
  }

  @Test
  public void testContainsInclusiveNo() {
    final LongRange range1 = new LongRange(1, 3);
    final LongRange range2 = new LongRange(0, 3);
    Assertions.assertFalse(range1.containsInclusive(range2));
  }

  @Test
  public void testContainsIntExclusiveYes() {
    final LongRange range = new LongRange(1, 3);
    Assertions.assertTrue(range.containsExclusive(1));
  }

  @Test
  public void testContainsIntExclusiveNo() {
    final LongRange range = new LongRange(1, 3);
    Assertions.assertFalse(range.containsExclusive(3));
  }

  @Test
  public void testContainsIntInclusiveYes() {
    final LongRange range = new LongRange(1, 3);
    Assertions.assertTrue(range.containsInclusive(3));
  }

  @Test
  public void testContainsIntInclusiveNo() {
    final LongRange range = new LongRange(1, 3);
    Assertions.assertFalse(range.containsInclusive(4));
  }

  @Test
  public void testOverlapsYes() {
    final LongRange range1 = new LongRange(1, 8);
    final LongRange range2 = new LongRange(6, 10);
    Assertions.assertTrue(range1.overlaps(range2));
    Assertions.assertTrue(range2.overlaps(range1));
  }

  @Test
  public void testOverlapsNo() {
    final LongRange range1 = new LongRange(1, 8);
    final LongRange range2 = new LongRange(9, 12);
    Assertions.assertFalse(range1.overlaps(range2));
    Assertions.assertFalse(range2.overlaps(range1));
  }

  @Test
  public void testUnion1() {
    final LongRange range1 = new LongRange(3, 8);
    final LongRange range2 = new LongRange(5, 12);
    final LongRange range3 = new LongRange(3, 12);
    Assertions.assertEquals(range1.union(range2), range3);
  }

  @Test
  public void testUnion2() {
    final LongRange range1 = new LongRange(3, 8);
    final LongRange range2 = new LongRange(-4, 6);
    final LongRange range3 = new LongRange(-4, 8);
    Assertions.assertEquals(range1.union(range2), range3);
  }

  @Test
  public void testUnionDisjoint() {
    final LongRange range1 = new LongRange(3, 8);
    final LongRange range2 = new LongRange(12, 22);
    Assertions.assertNull(range1.union(range2));
  }

  @Test
  public void testUnionReflexive() {
    final LongRange range1 = new LongRange(3, 8);
    final LongRange range2 = new LongRange(5, 12);
    Assertions.assertEquals(range1.union(range2), range2.union(range1));
  }

  @Test
  public void testUnionAtEdge() {
    final LongRange range1 = new LongRange(3, 8);
    final LongRange range2 = new LongRange(8, 12);
    final LongRange range3 = new LongRange(3, 12);
    Assertions.assertEquals(range1.union(range2), range3);
  }

  @Test
  public void testUnionAdjacent() {
    final LongRange range1 = new LongRange(3, 8);
    final LongRange range2 = new LongRange(9, 12);
    final LongRange range3 = new LongRange(3, 12);
    Assertions.assertEquals(range1.union(range2), range3);
  }

  @Test
  public void testIntersectionYes() {
    final LongRange range1 = new LongRange(1, 8);
    final LongRange range2 = new LongRange(6, 10);
    final LongRange expected = new LongRange(6, 8);
    Assertions.assertEquals(expected, range1.intersection(range2));
    Assertions.assertEquals(expected, range2.intersection(range1));
  }

  @Test
  public void testIntersectionReflective() {
    final LongRange range1 = new LongRange(1, 8);
    final LongRange intersection = range1.intersection(range1);
    Assertions.assertEquals(range1, intersection);
  }

  @Test
  public void testIntersectionNo() {
    final LongRange range1 = new LongRange(1, 8);
    final LongRange range2 = new LongRange(9, 12);
    Assertions.assertNull(range1.intersection(range2));
    Assertions.assertNull(range2.intersection(range1));
  }

  @Test
  public void testLongRangeExclusive() {
    final LongRange range = new LongRange(0, 3);
    Assertions.assertArrayEquals(new long[] { 0, 1, 2 }, range.getRangeXExclusive());
  }

  @Test
  public void testLongRangeInclusive() {
    final LongRange range = new LongRange(0, 3);
    Assertions.assertArrayEquals(new long[] { 0, 1, 2, 3 }, range.getRangeXInclusive());
  }

  @Test
  public void testSizeInclusive() {
    final LongRange range = new LongRange(0, 3);
    Assertions.assertEquals(4, range.sizeInclusive());
  }

  @Test
  public void testSizeExclusive() {
    final LongRange range = new LongRange(0, 3);
    Assertions.assertEquals(3, range.sizeExclusive());
  }

  @Test
  public void testShift1() {
    final LongRange expected = new LongRange(4, 8);
    final LongRange actual = new LongRange(1, 5).shift(3);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void testShift2() {
    final LongRange expected = new LongRange(5, 6);
    final LongRange actual = new LongRange(7, 8).shift(-2);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void testShift3() {
    final LongRange expected = new LongRange(4, 8);
    final LongRange actual = new LongRange(1, 8).shift(3);
    Assertions.assertNotEquals(expected, actual);
  }

  @Test
  public void testShift4() {
    final LongRange expected = new LongRange(4, 8);
    final LongRange actual = new LongRange(4, 5).shift(3);
    Assertions.assertNotEquals(expected, actual);
  }

  @Test
  public void testEquals() {
    final LongRange range1 = new LongRange(1, 3);
    final LongRange range2 = new LongRange(1, 3);
    Assertions.assertTrue(range1.equals(range2));
    Assertions.assertTrue(range2.equals(range1));
  }

  @Test
  public void testNotEquals() {
    final LongRange range1 = new LongRange(1, 2);
    final LongRange range2 = new LongRange(1, 3);
    Assertions.assertFalse(range1.equals(range2));
    Assertions.assertFalse(range2.equals(range1));
  }

  @Test
  public void testHashCode() {
    final LongRange range1 = new LongRange(1, 3);
    final LongRange range2 = new LongRange(1, 3);
    Assertions.assertTrue(range1.hashCode() == range2.hashCode());
  }

  @Test
  public void testCompareToEqual() {
    final LongRange range1 = new LongRange(0, 0);
    final LongRange range2 = new LongRange(0, 0);
    Assertions.assertTrue(range1.compareTo(range2) == 0);
    Assertions.assertTrue(range2.compareTo(range1) == 0);
  }

  @Test
  public void testCompareToContains() {
    final LongRange range1 = new LongRange(1, 6);
    final LongRange range2 = new LongRange(2, 4);
    Assertions.assertTrue(range1.compareTo(range2) < 0);
    Assertions.assertTrue(range2.compareTo(range1) > 0);
  }

  @Test
  public void testCompareTo1() {
    final LongRange range1 = new LongRange(0, 0);
    final LongRange range2 = new LongRange(1, 2);
    Assertions.assertTrue(range1.compareTo(range2) < 0);
    Assertions.assertTrue(range2.compareTo(range1) > 0);
  }

  @Test
  public void testCompareTo2() {
    final LongRange range1 = new LongRange(0, 5);
    final LongRange range2 = new LongRange(3, 7);
    Assertions.assertTrue(range1.compareTo(range2) < 0);
    Assertions.assertTrue(range2.compareTo(range1) > 0);
  }

  @Test
  public void testCompareTo3() {
    final LongRange range1 = new LongRange(1, 5);
    final LongRange range2 = new LongRange(1, 7);
    Assertions.assertTrue(range1.compareTo(range2) < 0);
    Assertions.assertTrue(range2.compareTo(range1) > 0);
  }

  @Test
  public void testCompareTo4() {
    final LongRange range1 = new LongRange(1, 7);
    final LongRange range2 = new LongRange(1, 5);
    Assertions.assertTrue(range1.compareTo(range2) > 0);
    Assertions.assertTrue(range2.compareTo(range1) < 0);
  }

  @Test
  public void testCompareTo5() {
    final LongRange range1 = new LongRange(4, 6);
    final LongRange range2 = new LongRange(9, 12);
    Assertions.assertTrue(range1.compareTo(range2) < 0);
    Assertions.assertTrue(range2.compareTo(range1) > 0);
  }

}
