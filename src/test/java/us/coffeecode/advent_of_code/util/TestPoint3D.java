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

public class TestPoint3D
extends AbstractTests {

  public void testValueOf() {
    Assertions.assertEquals(new Point3D(5, 9, -1), Point3D.valueOf("5,9,-1"));
  }

  @Test
  public void testArrayConstructorNull() {
    Assertions.assertThrows(NullPointerException.class, () -> new Point3D((int[]) null));
  }

  @Test
  public void testArrayConstructorEmpty() {
    Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> new Point3D(new int[0]));
  }

  @Test
  public void testArrayConstructorSizeOne() {
    Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> new Point3D(new int[] { 7 }));
  }

  @Test
  public void testArrayConstructorSizeTwo() {
    Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> new Point3D(new int[] { 9, 2 }));
  }

  @Test
  public void testArrayConstructorSizeThree() {
    final int x = 1;
    final int y = -4;
    final int z = 8;
    Point3D point = new Point3D(new int[] { x, y, z });
    Assertions.assertEquals(x, point.getX());
    Assertions.assertEquals(y, point.getY());
    Assertions.assertEquals(z, point.getZ());
  }

  @Test
  public void testArrayConstructorSizeFour() {
    final int x = -2;
    final int y = 72;
    final int z = 1;
    Point3D point = new Point3D(new int[] { x, y, z, 6 });
    Assertions.assertEquals(x, point.getX());
    Assertions.assertEquals(y, point.getY());
    Assertions.assertEquals(z, point.getZ());
  }

  @Test
  public void testDeltaConstructor() {
    Point3D orig = new Point3D(1, 17, -4);
    Point3D point = new Point3D(orig, 0, 2, 7);
    Assertions.assertEquals(1, point.getX());
    Assertions.assertEquals(19, point.getY());
    Assertions.assertEquals(3, point.getZ());
  }

  @Test
  public void test2dConstructorWithoutZ() {
    final int x = 2;
    final int y = -7;
    Point2D orig = new Point2D(x, y);
    Point3D point = new Point3D(orig);
    Assertions.assertEquals(x, point.getX());
    Assertions.assertEquals(y, point.getY());
    Assertions.assertEquals(0, point.getZ());
  }

  @Test
  public void test2dConstructorWithZ() {
    final int x = 99;
    final int y = 32;
    final int z = -400;
    Point2D orig = new Point2D(x, y);
    Point3D point = new Point3D(orig, z);
    Assertions.assertEquals(x, point.getX());
    Assertions.assertEquals(y, point.getY());
    Assertions.assertEquals(z, point.getZ());
  }

  @Test
  public void testNeighbors() {
    final Set<Point3D> expected = Set.of(new Point3D(1, 0, 0), new Point3D(-1, 0, 0), new Point3D(0, 1, 0), new Point3D(0, -1, 0),
      new Point3D(0, 0, 1), new Point3D(0, 0, -1));
    final List<Point3D> actual = new Point3D(0, 0, 0).getNeighbors();
    Assertions.assertTrue(expected.containsAll(actual));
    Assertions.assertTrue(actual.containsAll(expected));
  }

  @Test
  public void testGetBoolean() {
    final boolean[][][] array =
      new boolean[][][] { { { false, false }, { false, false } }, { { false, false }, { false, false } } };
    final Point3D point = new Point3D(1, 1, 1);
    Assertions.assertTrue(point.get(array) == array[1][1][1]);
    array[1][1][1] = true;
    Assertions.assertTrue(point.get(array) == array[1][1][1]);
  }

  @Test
  public void testSetBoolean() {
    final boolean[][][] array =
      new boolean[][][] { { { false, false }, { false, false } }, { { false, false }, { false, false } } };
    final Point3D point = new Point3D(1, 1, 1);
    point.set(array, true);
    Assertions.assertFalse(array[0][0][0]);
    Assertions.assertFalse(array[0][0][1]);
    Assertions.assertFalse(array[0][1][0]);
    Assertions.assertFalse(array[0][1][1]);
    Assertions.assertFalse(array[1][0][0]);
    Assertions.assertFalse(array[1][0][1]);
    Assertions.assertFalse(array[1][1][0]);
    Assertions.assertTrue(array[1][1][1]);
  }

  @Test
  public void testGetInteger() {
    final int[][][] array = new int[][][] { { { 0, 0 }, { 0, 0 } }, { { 0, 0 }, { 0, 0 } } };
    final Point3D point = new Point3D(1, 1, 1);
    Assertions.assertEquals(array[1][1][1], point.get(array));
    array[1][1][1] = -37;
    Assertions.assertEquals(array[1][1][1], point.get(array));
  }

  @Test
  public void testSetInteger() {
    final int[][][] array = new int[][][] { { { 0, 0 }, { 0, 0 } }, { { 0, 0 }, { 0, 0 } } };
    final Point3D point = new Point3D(1, 1, 1);
    point.set(array, 1);
    Assertions.assertEquals(0, array[0][0][0]);
    Assertions.assertEquals(0, array[0][0][1]);
    Assertions.assertEquals(0, array[0][1][0]);
    Assertions.assertEquals(0, array[0][1][1]);
    Assertions.assertEquals(0, array[1][0][0]);
    Assertions.assertEquals(0, array[1][0][1]);
    Assertions.assertEquals(0, array[1][1][0]);
    Assertions.assertEquals(1, array[1][1][1]);
  }

  @Test
  public void testGetLong() {
    final long[][][] array = new long[][][] { { { 0, 0 }, { 0, 0 } }, { { 0, 0 }, { 0, 0 } } };
    final Point3D point = new Point3D(1, 1, 1);
    Assertions.assertEquals(array[1][1][1], point.get(array));
    array[1][1][1] = 7_092;
    Assertions.assertEquals(array[1][1][1], point.get(array));
  }

  @Test
  public void testSetLong() {
    final long[][][] array = new long[][][] { { { 0, 0 }, { 0, 0 } }, { { 0, 0 }, { 0, 0 } } };
    final Point3D point = new Point3D(1, 1, 1);
    point.set(array, 1);
    Assertions.assertEquals(0, array[0][0][0]);
    Assertions.assertEquals(0, array[0][0][1]);
    Assertions.assertEquals(0, array[0][1][0]);
    Assertions.assertEquals(0, array[0][1][1]);
    Assertions.assertEquals(0, array[1][0][0]);
    Assertions.assertEquals(0, array[1][0][1]);
    Assertions.assertEquals(0, array[1][1][0]);
    Assertions.assertEquals(1, array[1][1][1]);
  }

  @Test
  public void testGetObject() {
    final Object[][][] array = new Object[][][] { { { null, null }, { null, null } }, { { null, null }, { null, null } } };
    final Point3D point = new Point3D(1, 1, 1);
    Assertions.assertEquals(array[1][1][1], point.get(array));
    array[1][1][1] = new Object();
    Assertions.assertSame(array[1][1][1], point.get(array));
  }

  @Test
  public void testSetObject() {
    final Object obj = new Object();
    final Object[][][] array = new Object[][][] { { { null, null }, { null, null } }, { { null, null }, { null, null } } };
    final Point3D point = new Point3D(1, 1, 1);
    point.set(array, obj);
    Assertions.assertNull(array[0][0][0]);
    Assertions.assertNull(array[0][0][1]);
    Assertions.assertNull(array[0][1][0]);
    Assertions.assertNull(array[0][1][1]);
    Assertions.assertNull(array[1][0][0]);
    Assertions.assertNull(array[1][0][1]);
    Assertions.assertNull(array[1][1][0]);
    Assertions.assertEquals(obj, array[1][1][1]);
  }

  @Test
  public void testManhattanDistanceFromOriginToOrigin() {
    final Point3D point = new Point3D(0, 0, 0);
    Assertions.assertEquals(0, point.getManhattanDistance());
  }

  @Test
  public void testManhattanDistanceFromOriginToPPP() {
    final Point3D point = new Point3D(6, 912, 58);
    Assertions.assertEquals(976, point.getManhattanDistance());
  }

  @Test
  public void testManhattanDistanceFromOriginToPPN() {
    final Point3D point = new Point3D(67, 12, -30);
    Assertions.assertEquals(109, point.getManhattanDistance());
  }

  @Test
  public void testManhattanDistanceFromOriginToPNP() {
    final Point3D point = new Point3D(83, -11, 7);
    Assertions.assertEquals(101, point.getManhattanDistance());
  }

  @Test
  public void testManhattanDistanceFromOriginToPNN() {
    final Point3D point = new Point3D(1, -4, -51);
    Assertions.assertEquals(56, point.getManhattanDistance());
  }

  @Test
  public void testManhattanDistanceFromOriginToNPP() {
    final Point3D point = new Point3D(-3, 47, 29);
    Assertions.assertEquals(79, point.getManhattanDistance());
  }

  @Test
  public void testManhattanDistanceFromOriginToNPN() {
    final Point3D point = new Point3D(-19, 3, -8);
    Assertions.assertEquals(30, point.getManhattanDistance());
  }

  @Test
  public void testManhattanDistanceFromOriginToNNP() {
    final Point3D point = new Point3D(-3, -9, 41);
    Assertions.assertEquals(53, point.getManhattanDistance());
  }

  @Test
  public void testManhattanDistanceFromOriginToNNN() {
    final Point3D point = new Point3D(-13, -9, -4);
    Assertions.assertEquals(26, point.getManhattanDistance());
  }

  @Test
  public void testManhattanDistanceFromAnotherPoint() {
    final Point3D point1 = new Point3D(1, -3, 8);
    final Point3D point2 = new Point3D(-5, -2, 0);
    Assertions.assertEquals(15, point1.getManhattanDistance(point2));
    Assertions.assertEquals(15, point2.getManhattanDistance(point1));
  }

  @Test
  public void testIsOriginYes() {
    final Point3D point = new Point3D(0, 0, 0);
    Assertions.assertTrue(point.isOrigin());
  }

  @Test
  public void testIsOriginNo() {
    final Point3D point = new Point3D(-1, 0, 0);
    Assertions.assertFalse(point.isOrigin());
  }

  @Test
  public void testIsInBooleanZeroDimensionMap() {
    final boolean[][][] map = new boolean[0][0][0];
    final Point3D point = new Point3D(0, 0, 0);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsInBooleanOnlyValidCoordinate() {
    final boolean[][][] map = new boolean[1][1][1];
    final Point3D point = new Point3D(0, 0, 0);
    Assertions.assertTrue(point.isIn(map));
  }

  @Test
  public void testIsInBooleanNegativeX() {
    final boolean[][][] map = new boolean[1][1][1];
    final Point3D point = new Point3D(-1, 0, 0);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsInBooleanNegativeY() {
    final boolean[][][] map = new boolean[1][1][1];
    final Point3D point = new Point3D(0, -1, 0);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsInBooleanNegativeZ() {
    final boolean[][][] map = new boolean[1][1][1];
    final Point3D point = new Point3D(0, 0, -1);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsInBooleanTooBigX() {
    final boolean[][][] map = new boolean[1][1][1];
    final Point3D point = new Point3D(1, 0, 0);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsInBooleanTooBigY() {
    final boolean[][][] map = new boolean[1][1][1];
    final Point3D point = new Point3D(0, 1, 0);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsInBooleanTooBigZ() {
    final boolean[][][] map = new boolean[1][1][1];
    final Point3D point = new Point3D(0, 0, 1);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsInIntZeroDimensionMap() {
    final int[][][] map = new int[0][0][0];
    final Point3D point = new Point3D(0, 0, 0);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsInIntOnlyValidCoordinate() {
    final int[][][] map = new int[1][1][1];
    final Point3D point = new Point3D(0, 0, 0);
    Assertions.assertTrue(point.isIn(map));
  }

  @Test
  public void testIsInIntNegativeX() {
    final int[][][] map = new int[1][1][1];
    final Point3D point = new Point3D(-1, 0, 0);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsInIntNegativeY() {
    final int[][][] map = new int[1][1][1];
    final Point3D point = new Point3D(0, -1, 0);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsInIntNegativeZ() {
    final int[][][] map = new int[1][1][1];
    final Point3D point = new Point3D(0, 0, -1);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsInIntTooBigX() {
    final int[][][] map = new int[1][1][1];
    final Point3D point = new Point3D(1, 0, 0);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsInIntTooBigY() {
    final int[][][] map = new int[1][1][1];
    final Point3D point = new Point3D(0, 1, 0);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsInIntTooBigZ() {
    final int[][][] map = new int[1][1][1];
    final Point3D point = new Point3D(0, 0, 1);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsInLongZeroDimensionMap() {
    final long[][][] map = new long[0][0][0];
    final Point3D point = new Point3D(0, 0, 0);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsInLongOnlyValidCoordinate() {
    final long[][][] map = new long[1][1][1];
    final Point3D point = new Point3D(0, 0, 0);
    Assertions.assertTrue(point.isIn(map));
  }

  @Test
  public void testIsInLongNegativeX() {
    final long[][][] map = new long[1][1][1];
    final Point3D point = new Point3D(-1, 0, 0);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsInLongNegativeY() {
    final long[][][] map = new long[1][1][1];
    final Point3D point = new Point3D(0, -1, 0);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsInLongNegativeZ() {
    final long[][][] map = new long[1][1][1];
    final Point3D point = new Point3D(0, 0, -1);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsInLongTooBigX() {
    final long[][][] map = new long[1][1][1];
    final Point3D point = new Point3D(1, 0, 0);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsInLongTooBigY() {
    final long[][][] map = new long[1][1][1];
    final Point3D point = new Point3D(0, 1, 0);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsInLongTooBigZ() {
    final long[][][] map = new long[1][1][1];
    final Point3D point = new Point3D(0, 0, 1);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsInObjectZeroDimensionMap() {
    final Object[][][] map = new Object[0][0][0];
    final Point3D point = new Point3D(0, 0, 0);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsInObjectOnlyValidCoordinate() {
    final Object[][][] map = new Object[1][1][1];
    final Point3D point = new Point3D(0, 0, 0);
    Assertions.assertTrue(point.isIn(map));
  }

  @Test
  public void testIsInObjectNegativeX() {
    final Object[][][] map = new Object[1][1][1];
    final Point3D point = new Point3D(-1, 0, 0);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsInObjectNegativeY() {
    final Object[][][] map = new Object[1][1][1];
    final Point3D point = new Point3D(0, -1, 0);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsInObjectNegativeZ() {
    final Object[][][] map = new Object[1][1][1];
    final Point3D point = new Point3D(0, 0, -1);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsInObjectTooBigX() {
    final Object[][][] map = new Object[1][1][1];
    final Point3D point = new Point3D(1, 0, 0);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsInObjectTooBigY() {
    final Object[][][] map = new Object[1][1][1];
    final Point3D point = new Point3D(0, 1, 0);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testIsInObjectTooBigZ() {
    final Object[][][] map = new Object[1][1][1];
    final Point3D point = new Point3D(0, 0, 1);
    Assertions.assertFalse(point.isIn(map));
  }

  @Test
  public void testAdd1() {
    final Point3D p1 = new Point3D(0, 2, -4);
    final Point3D p2 = new Point3D(1, -7, 4);
    final Point3D expected = new Point3D(1, -5, 0);
    final Point3D actual = p1.add(p2);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void testAdd2() {
    final Point3D p1 = new Point3D(0, 2, -4);
    final Point3D p2 = new Point3D(1, -7, 4);
    final Point3D expected = new Point3D(1, -5, 0);
    final Point3D actual = p2.add(p1);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void testSubtract1() {
    final Point3D p1 = new Point3D(0, 2, -4);
    final Point3D p2 = new Point3D(1, -7, 4);
    final Point3D expected = new Point3D(-1, 9, -8);
    final Point3D actual = p1.subtract(p2);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void testSubtract2() {
    final Point3D p1 = new Point3D(0, 2, -4);
    final Point3D p2 = new Point3D(1, -7, 4);
    final Point3D expected = new Point3D(1, -9, 8);
    final Point3D actual = p2.subtract(p1);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void testInvert() {
    final Point3D p1 = new Point3D(0, 2, -4);
    final Point3D expected = new Point3D(0, -2, 4);
    final Point3D actual = p1.invert();
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void testCompareToEquals() {
    final int x = 57;
    final int y = -39;
    final int z = 881;
    final Point3D point1 = new Point3D(x, y, z);
    final Point3D point2 = new Point3D(x, y, z);
    Assertions.assertEquals(0, point1.compareTo(point2));
  }

  @Test
  public void testCompareToXOrder() {
    final Point3D point1 = new Point3D(1, 0, 0);
    final Point3D point2 = new Point3D(2, 0, 0);
    Assertions.assertTrue(point2.compareTo(point1) > 0);
    Assertions.assertTrue(point1.compareTo(point2) < 0);
  }

  @Test
  public void testCompareToYOrder() {
    final Point3D point1 = new Point3D(0, 1, 0);
    final Point3D point2 = new Point3D(0, 2, 0);
    Assertions.assertTrue(point2.compareTo(point1) > 0);
    Assertions.assertTrue(point1.compareTo(point2) < 0);
  }

  @Test
  public void testCompareToZOrder() {
    final Point3D point1 = new Point3D(0, 0, 1);
    final Point3D point2 = new Point3D(0, 0, 2);
    Assertions.assertTrue(point2.compareTo(point1) > 0);
    Assertions.assertTrue(point1.compareTo(point2) < 0);
  }

  @Test
  public void testEqualsIdentity() {
    final Point3D point = new Point3D(1, 0, -4);
    Assertions.assertEquals(point, point);
  }

  @Test
  public void testEqualsEquivalent() {
    final int x = 98_449;
    final int y = -2;
    final int z = 782;
    final Point3D point1 = new Point3D(x, y, z);
    final Point3D point2 = new Point3D(x, y, z);
    Assertions.assertEquals(point1, point2);
  }

  @Test
  public void testEqualsNotEquivalent() {
    final int x = 12;
    final int y = -36;
    final int z = 2;
    final Point3D point1 = new Point3D(x, y, z);
    final Point3D point2 = new Point3D(x, z, y);
    Assertions.assertNotEquals(point1, point2);
  }

  @Test
  public void testHashCode() {
    final int x = 17;
    final int y = -1_994;
    final int z = 403;
    final Point3D point1 = new Point3D(x, y, z);
    final Point3D point2 = new Point3D(x, y, z);
    Assertions.assertEquals(point1.hashCode(), point2.hashCode());
  }

}
