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
package us.coffeecode.advent_of_code.y2017;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import us.coffeecode.advent_of_code.AbstractTests;

public class TestKnotHash
extends AbstractTests {

  private static KnotHash knot;

  @BeforeAll
  public static void beforeAll() {
    knot = context.getBean(KnotHash.class);
  }

  @Test
  public void testHashOneRound() {
    final int[] actual = knot.hashOneRound(new int[] { 19, 3, 67, 4, 12, 79, 55, 112, 3, 47, 9, 13 });
    Assertions.assertArrayEquals(new int[] { 18, 17, 16, 15, 193, 114, 113, 112, 111, 99, 100, 101, 102, 103, 104, 105, 106, 107,
        108, 109, 110, 98, 97, 96, 92, 93, 94, 95, 91, 90, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
        41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70,
        71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 22, 19, 20, 21, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
        10, 11, 12, 13, 14, 192, 191, 190, 189, 188, 187, 186, 183, 184, 185, 182, 181, 180, 179, 178, 177, 176, 175, 128, 129,
        130, 131, 132, 133, 134, 135, 136, 137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148, 149, 150, 151, 152, 153,
        154, 155, 156, 157, 158, 159, 160, 161, 162, 163, 164, 165, 166, 167, 168, 169, 170, 171, 172, 173, 174, 127, 126, 125,
        124, 123, 122, 121, 120, 119, 198, 197, 196, 195, 194, 115, 116, 117, 118, 253, 252, 251, 250, 249, 248, 247, 246, 245,
        244, 231, 232, 233, 234, 235, 236, 237, 238, 239, 240, 241, 242, 243, 230, 229, 228, 227, 226, 225, 224, 223, 222, 221,
        220, 219, 218, 217, 216, 215, 214, 213, 212, 211, 210, 209, 208, 207, 206, 205, 204, 203, 202, 201, 200, 199, 254, 255 },
      actual);
  }

  @Test
  public void testHash() {
    final int[] actual = knot.hash("Hello, world!");
    Assertions.assertArrayEquals(new int[] { 111, 76, 115, 231, 164, 37, 36, 136, 67, 52, 161, 132, 41, 186, 210, 137 }, actual);
  }

}
