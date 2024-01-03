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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import us.coffeecode.advent_of_code.AbstractTests;

public class TestCircularIntBuffer
extends AbstractTests {

  @Test
  public void testArrayConstructorNullArray() {
    Assertions.assertThrows(RuntimeException.class, () -> new CircularIntBuffer((int[]) null));
  }

  @Test
  public void testArrayConstructorZeroLengthArray() {
    Assertions.assertThrows(RuntimeException.class, () -> new CircularIntBuffer(new int[0]));
  }

  @Test
  public void testArrayConstructorGoodArray() {
    Assertions.assertDoesNotThrow(() -> new CircularIntBuffer(new int[1]));
  }

  @Test
  public void testArrayConstructorNullString() {
    Assertions.assertThrows(RuntimeException.class, () -> new CircularIntBuffer((String) null));
  }

  @Test
  public void testArrayConstructorZeroLengthString() {
    Assertions.assertThrows(RuntimeException.class, () -> new CircularIntBuffer(""));
  }

  @Test
  public void testArrayConstructorGoodString() {
    Assertions.assertDoesNotThrow(() -> new CircularIntBuffer("test"));
  }

  @Test
  public void testNextFromInt() {
    final int[] input = new int[] { '0', '1', '2' };
    final int[] expected = new int[input.length << 1];
    final int[] actual = new int[input.length << 1];
    System.arraycopy(input, 0, expected, 0, input.length);
    System.arraycopy(input, 0, expected, expected.length >> 1, input.length);
    final CircularIntBuffer cib = new CircularIntBuffer(input);
    for (int i = 0; i < actual.length; ++i) {
      actual[i] = cib.next();
    }
    Assertions.assertArrayEquals(expected, actual);
  }

  @Test
  public void testNextFromString() {
    final String input = "test";
    final int[] inputCodePoints = input.codePoints().toArray();
    final int[] expected = new int[input.length() << 1];
    final int[] actual = new int[input.length() << 1];
    System.arraycopy(inputCodePoints, 0, expected, 0, inputCodePoints.length);
    System.arraycopy(inputCodePoints, 0, expected, expected.length >> 1, inputCodePoints.length);
    final CircularIntBuffer cib = new CircularIntBuffer(input);
    for (int i = 0; i < actual.length; ++i) {
      actual[i] = cib.next();
    }
    Assertions.assertArrayEquals(expected, actual);
  }

}
