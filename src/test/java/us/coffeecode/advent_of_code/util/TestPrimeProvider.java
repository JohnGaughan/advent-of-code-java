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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import us.coffeecode.advent_of_code.AbstractTests;

public class TestPrimeProvider
extends AbstractTests {

  private static final int[] PRIMES_MAX_30 = new int[] { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29 };

  private static PrimeProvider primes;

  @BeforeAll
  public static void beforeAll() {
    primes = context.getBean(PrimeProvider.class);
  }

  @Test
  public void testCorrectContentsMaxNumber30() {
    final int[] actual = primes.getPrimesUpTo(30);
    Assertions.assertArrayEquals(PRIMES_MAX_30, actual);
  }

  @Test
  public void testCorrectQuantityMaxNumber30() {
    final int[] actual = primes.getPrimesUpTo(30);
    Assertions.assertEquals(PRIMES_MAX_30.length, actual.length);
  }

}
