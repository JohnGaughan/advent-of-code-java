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
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import us.coffeecode.advent_of_code.AbstractTests;

public class TestPoint2D
extends AbstractTests {

  @Test
  public void testArrayConstructorNull() {
    Assertions.assertThrows(NullPointerException.class, () -> new Point2D((int[]) null));
  }

  @Test
  public void testArrayConstructorEmpty() {
    Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> new Point2D(new int[0]));
  }

  @Test
  public void testArrayConstructorSizeOne() {
    Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> new Point2D(new int[] { 3 }));
  }

  @Test
  public void testArrayConstructorSizeTwo() {
    final int x = 2;
    final int y = 7;
    Point2D point = new Point2D(new int[] { x, y });
    Assertions.assertEquals(x, point.getX());
    Assertions.assertEquals(y, point.getY());
  }

  @Test
  public void testArrayConstructorSizeThree() {
    final int x = 9;
    final int y = 3;
    Point2D point = new Point2D(new int[] { x, y, 6 });
    Assertions.assertEquals(x, point.getX());
    Assertions.assertEquals(y, point.getY());
  }

  @Test
  public void testDeltaConstructor() {
    Point2D orig = new Point2D(2, 7);
    Point2D point = new Point2D(orig, 3, -6);
    Assertions.assertEquals(5, point.getX());
    Assertions.assertEquals(1, point.getY());
  }

  @Test
  public void test3dConstructor() {
    final int x = 6;
    final int y = 3;
    Point3D orig = new Point3D(x, y, 9);
    Point2D point = new Point2D(orig);
    Assertions.assertEquals(x, point.getX());
    Assertions.assertEquals(y, point.getY());
  }

  @Test
  public void testCardinalNeighbors() {
    final Set<Point2D> expected = Set.of(new Point2D(4, 6), new Point2D(2, 6), new Point2D(3, 5), new Point2D(3, 7));
    final List<Point2D> actual = new Point2D(3, 6).getCardinalNeighbors();
    Assertions.assertTrue(expected.containsAll(actual));
    Assertions.assertTrue(actual.containsAll(expected));
  }

  @Test
  public void testDiagonalNeighbors() {
    final Set<Point2D> expected = Set.of(new Point2D(3, 6), new Point2D(3, 8), new Point2D(5, 6), new Point2D(5, 8));
    final List<Point2D> actual = new Point2D(4, 7).getDiagonalNeighbors();
    Assertions.assertTrue(expected.containsAll(actual));
    Assertions.assertTrue(actual.containsAll(expected));
  }

  @Test
  public void testXNeighbors() {
    final Point2D p = new Point2D(15, -8);
    final Collection<Point2D> expected = List.of(new Point2D(14, -8), new Point2D(16, -8));
    final Collection<Point2D> actual = p.getXNeighbors();
    Assertions.assertTrue(expected.containsAll(actual));
    Assertions.assertTrue(actual.containsAll(expected));
  }

  @Test
  public void testYNeighbors() {
    final Point2D p = new Point2D(-48, 4);
    final Collection<Point2D> expected = List.of(new Point2D(-48, 3), new Point2D(-48, 5));
    final Collection<Point2D> actual = p.getYNeighbors();
    Assertions.assertTrue(expected.containsAll(actual));
    Assertions.assertTrue(actual.containsAll(expected));
  }

  @Test
  public void testAllNeighbors() {
    final Collection<Point2D> expected = Set.of(new Point2D(-6, 8), new Point2D(-5, 8), new Point2D(-4, 8), new Point2D(-6, 7),
      new Point2D(-4, 7), new Point2D(-6, 6), new Point2D(-5, 6), new Point2D(-4, 6));
    final Collection<Point2D> actual = new Point2D(-5, 7).getAllNeighbors();
    Assertions.assertTrue(expected.containsAll(actual));
    Assertions.assertTrue(actual.containsAll(expected));
  }

  @Test
  public void testAllNeighborsEqualsDiagonalCombinedWithCardinal() {
    final Point2D p = new Point2D(5, -4);
    final Collection<Point2D> expected = p.getCardinalNeighbors();
    expected.addAll(p.getDiagonalNeighbors());
    final Collection<Point2D> actual = p.getAllNeighbors();
    Assertions.assertTrue(expected.containsAll(actual));
    Assertions.assertTrue(actual.containsAll(expected));
  }

  @Test
  public void testGetBoolean() {
    final boolean[][] array = new boolean[][] { { false, false }, { false, false } };
    final Point2D point = new Point2D(1, 1);
    Assertions.assertTrue(point.get(array) == array[1][1]);
    array[1][1] = true;
    Assertions.assertTrue(point.get(array) == array[1][1]);
  }

  @Test
  public void testSetBoolean() {
    final boolean[][] array = new boolean[][] { { false, false }, { false, false } };
    final Point2D point = new Point2D(1, 1);
    point.set(array, true);
    Assertions.assertFalse(array[0][0]);
    Assertions.assertFalse(array[0][1]);
    Assertions.assertFalse(array[1][0]);
    Assertions.assertTrue(array[1][1]);
  }

  @Test
  public void testGetInteger() {
    final int[][] array = new int[][] { { 0, 0 }, { 0, 0 } };
    final Point2D point = new Point2D(1, 1);
    Assertions.assertEquals(array[1][1], point.get(array));
    array[1][1] = 700;
    Assertions.assertEquals(array[1][1], point.get(array));
  }

  @Test
  public void testSetInteger() {
    final int[][] array = new int[][] { { 0, 0 }, { 0, 0 } };
    final Point2D point = new Point2D(1, 1);
    point.set(array, 1);
    Assertions.assertEquals(0, array[0][0]);
    Assertions.assertEquals(0, array[0][1]);
    Assertions.assertEquals(0, array[1][0]);
    Assertions.assertEquals(1, array[1][1]);
  }

  @Test
  public void testGetLong() {
    final long[][] array = new long[][] { { 0, 0 }, { 0, 0 } };
    final Point2D point = new Point2D(1, 1);
    Assertions.assertEquals(array[1][1], point.get(array));
    array[1][1] = 97;
    Assertions.assertEquals(array[1][1], point.get(array));
  }

  @Test
  public void testSetLong() {
    final long[][] array = new long[][] { { 0, 0 }, { 0, 0 } };
    final Point2D point = new Point2D(1, 1);
    point.set(array, 1);
    Assertions.assertEquals(0, array[0][0]);
    Assertions.assertEquals(0, array[0][1]);
    Assertions.assertEquals(0, array[1][0]);
    Assertions.assertEquals(1, array[1][1]);
  }

  @Test
  public void testGetObject() {
    final Object[][] array = new Object[][] { { null, null }, { null, null } };
    final Point2D point = new Point2D(1, 1);
    Assertions.assertEquals(array[1][1], point.get(array));
    array[1][1] = new Object();
    Assertions.assertEquals(array[1][1], point.get(array));
  }

  @Test
  public void testSetObject() {
    final Object obj = new Object();
    final Object[][] array = new Object[][] { { null, null }, { null, null } };
    final Point2D point = new Point2D(1, 1);
    point.set(array, obj);
    Assertions.assertNull(array[0][0]);
    Assertions.assertNull(array[0][1]);
    Assertions.assertNull(array[1][0]);
    Assertions.assertEquals(obj, array[1][1]);
  }

  @Test
  public void testManhattanDistanceFromOriginToOrigin() {
    final Point2D point = new Point2D(0, 0);
    Assertions.assertEquals(0, point.getManhattanDistance());
  }

  @Test
  public void testManhattanDistanceFromOriginToQuadrant1() {
    final Point2D point = new Point2D(2, 3);
    Assertions.assertEquals(5, point.getManhattanDistance());
  }

  @Test
  public void testManhattanDistanceFromOriginToQuadrant2() {
    final Point2D point = new Point2D(-7, 8);
    Assertions.assertEquals(15, point.getManhattanDistance());
  }

  @Test
  public void testManhattanDistanceFromOriginToQuadrant3() {
    final Point2D point = new Point2D(-1, -27);
    Assertions.assertEquals(28, point.getManhattanDistance());
  }

  @Test
  public void testManhattanDistanceFromOriginToQuadrant4() {
    final Point2D point = new Point2D(4, -9);
    Assertions.assertEquals(13, point.getManhattanDistance());
  }

  @Test
  public void testManhattanDistanceFromAnotherPoint() {
    final Point2D point1 = new Point2D(5, -7);
    final Point2D point2 = new Point2D(0, 3);
    Assertions.assertEquals(15, point1.getManhattanDistance(point2));
    Assertions.assertEquals(15, point2.getManhattanDistance(point1));
  }

  @Test
  public void testIsOriginYes() {
    final Point2D point = new Point2D(0, 0);
    Assertions.assertTrue(point.isOrigin());
  }

  @Test
  public void testIsOriginNo() {
    final Point2D point = new Point2D(0, 1);
    Assertions.assertFalse(point.isOrigin());
  }

  @Test
  public void testIsInBooleanZeroDimensionMap() {
    final boolean[][] map = new boolean[0][0];
    final Point2D point = new Point2D(0, 0);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsInBooleanOnlyValidCoordinate() {
    final boolean[][] map = new boolean[1][1];
    final Point2D point = new Point2D(0, 0);
    Assertions.assertTrue(point.isIn(map));
  }

  @Test
  public void testIsInBooleanNegativeX() {
    final boolean[][] map = new boolean[1][1];
    final Point2D point = new Point2D(-1, 0);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsInBooleanNegativeY() {
    final boolean[][] map = new boolean[1][1];
    final Point2D point = new Point2D(0, -1);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsInBooleanTooBigX() {
    final boolean[][] map = new boolean[1][1];
    final Point2D point = new Point2D(1, 0);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsInBooleanTooBigY() {
    final boolean[][] map = new boolean[1][1];
    final Point2D point = new Point2D(0, 1);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsInIntZeroDimensionMap() {
    final int[][] map = new int[0][0];
    final Point2D point = new Point2D(0, 0);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsInIntOnlyValidCoordinate() {
    final int[][] map = new int[1][1];
    final Point2D point = new Point2D(0, 0);
    Assertions.assertTrue(point.isIn(map));
  }

  @Test
  public void testIsInIntNegativeX() {
    final int[][] map = new int[1][1];
    final Point2D point = new Point2D(-1, 0);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsInIntNegativeY() {
    final int[][] map = new int[1][1];
    final Point2D point = new Point2D(0, -1);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsInIntTooBigX() {
    final int[][] map = new int[1][1];
    final Point2D point = new Point2D(1, 0);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsInIntTooBigY() {
    final int[][] map = new int[1][1];
    final Point2D point = new Point2D(0, 1);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsInLongZeroDimensionMap() {
    final long[][] map = new long[0][0];
    final Point2D point = new Point2D(0, 0);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsInLongOnlyValidCoordinate() {
    final long[][] map = new long[1][1];
    final Point2D point = new Point2D(0, 0);
    Assertions.assertTrue(point.isIn(map));
  }

  @Test
  public void testIsInLongNegativeX() {
    final long[][] map = new long[1][1];
    final Point2D point = new Point2D(-1, 0);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsInLongNegativeY() {
    final long[][] map = new long[1][1];
    final Point2D point = new Point2D(0, -1);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsInLongTooBigX() {
    final long[][] map = new long[1][1];
    final Point2D point = new Point2D(1, 0);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsInLongTooBigY() {
    final long[][] map = new long[1][1];
    final Point2D point = new Point2D(0, 1);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsInObjectZeroDimensionMap() {
    final Object[][] map = new Object[0][0];
    final Point2D point = new Point2D(0, 0);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsInObjectOnlyValidCoordinate() {
    final Object[][] map = new Object[1][1];
    final Point2D point = new Point2D(0, 0);
    Assertions.assertTrue(point.isIn(map));
  }

  @Test
  public void testIsInObjectNegativeX() {
    final Object[][] map = new Object[1][1];
    final Point2D point = new Point2D(-1, 0);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsInObjectNegativeY() {
    final Object[][] map = new Object[1][1];
    final Point2D point = new Point2D(0, -1);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsInObjectTooBigX() {
    final Object[][] map = new Object[1][1];
    final Point2D point = new Point2D(1, 0);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsInObjectTooBigY() {
    final Object[][] map = new Object[1][1];
    final Point2D point = new Point2D(0, 1);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsAdjacent() {
    final Point2D p0 = new Point2D(0, 0);
    final Point2D p1 = new Point2D(-1, -1);
    final Point2D p2 = new Point2D(-1, 0);
    final Point2D p3 = new Point2D(-1, 1);
    final Point2D p4 = new Point2D(0, -1);
    final Point2D p5 = new Point2D(0, 1);
    final Point2D p6 = new Point2D(1, -1);
    final Point2D p7 = new Point2D(1, 0);
    final Point2D p8 = new Point2D(1, 1);
    final Point2D p9 = new Point2D(2, 0);
    Assertions.assertFalse(p0.isAdjacent(p0));
    Assertions.assertTrue(p0.isAdjacent(p1));
    Assertions.assertTrue(p0.isAdjacent(p2));
    Assertions.assertTrue(p0.isAdjacent(p3));
    Assertions.assertTrue(p0.isAdjacent(p4));
    Assertions.assertTrue(p0.isAdjacent(p5));
    Assertions.assertTrue(p0.isAdjacent(p6));
    Assertions.assertTrue(p0.isAdjacent(p7));
    Assertions.assertTrue(p0.isAdjacent(p8));
    Assertions.assertFalse(p0.isAdjacent(p9));
  }

  @Test
  public void testIsAdjacentCardinally() {
    final Point2D p0 = new Point2D(0, 0);
    final Point2D p1 = new Point2D(-1, -1);
    final Point2D p2 = new Point2D(-1, 0);
    final Point2D p3 = new Point2D(-1, 1);
    final Point2D p4 = new Point2D(0, -1);
    final Point2D p5 = new Point2D(0, 1);
    final Point2D p6 = new Point2D(1, -1);
    final Point2D p7 = new Point2D(1, 0);
    final Point2D p8 = new Point2D(1, 1);
    final Point2D p9 = new Point2D(2, 0);
    Assertions.assertFalse(p0.isAdjacentCardinally(p0));
    Assertions.assertFalse(p0.isAdjacentCardinally(p1));
    Assertions.assertTrue(p0.isAdjacentCardinally(p2));
    Assertions.assertFalse(p0.isAdjacentCardinally(p3));
    Assertions.assertTrue(p0.isAdjacentCardinally(p4));
    Assertions.assertTrue(p0.isAdjacentCardinally(p5));
    Assertions.assertFalse(p0.isAdjacentCardinally(p6));
    Assertions.assertTrue(p0.isAdjacentCardinally(p7));
    Assertions.assertFalse(p0.isAdjacentCardinally(p8));
    Assertions.assertFalse(p0.isAdjacentCardinally(p9));
  }

  @Test
  public void testIsAdjacentDiagonally() {
    final Point2D p0 = new Point2D(0, 0);
    final Point2D p1 = new Point2D(-1, -1);
    final Point2D p2 = new Point2D(-1, 0);
    final Point2D p3 = new Point2D(-1, 1);
    final Point2D p4 = new Point2D(0, -1);
    final Point2D p5 = new Point2D(0, 1);
    final Point2D p6 = new Point2D(1, -1);
    final Point2D p7 = new Point2D(1, 0);
    final Point2D p8 = new Point2D(1, 1);
    final Point2D p9 = new Point2D(2, 0);
    Assertions.assertFalse(p0.isAdjacentDiagonally(p0));
    Assertions.assertTrue(p0.isAdjacentDiagonally(p1));
    Assertions.assertFalse(p0.isAdjacentDiagonally(p2));
    Assertions.assertTrue(p0.isAdjacentDiagonally(p3));
    Assertions.assertFalse(p0.isAdjacentDiagonally(p4));
    Assertions.assertFalse(p0.isAdjacentDiagonally(p5));
    Assertions.assertTrue(p0.isAdjacentDiagonally(p6));
    Assertions.assertFalse(p0.isAdjacentDiagonally(p7));
    Assertions.assertTrue(p0.isAdjacentDiagonally(p8));
    Assertions.assertFalse(p0.isAdjacentDiagonally(p9));
  }

  @Test
  public void testAddPoint1() {
    final Point2D p1 = new Point2D(0, 2);
    final Point2D p2 = new Point2D(1, -7);
    final Point2D expected = new Point2D(1, -5);
    final Point2D actual = p1.add(p2);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void testAddPoint2() {
    final Point2D p1 = new Point2D(0, 2);
    final Point2D p2 = new Point2D(1, -7);
    final Point2D expected = new Point2D(1, -5);
    final Point2D actual = p2.add(p1);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void testAddInts() {
    final Point2D p1 = new Point2D(0, 2);
    final int dx = 1;
    final int dy = -7;
    final Point2D expected = new Point2D(1, -5);
    final Point2D actual = p1.add(dx, dy);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void testSubtract1() {
    final Point2D p1 = new Point2D(0, 2);
    final Point2D p2 = new Point2D(1, -7);
    final Point2D expected = new Point2D(-1, 9);
    final Point2D actual = p1.subtract(p2);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void testSubtract2() {
    final Point2D p1 = new Point2D(0, 2);
    final Point2D p2 = new Point2D(1, -7);
    final Point2D expected = new Point2D(1, -9);
    final Point2D actual = p2.subtract(p1);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void testInvert() {
    final Point2D p1 = new Point2D(0, 2);
    final Point2D expected = new Point2D(0, -2);
    final Point2D actual = p1.invert();
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void testCompareToEquals() {
    final int x = 57;
    final int y = -39;
    final Point2D point1 = new Point2D(x, y);
    final Point2D point2 = new Point2D(x, y);
    Assertions.assertEquals(0, point1.compareTo(point2));
  }

  @Test
  public void testCompareToXOrder() {
    final Point2D point1 = new Point2D(1, 0);
    final Point2D point2 = new Point2D(2, 0);
    Assertions.assertTrue(point2.compareTo(point1) > 0);
    Assertions.assertTrue(point1.compareTo(point2) < 0);
  }

  @Test
  public void testCompareToYOrder() {
    final Point2D point1 = new Point2D(0, 1);
    final Point2D point2 = new Point2D(0, 2);
    Assertions.assertTrue(point2.compareTo(point1) > 0);
    Assertions.assertTrue(point1.compareTo(point2) < 0);
  }

  @Test
  public void testEqualsIdentity() {
    final Point2D point = new Point2D(7, -4);
    Assertions.assertEquals(point, point);
  }

  @Test
  public void testEqualsEquivalent() {
    final int x = 1_094_229;
    final int y = -54_001;
    final Point2D point1 = new Point2D(x, y);
    final Point2D point2 = new Point2D(x, y);
    Assertions.assertEquals(point1, point2);
  }

  @Test
  public void testEqualsNotEquivalent() {
    final int x = 490;
    final int y = 77;
    final Point2D point1 = new Point2D(x, y);
    final Point2D point2 = new Point2D(y, x);
    Assertions.assertNotEquals(point1, point2);
  }

  @Test
  public void testHashCode() {
    final int x = 937;
    final int y = -4_012;
    final Point2D point1 = new Point2D(x, y);
    final Point2D point2 = new Point2D(x, y);
    Assertions.assertEquals(point1.hashCode(), point2.hashCode());
  }

}
