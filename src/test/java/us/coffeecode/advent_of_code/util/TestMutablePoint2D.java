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

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import us.coffeecode.advent_of_code.AbstractTests;

public class TestMutablePoint2D
extends AbstractTests {

  @Test
  public void testArrayConstructorNull() {
    Assertions.assertThrows(NullPointerException.class, () -> new MutablePoint2D((int[]) null));
  }

  @Test
  public void testArrayConstructorEmpty() {
    Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> new MutablePoint2D(new int[0]));
  }

  @Test
  public void testArrayConstructorSizeOne() {
    Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> new MutablePoint2D(new int[] { 3 }));
  }

  @Test
  public void testArrayConstructorSizeTwo() {
    final int x = 2;
    final int y = 7;
    final MutablePoint2D point = new MutablePoint2D(new int[] { x, y });
    Assertions.assertEquals(x, point.getX());
    Assertions.assertEquals(y, point.getY());
  }

  @Test
  public void testArrayConstructorSizeThree() {
    final int x = 9;
    final int y = 3;
    final MutablePoint2D point = new MutablePoint2D(new int[] { x, y, 6 });
    Assertions.assertEquals(x, point.getX());
    Assertions.assertEquals(y, point.getY());
  }

  @Test
  public void testDeltaConstructor() {
    final MutablePoint2D orig = new MutablePoint2D(2, 7);
    final MutablePoint2D point = new MutablePoint2D(orig, 3, -6);
    Assertions.assertEquals(5, point.getX());
    Assertions.assertEquals(1, point.getY());
  }

  @Test
  public void test3dConstructor() {
    final int x = 6;
    final int y = 3;
    final MutablePoint3D orig = new MutablePoint3D(x, y, 9);
    final MutablePoint2D point = new MutablePoint2D(orig);
    Assertions.assertEquals(x, point.getX());
    Assertions.assertEquals(y, point.getY());
  }

  @Test
  public void testAddX() {
    final MutablePoint2D point = new MutablePoint2D(3, 5);
    point.addX(-7);
    Assertions.assertEquals(-4, point.getX());
  }

  @Test
  public void testAddY() {
    final MutablePoint2D point = new MutablePoint2D(3, 5);
    point.addY(12);
    Assertions.assertEquals(17, point.getY());
  }

  @Test
  public void testNeighbors() {
    final Set<MutablePoint2D> expected =
      Set.of(new MutablePoint2D(1, 0), new MutablePoint2D(-1, 0), new MutablePoint2D(0, 1), new MutablePoint2D(0, -1));
    final List<MutablePoint2D> actual = new MutablePoint2D(0, 0).getNeighbors();
    Assertions.assertTrue(expected.containsAll(actual));
    Assertions.assertTrue(actual.containsAll(expected));
  }

  @Test
  public void testGetBoolean() {
    final boolean[][] array = new boolean[][] { { false, false }, { false, false } };
    final MutablePoint2D point = new MutablePoint2D(1, 1);
    Assertions.assertTrue(point.get(array) == array[1][1]);
    array[1][1] = true;
    Assertions.assertTrue(point.get(array) == array[1][1]);
  }

  @Test
  public void testSetBoolean() {
    final boolean[][] array = new boolean[][] { { false, false }, { false, false } };
    final MutablePoint2D point = new MutablePoint2D(1, 1);
    point.set(array, true);
    Assertions.assertFalse(array[0][0]);
    Assertions.assertFalse(array[0][1]);
    Assertions.assertFalse(array[1][0]);
    Assertions.assertTrue(array[1][1]);
  }

  @Test
  public void testGetInteger() {
    final int[][] array = new int[][] { { 0, 0 }, { 0, 0 } };
    final MutablePoint2D point = new MutablePoint2D(1, 1);
    Assertions.assertEquals(array[1][1], point.get(array));
    array[1][1] = 700;
    Assertions.assertEquals(array[1][1], point.get(array));
  }

  @Test
  public void testSetInteger() {
    final int[][] array = new int[][] { { 0, 0 }, { 0, 0 } };
    final MutablePoint2D point = new MutablePoint2D(1, 1);
    point.set(array, 1);
    Assertions.assertEquals(0, array[0][0]);
    Assertions.assertEquals(0, array[0][1]);
    Assertions.assertEquals(0, array[1][0]);
    Assertions.assertEquals(1, array[1][1]);
  }

  @Test
  public void testGetLong() {
    final long[][] array = new long[][] { { 0, 0 }, { 0, 0 } };
    final MutablePoint2D point = new MutablePoint2D(1, 1);
    Assertions.assertEquals(array[1][1], point.get(array));
    array[1][1] = 97;
    Assertions.assertEquals(array[1][1], point.get(array));
  }

  @Test
  public void testSetLong() {
    final long[][] array = new long[][] { { 0, 0 }, { 0, 0 } };
    final MutablePoint2D point = new MutablePoint2D(1, 1);
    point.set(array, 1);
    Assertions.assertEquals(0, array[0][0]);
    Assertions.assertEquals(0, array[0][1]);
    Assertions.assertEquals(0, array[1][0]);
    Assertions.assertEquals(1, array[1][1]);
  }

  @Test
  public void testGetObject() {
    final Object[][] array = new Object[][] { { null, null }, { null, null } };
    final MutablePoint2D point = new MutablePoint2D(1, 1);
    Assertions.assertEquals(array[1][1], point.get(array));
    array[1][1] = new Object();
    Assertions.assertEquals(array[1][1], point.get(array));
  }

  @Test
  public void testSetObject() {
    final Object obj = new Object();
    final Object[][] array = new Object[][] { { null, null }, { null, null } };
    final MutablePoint2D point = new MutablePoint2D(1, 1);
    point.set(array, obj);
    Assertions.assertNull(array[0][0]);
    Assertions.assertNull(array[0][1]);
    Assertions.assertNull(array[1][0]);
    Assertions.assertEquals(obj, array[1][1]);
  }

  @Test
  public void testManhattanDistanceFromOriginToOrigin() {
    final MutablePoint2D point = new MutablePoint2D(0, 0);
    Assertions.assertEquals(0, point.getManhattanDistance());
  }

  @Test
  public void testManhattanDistanceFromOriginToQuadrant1() {
    final MutablePoint2D point = new MutablePoint2D(2, 3);
    Assertions.assertEquals(5, point.getManhattanDistance());
  }

  @Test
  public void testManhattanDistanceFromOriginToQuadrant2() {
    final MutablePoint2D point = new MutablePoint2D(-7, 8);
    Assertions.assertEquals(15, point.getManhattanDistance());
  }

  @Test
  public void testManhattanDistanceFromOriginToQuadrant3() {
    final MutablePoint2D point = new MutablePoint2D(-1, -27);
    Assertions.assertEquals(28, point.getManhattanDistance());
  }

  @Test
  public void testManhattanDistanceFromOriginToQuadrant4() {
    final MutablePoint2D point = new MutablePoint2D(4, -9);
    Assertions.assertEquals(13, point.getManhattanDistance());
  }

  @Test
  public void testManhattanDistanceFromAnotherPoint() {
    final MutablePoint2D point1 = new MutablePoint2D(5, -7);
    final MutablePoint2D point2 = new MutablePoint2D(0, 3);
    Assertions.assertEquals(15, point1.getManhattanDistance(point2));
    Assertions.assertEquals(15, point2.getManhattanDistance(point1));
  }

  @Test
  public void testIsOriginYes() {
    final MutablePoint2D point = new MutablePoint2D(0, 0);
    Assertions.assertTrue(point.isOrigin());
  }

  @Test
  public void testIsOriginNo() {
    final MutablePoint2D point = new MutablePoint2D(0, 1);
    Assertions.assertFalse(point.isOrigin());
  }

  @Test
  public void testIsInBooleanZeroDimensionMap() {
    final boolean[][] map = new boolean[0][0];
    final MutablePoint2D point = new MutablePoint2D(0, 0);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsInBooleanOnlyValidCoordinate() {
    final boolean[][] map = new boolean[1][1];
    final MutablePoint2D point = new MutablePoint2D(0, 0);
    Assertions.assertTrue(point.isIn(map));
  }

  @Test
  public void testIsInBooleanNegativeX() {
    final boolean[][] map = new boolean[1][1];
    final MutablePoint2D point = new MutablePoint2D(-1, 0);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsInBooleanNegativeY() {
    final boolean[][] map = new boolean[1][1];
    final MutablePoint2D point = new MutablePoint2D(0, -1);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsInBooleanTooBigX() {
    final boolean[][] map = new boolean[1][1];
    final MutablePoint2D point = new MutablePoint2D(1, 0);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsInBooleanTooBigY() {
    final boolean[][] map = new boolean[1][1];
    final MutablePoint2D point = new MutablePoint2D(0, 1);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsInIntZeroDimensionMap() {
    final int[][] map = new int[0][0];
    final MutablePoint2D point = new MutablePoint2D(0, 0);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsInIntOnlyValidCoordinate() {
    final int[][] map = new int[1][1];
    final MutablePoint2D point = new MutablePoint2D(0, 0);
    Assertions.assertTrue(point.isIn(map));
  }

  @Test
  public void testIsInIntNegativeX() {
    final int[][] map = new int[1][1];
    final MutablePoint2D point = new MutablePoint2D(-1, 0);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsInIntNegativeY() {
    final int[][] map = new int[1][1];
    final MutablePoint2D point = new MutablePoint2D(0, -1);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsInIntTooBigX() {
    final int[][] map = new int[1][1];
    final MutablePoint2D point = new MutablePoint2D(1, 0);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsInIntTooBigY() {
    final int[][] map = new int[1][1];
    final MutablePoint2D point = new MutablePoint2D(0, 1);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsInLongZeroDimensionMap() {
    final long[][] map = new long[0][0];
    final MutablePoint2D point = new MutablePoint2D(0, 0);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsInLongOnlyValidCoordinate() {
    final long[][] map = new long[1][1];
    final MutablePoint2D point = new MutablePoint2D(0, 0);
    Assertions.assertTrue(point.isIn(map));
  }

  @Test
  public void testIsInLongNegativeX() {
    final long[][] map = new long[1][1];
    final MutablePoint2D point = new MutablePoint2D(-1, 0);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsInLongNegativeY() {
    final long[][] map = new long[1][1];
    final MutablePoint2D point = new MutablePoint2D(0, -1);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsInLongTooBigX() {
    final long[][] map = new long[1][1];
    final MutablePoint2D point = new MutablePoint2D(1, 0);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsInLongTooBigY() {
    final long[][] map = new long[1][1];
    final MutablePoint2D point = new MutablePoint2D(0, 1);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsInObjectZeroDimensionMap() {
    final Object[][] map = new Object[0][0];
    final MutablePoint2D point = new MutablePoint2D(0, 0);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsInObjectOnlyValidCoordinate() {
    final Object[][] map = new Object[1][1];
    final MutablePoint2D point = new MutablePoint2D(0, 0);
    Assertions.assertTrue(point.isIn(map));
  }

  @Test
  public void testIsInObjectNegativeX() {
    final Object[][] map = new Object[1][1];
    final MutablePoint2D point = new MutablePoint2D(-1, 0);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsInObjectNegativeY() {
    final Object[][] map = new Object[1][1];
    final MutablePoint2D point = new MutablePoint2D(0, -1);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsInObjectTooBigX() {
    final Object[][] map = new Object[1][1];
    final MutablePoint2D point = new MutablePoint2D(1, 0);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsInObjectTooBigY() {
    final Object[][] map = new Object[1][1];
    final MutablePoint2D point = new MutablePoint2D(0, 1);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testCompareToEquals() {
    final int x = 57;
    final int y = -39;
    final MutablePoint2D point1 = new MutablePoint2D(x, y);
    final MutablePoint2D point2 = new MutablePoint2D(x, y);
    Assertions.assertEquals(0, point1.compareTo(point2));
  }

  @Test
  public void testCompareToXOrder() {
    final MutablePoint2D point1 = new MutablePoint2D(1, 0);
    final MutablePoint2D point2 = new MutablePoint2D(2, 0);
    Assertions.assertTrue(point2.compareTo(point1) > 0);
    Assertions.assertTrue(point1.compareTo(point2) < 0);
  }

  @Test
  public void testCompareToYOrder() {
    final MutablePoint2D point1 = new MutablePoint2D(0, 1);
    final MutablePoint2D point2 = new MutablePoint2D(0, 2);
    Assertions.assertTrue(point2.compareTo(point1) > 0);
    Assertions.assertTrue(point1.compareTo(point2) < 0);
  }

  @Test
  public void testEqualsIdentity() {
    final MutablePoint2D point = new MutablePoint2D(7, -4);
    Assertions.assertEquals(point, point);
  }

  @Test
  public void testEqualsEquivalent() {
    final int x = 1_094_229;
    final int y = -54_001;
    final MutablePoint2D point1 = new MutablePoint2D(x, y);
    final MutablePoint2D point2 = new MutablePoint2D(x, y);
    Assertions.assertEquals(point1, point2);
  }

  @Test
  public void testEqualsNotEquivalent() {
    final int x = 490;
    final int y = 77;
    final MutablePoint2D point1 = new MutablePoint2D(x, y);
    final MutablePoint2D point2 = new MutablePoint2D(y, x);
    Assertions.assertNotEquals(point1, point2);
  }

  @Test
  public void testHashCode() {
    final int x = 937;
    final int y = -4_012;
    final MutablePoint2D point1 = new MutablePoint2D(x, y);
    final MutablePoint2D point2 = new MutablePoint2D(x, y);
    Assertions.assertEquals(point1.hashCode(), point2.hashCode());
  }

}
